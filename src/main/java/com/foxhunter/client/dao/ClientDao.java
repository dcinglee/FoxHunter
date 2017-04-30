/**
 *
 */
package com.foxhunter.client.dao;

import com.foxhunter.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 客户端数据层访问接口
 * 使用springData JPA提供的方法
 *
 * @author lijing
 * @2017年1月3日下午3:13:02
 */
public interface ClientDao extends JpaRepository<Client, String>, JpaSpecificationExecutor<Client> {

    Client findFirstByName(String name);

    Client findByNameAndPassword(String clientId, String password);
}
