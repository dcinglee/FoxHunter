package com.foxhunter.test;

import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.client.entity.Client;
import com.foxhunter.client.service.ClientBlacklist;
import com.foxhunter.client.service.ClientService;
import com.foxhunter.common.util.JSonUtils;
import com.foxhunter.common.vo.ResultMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * 客户端模块测试类。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestClient {
    private Logger logger = LoggerFactory.getLogger(TestClient.class);

    private ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Test
    public void clientQueryBlacklist() {
        Blacklist blacklist = new Blacklist();
        blacklist.setPhoneNo("1");
        Client client = new Client();
        client.setName("123");
        client.setPassword(DigestUtils.md5DigestAsHex("123123".getBytes()));
        try {
            List<ClientBlacklist> blacklists = clientService.clientQueryBlacklist(blacklist, client);
            logger.info("=====>" + JSonUtils.toJSon(ResultMessage.newSuccess().setData(blacklists)));
        } catch (Exception e) {
            logger.error("客户端查询黑名单异常：" + e.getMessage()
                    + "\n查询参数：" + JSonUtils.toJSon(blacklist)
                    + "\n认证参数：" + JSonUtils.toJSon(client));
        }
    }

}
