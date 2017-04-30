package com.foxhunter.client.web;

import com.foxhunter.blacklist.entity.BlackQQGroup;
import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.client.entity.Client;
import com.foxhunter.client.service.ClientBlacklist;
import com.foxhunter.client.service.ClientService;
import com.foxhunter.common.interceptor.AuthLogin;
import com.foxhunter.common.util.JSonUtils;
import com.foxhunter.common.vo.EasyUIGridData;
import com.foxhunter.common.vo.ResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 客户端控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping("/client")
public class ClientController {
    private Logger logger = LoggerFactory.getLogger(ClientController.class);

    private ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }


    /**
     * 新增客户端。
     */
    @ResponseBody
    @RequestMapping("/add")
    @AuthLogin(needManager = true)
    public ResultMessage addClient(Client client) {
        try {
            clientService.addClient(client);
            return ResultMessage.newSuccess("新增客户端成功！");
        } catch (Exception e) {
            logger.error("新增客户端失败：" + e.getMessage());
            return ResultMessage.newFailure("新增客户端失败：" + e.getMessage());
        }
    }

    /**
     * 更新客户端。
     */
    @ResponseBody
    @RequestMapping("/update")
    @AuthLogin(needManager = true)
    public ResultMessage updateClient(Client client) {
        try {
            clientService.updateClient(client);
            return ResultMessage.newSuccess("修改客户端成功！");
        } catch (Exception e) {
            logger.error("修改客户端异常：" + e.getMessage());
            return ResultMessage.newFailure("修改客户端异常：" + e.getMessage());
        }
    }

    /**
     * 查询客户端。
     */
    @ResponseBody
    @RequestMapping("/list")
    @AuthLogin(needManager = true)
    public EasyUIGridData listClients(int page, int rows, String name, String phoneNo) {
        try {
            Pageable pageable = new PageRequest(page - 1, rows);
            Page dataPage = clientService.listClients(name, phoneNo, pageable);
            return new EasyUIGridData(dataPage);
        } catch (Exception e) {
            return new EasyUIGridData("name", "查询数据异常！");
        }
    }

    @ResponseBody
    @RequestMapping("/findBlacklist")
    public ResultMessage queryBlacklist(Blacklist blacklist, Client fromClient) {
        try {
            List<ClientBlacklist> blacklists = clientService.clientQueryBlacklist(blacklist, fromClient);
            return ResultMessage.newSuccess().setData(blacklists);
        } catch (Exception e) {
            logger.error("客户端查询黑名单异常：" + e.getMessage()
                    + "\n查询参数：" + JSonUtils.toJSon(blacklist)
                    + "\n认证参数：" + JSonUtils.toJSon(fromClient));
            return ResultMessage.newFailure("客户端查询黑名单失败！");
        }
    }

    @ResponseBody
    @RequestMapping("/findBlackQQGroup")
    public ResultMessage queryBlacklist(Client client) {
        try {
            BlackQQGroup group = clientService.clientQueryBlackQQGroup(client);
            if (group == null)
                return ResultMessage.newFailure("没有未爬取的QQ群。");
            else
                return ResultMessage.newSuccess().setData(group);
        } catch (Exception e) {
            logger.error("客户端查询黑名QQ群异常：" + e.getMessage()
                    + "\n认证参数：" + JSonUtils.toJSon(client));
            return ResultMessage.newFailure("客户端查询黑名QQ群异常：" + e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping("/addBlacklistsByQQGroup")
    public ResultMessage addBlacklistsByGroup(@RequestBody ClientGroupBlacklists cgbs) {
        try {
            Client client = new Client();
            client.setName(cgbs.getName());
            client.setPassword(cgbs.getPassword());
            return clientService.addBlacklistsByQQGroup(client, cgbs.getGroupNo(), cgbs.getBlacklists());
        } catch (Exception e) {
            logger.error("客户端通过QQ群新增黑名单异常：" + e.getMessage()
                    + "\n传入参数：" + JSonUtils.toJSon(cgbs));
            return ResultMessage.newFailure("客户端通过QQ群新增黑名单异常：" + e.getMessage());
        }
    }

}
