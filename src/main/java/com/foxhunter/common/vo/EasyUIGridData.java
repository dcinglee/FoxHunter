package com.foxhunter.common.vo;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EasyUI Grid专用数据模型。
 *
 * @author Ewing
 */
public class EasyUIGridData {
    private long total = 0;
    private List rows;

    public EasyUIGridData() {
    }

    public EasyUIGridData(List rows) {
        this.total = rows.size();
        this.rows = rows;
    }

    public EasyUIGridData(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public EasyUIGridData(Page page) {
        this.setTotal(page.getTotalElements());
        this.setRows(page.getContent());
    }

    public EasyUIGridData(String field, String message) {
        this.rows = new ArrayList<>(1);
        Map<String, String> map = new HashMap<>(1);
        map.put(field, message);
        this.rows.add(map);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

}
