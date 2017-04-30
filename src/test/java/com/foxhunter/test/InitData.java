package com.foxhunter.test;

import com.foxhunter.business.dao.DictionaryDao;
import com.foxhunter.business.entity.Dictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 初始化数据。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:springDataJpa.xml"})
public class InitData {
    private DictionaryDao dictionaryDao;

    @Autowired
    public void setDictionaryDao(DictionaryDao dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
    }

    /**
     * 插入数据到字典表，已存在则跳过。
     * 该方法不能使用@Test测试，否则事务失效。
     */
    @Transactional
    private void insertDictionary(int[] values, String[] names, String type) {
        for (int i = 0; i < values.length; i++) {
            Dictionary dictionary = new Dictionary(values[i], names[i], type);
            if (dictionaryDao.findByValueAndType(values[i], type) == null) {
                dictionaryDao.save(dictionary);
            }
        }
    }

    @Test
    public void initPublicDict() {
        int[] values = {0, 1, 2};
        String[] names = {"保密", "男", "女"};
        insertDictionary(values, names, "gender");
        int[] graduteValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        String[] gradutes = {"小学", "初中", "职高", "中专", "高中", "大专", "本科", "本科学士", "研究生", "研究生硕士", "研究生博士"};
        insertDictionary(graduteValues, gradutes, "gradute");
    }

    @Test
    public void initManagerDict() {
        int[] values = {1, 2};
        String[] names = {"超级管理员", "普通管理员"};
        insertDictionary(values, names, "managerType");
    }

    @Test
    public void initBlacklistDict() {
        int[] values1 = {1, 2, 3};
        String[] names1 = {"一般", "危险", "高危"};
        insertDictionary(values1, names1, "blacklistLevel");
        int[] values2 = {1, 2, 3};
        String[] names2 = {"待确认", "个人", "团伙"};
        insertDictionary(values2, names2, "blacklistOrgType");
        int[] values3 = {100, 101, 102, 103, 104, 105, 106,
                200, 201, 202, 203, 204, 205, 206, 207, 208,
                300, 301, 302, 303,
                400, 401, 402, 403, 404};
        String[] names3 = {"地推", "地推-商业区", "地推-休闲区", "地推-交通线", "地推-住宅区", "地推-办公区", "地推-生产区",
                "网络", "网络-QQ群", "网络-微信群", "网络-QQ空间", "网络-朋友圈", "网络-贴吧", "网络-论坛", "网络-知道", "网络-广告",
                "推算", "推算-智能算法", "推算-直接关联", "推算-人工推测",
                "举报", "举报-广告推送", "举报-宣传营销", "举报-诈骗诱骗", "举报-信息搔扰"};
        insertDictionary(values3, names3, "blacklistFromType");
        int[] values4 = {1, 2, 3};
        String[] names4 = {"灰名", "黑名未验证", "黑名已验证"};
        insertDictionary(values4, names4, "blacklistState");
    }

    @Test
    public void initBillDict() {
        int[] values = {1, 2, 3};
        String[] names = {"提现中", "提现成功", "提现失败"};
        insertDictionary(values, names, "billState");
        int[] values2 = {1, 2, 3};
        String[] names2 = {"人民币", "美元", "英镑"};
        insertDictionary(values2, names2, "billCurrencyType");
    }

    @Test
    public void initCustomDict() {
        int[] values = {1, 2};
        String[] names = {"正常", "锁定"};
        insertDictionary(values, names, "customState");
    }

    @Test
    public void initPhotoDict() {
        int[] values = {1, 2, 3, 4, 5};
        String[] names = {"待审核", "审核通过", "审核未通过", "管理员新建", "管理员已录入"};
        insertDictionary(values, names, "photoCheckState");
    }

    @Test
    public void initClientDict() {
        int[] values = {1, 2};
        String[] names = {"正常", "锁定"};
        insertDictionary(values, names, "clientState");
    }

}
