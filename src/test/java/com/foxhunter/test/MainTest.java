package com.foxhunter.test;

import org.springframework.util.DigestUtils;

/**
 * 该类临时测试用，用完还原，请勿提交！
 */
public class MainTest {
    /**
     * 该方法临时测试用，用完还原，请勿提交！
     */
    public static void main(String[] args) throws Exception {
        String str = "1234567890";
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        System.out.println(md5);
    }
}
