package com.foxhunter.business.dao;

import com.foxhunter.business.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 字典数据库访问类。
 *
 * @author Ewing
 */
public interface DictionaryDao extends JpaRepository<Dictionary, String> {
    List<Dictionary> findByTypeOrderByValue(String type);

    Dictionary findByValueAndType(Integer value, String type);
}
