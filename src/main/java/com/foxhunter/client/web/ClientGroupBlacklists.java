package com.foxhunter.client.web;

import com.foxhunter.blacklist.entity.Blacklist;

import java.util.List;

/**
 * 客户端新增黑名单参数。
 */
public class ClientGroupBlacklists {
    private String name;
    private String password;
    private String groupNo;
    private List<Blacklist> blacklists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public List<Blacklist> getBlacklists() {
        return blacklists;
    }

    public void setBlacklists(List<Blacklist> blacklists) {
        this.blacklists = blacklists;
    }
}
