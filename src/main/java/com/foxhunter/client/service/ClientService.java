package com.foxhunter.client.service;

import com.foxhunter.blacklist.entity.BlackQQGroup;
import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.client.entity.Client;
import com.foxhunter.common.vo.ResultMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 客户端服务类接口。
 *
 * @author Ewing
 */
public interface ClientService {
    Client addClient(Client client);

    Client updateClient(Client client);

    Client verifyClient(Client fromClient);

    Page<Client> listClients(String name, String phoneNo, Pageable pageable);

    List<ClientBlacklist> clientQueryBlacklist(Blacklist blacklist, Client fromClient);

    BlackQQGroup clientQueryBlackQQGroup(Client client);

    ResultMessage addBlacklistsByQQGroup(Client client, String groupNo, List<Blacklist> blacklists);
}
