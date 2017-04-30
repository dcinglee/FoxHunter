package com.foxhunter.client.service;

import com.foxhunter.blacklist.dao.BlackQQGroupDao;
import com.foxhunter.blacklist.dao.BlacklistDao;
import com.foxhunter.blacklist.entity.BlackQQGroup;
import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.blacklist.service.BlacklistService;
import com.foxhunter.client.dao.ClientDao;
import com.foxhunter.client.entity.Client;
import com.foxhunter.common.config.AppConfig;
import com.foxhunter.common.vo.ResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客户端服务类接口实现类
 *
 * @author Ewing
 */
@Service
public class ClientServiceImpl implements ClientService {
    private Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    // 黑名单名称只能包含空格、数字、中英文。
    private String NAME_NOT_PATTERN = "[^0-9a-zA-Z\\u4e00-\\u9fa5 ]+";

    private ClientDao clientDao;

    private BlacklistDao blacklistDao;
    private BlacklistService blacklistService;

    private BlackQQGroupDao blackQQGroupDao;

    @Autowired
    public void setBlacklistDao(BlacklistDao blacklistDao) {
        this.blacklistDao = blacklistDao;
    }

    @Autowired
    public void setBlacklistService(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @Autowired
    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Autowired
    public void setBlackQQGroupDao(BlackQQGroupDao blackQQGroupDao) {
        this.blackQQGroupDao = blackQQGroupDao;
    }

    /**
     * 封装给客户端查看的信息。
     */
    private static List<ClientBlacklist> toClientBlacklists(List<Blacklist> blacklists) {
        int size = blacklists.size();
        List<ClientBlacklist> clientBlacklists = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Blacklist blacklist = blacklists.get(i);
            ClientBlacklist clientBlacklist = new ClientBlacklist();
            clientBlacklist.setName(blacklist.getName());
            clientBlacklist.setLevelValue(blacklist.getLevelValue());
            clientBlacklist.setOrgTypeValue(blacklist.getOrgTypeValue());
            clientBlacklist.setStateValue(blacklist.getStateValue());
            clientBlacklist.setPhoneNo(blacklist.getPhoneNo());
            clientBlacklist.setQqNo(blacklist.getQqNo());
            clientBlacklist.setMicroNo(blacklist.getMicroNo());
            clientBlacklist.setAddress(blacklist.getAddress());
            clientBlacklists.add(clientBlacklist);
        }
        return clientBlacklists;
    }

    @Override
    public Client addClient(Client client) {
        String name = client.getName();
        String password = client.getPassword();
        if (!StringUtils.hasText(name) || !StringUtils.hasText(password)) {
            throw new RuntimeException("名称和密码不能为空！");
        }
        if (clientDao.findFirstByName(name) != null) {
            throw new RuntimeException("该客户端名称已存在！");
        }
        // 加密存储密码 MD5（名称+明文）
        String passwordMd5 = name + password;
        passwordMd5 = DigestUtils.md5DigestAsHex(passwordMd5.getBytes());
        client.setPassword(passwordMd5);
        client.setStateValue(1); // 1为正常状态，2为锁定状态
        client.setCreateDate(new Date());
        client.setBalance(0f);
        client.setTotalNum(0L);
        return clientDao.save(client);
    }

    @Override
    @Transactional
    public Client updateClient(Client client) {
        String newPassword = client.getPassword();
        if (!StringUtils.hasText(newPassword)) {
            throw new RuntimeException("新密码不能为空！");
        }
        Client oldClient = clientDao.findOne(client.getClientId());
        if (oldClient == null) {
            throw new RuntimeException("该客户端不存在或已删除！");
        }
        // 修改了密码，此处的密码是明文，需要加密
        if (!newPassword.equals(oldClient.getPassword())) {
            String passwordMd5 = oldClient.getName() + newPassword;
            passwordMd5 = DigestUtils.md5DigestAsHex(passwordMd5.getBytes());
            oldClient.setPassword(passwordMd5);
        }
        Float balance = client.getBalance();
        oldClient.setStateValue(client.getStateValue());
        oldClient.setPhoneNo(client.getPhoneNo());
        oldClient.setMail(client.getMail());
        oldClient.setBalance(balance == null ? 0f : balance);
        oldClient.setNotes(client.getNotes());
        return clientDao.save(oldClient);
    }

    @Override
    public Client verifyClient(Client fromClient) {
        String name = fromClient.getName();
        String password = fromClient.getPassword();
        if (!StringUtils.hasText(name) || !StringUtils.hasText(password)) {
            throw new RuntimeException("客户端名称和密码不能为空！");
        }
        Client client = clientDao.findByNameAndPassword(name, password);
        if (client == null) {
            throw new RuntimeException("客户端认证失败！");
        }
        if (client.getStateValue() == 2) {
            throw new RuntimeException("客户端已经被锁定。");
        }
        return client;
    }

    @Override
    public Page<Client> listClients(String name, String phoneNo, Pageable pageable) {
        return clientDao.findAll((root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(phoneNo)) {
                predicate = builder.and(predicate, builder.like(root.get("phoneNo"), "%" + phoneNo + "%"));
            }
            query.orderBy(builder.asc(root.get("createDate")));
            return predicate;
        }, pageable);
    }

    @Override
    @Transactional
    public List<ClientBlacklist> clientQueryBlacklist(Blacklist blacklist, Client fromClient) {
        String phoneNo = blacklist.getPhoneNo();
        String qqNo = blacklist.getQqNo();
        String microNo = blacklist.getMicroNo();
        if (!StringUtils.hasText(phoneNo) && !StringUtils.hasText(qqNo) && !StringUtils.hasText(microNo)) {
            throw new RuntimeException("请至少提供一种联系方式！");
        }
        // 验证客户端信息
        Client client = verifyClient(fromClient);
        if (client.getBalance() <= 0) {
            throw new RuntimeException("客户端余额不足。");
        }
        List<Blacklist> blacklists = blacklistDao.findByPhoneNoOrQqNoOrMicroNo(phoneNo, qqNo, microNo);
        int size = blacklists.size();
        // 客户端查询总条次增加，余额减少。
        client.setTotalNum(client.getTotalNum() + size);
        client.setBalance(client.getBalance() - size * AppConfig.QUERY_PRISE_PER_BLACKLIST);
        clientDao.save(client);
        return toClientBlacklists(blacklists);
    }

    @Override
    public BlackQQGroup clientQueryBlackQQGroup(Client client) {
        verifyClient(client); // 验证客户端信息
        return blackQQGroupDao.findFirstByStateValueOrderByCreateDateAsc(0);
    }

    @Override
    @Transactional
    public ResultMessage addBlacklistsByQQGroup(Client client, String groupNo, List<Blacklist> blacklists) {
        verifyClient(client); // 验证客户端信息
        if (!StringUtils.hasText(groupNo))
            throw new RuntimeException("QQ群号不能为空！");
        if (blacklists == null)
            throw new RuntimeException("黑名单列表不能为空！");
        BlackQQGroup group = blackQQGroupDao.findFirstByGroupNo(groupNo);
        if (group == null)
            throw new RuntimeException("该群号未录入！");
        int count = 0;
        // 逐条录入，单条异常不影响其他条录入
        for (Blacklist blacklist : blacklists) {
            if (addOneBlacklistByQQGroup(blacklist, groupNo) != null)
                count++;
        }
        String message = "共 " + blacklists.size() + " 条，录入成功 " + count + " 条。";
        group.setStateValue(1);
        group.setResultMessage(message);
        blackQQGroupDao.save(group);
        return ResultMessage.newSuccess(message);
    }

    /**
     * 单条录入。
     */
    @Transactional
    private Blacklist addOneBlacklistByQQGroup(Blacklist blacklist, String groupNo) {
        try {
            // 验证并防止联系方式重复
            blacklist = blacklistService.validateAndResolve(blacklist);
            List<Blacklist> blacklists = blacklistService.findByContact(blacklist);
            if (blacklists != null && !blacklists.isEmpty()) {
                throw new RuntimeException("该联系方式的组合已存在！");
            }
            String name = blacklist.getName();
            if (name == null) {
                name = "QQ";
            } else {
                name = name.replaceAll(NAME_NOT_PATTERN, "").trim();
                if (name.length() == 0) name = "QQ";
            }
            blacklist.setName(name);
            blacklist.setCreateDate(new Date());
            blacklist.setFromInfo(groupNo);
            return blacklistDao.save(blacklist);
        } catch (Exception e) {
            logger.error("客户端通过QQ群：" + groupNo + " 新增黑名单失败：" + e.getMessage());
            return null;
        }
    }

}
