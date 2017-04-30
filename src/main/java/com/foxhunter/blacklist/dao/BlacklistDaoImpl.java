package com.foxhunter.blacklist.dao;

import com.foxhunter.blacklist.entity.Blacklist;
import com.foxhunter.manager.entity.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单数据库访问实现类，仅实现需要手动实现的方法，所以不能显式实现接口，由SpringData负责实现接口。
 */
public class BlacklistDaoImpl {
    private Logger logger = LoggerFactory.getLogger(BlacklistDaoImpl.class);
    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * 根据录入管理员查询黑名单。
     * 完整的使用API进行分页查询的模板，实际很少需要完整实现。
     * 统计总数和查询结果的对象，由于查询目标不一致，不能共享。
     * 一般的查询不建议用这种实现方式，建议参考CustomService.listCustoms方法。
     */
    public Page<Blacklist> findByCheckedManager(Manager manager, Pageable pageable) {
        // 获取创建规则的工厂。
        CriteriaBuilder queryBuilder = entityManager.getCriteriaBuilder();
        CriteriaBuilder countBuilder = entityManager.getCriteriaBuilder();
        // 获取查询规则，查询规则不能执行查询。
        CriteriaQuery<Blacklist> criteriaQuery = queryBuilder.createQuery(Blacklist.class);
        CriteriaQuery<Long> criteriaCount = countBuilder.createQuery(Long.class);
        // 获取查询的根对象。
        Root<Blacklist> rootQuery = criteriaQuery.from(Blacklist.class);
        Root<Blacklist> rootCount = criteriaCount.from(Blacklist.class);
        // 总条件，初始构建一个空条件。
        Predicate queryPre = queryBuilder.conjunction();
        Predicate countPre = queryBuilder.conjunction();
        if (manager != null && manager.getManagerId() != null) {
            // 添加条件到总体条件。
            countPre = countBuilder.and(countPre, countBuilder.equal(rootCount.get("addManager"), manager));
            queryPre = queryBuilder.and(queryPre, queryBuilder.equal(rootQuery.get("addManager"), manager));
        }
        // 获取结果总数。
        criteriaCount.select(countBuilder.count(rootCount)).where(countPre);
        Long total = entityManager.createQuery(criteriaCount).getSingleResult();
        // 总数大于0并且这一页有数据才继续查询，否则返回空集。
        if (total > 0 && total > pageable.getOffset()) {
            criteriaQuery.select(rootQuery).where(queryPre);
            Query queryPage = entityManager.createQuery(criteriaQuery);
            queryPage.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());
            List blacklists = queryPage.getResultList();
            return new PageImpl(blacklists, pageable, total);
        } else {
            return new PageImpl(new ArrayList<>(0), pageable, total);
        }
    }

}
