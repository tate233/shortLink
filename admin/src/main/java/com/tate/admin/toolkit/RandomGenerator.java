package com.tate.admin.toolkit;

import java.security.SecureRandom;

/**
 * 分组ID随机生成器
 */
public  class RandomGenerator {
    private static final String CHARACTER_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 使用 SecureRandom 来生成加密强度的随机数。
     * 这是比 java.util.Random 更安全的选择，特别是在生成令牌或密码时。
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 私有构造函数，防止这个工具类被外部实例化。
     */
    private RandomGenerator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 生成一个包含数字和英文字母的6位随机数。
     *
     * @return 一个6位长度的随机字母数字字符串
     */
    public static String generateRandom() {
        // 你可以把 6 换成任意你想要的长度
        int length = 6;

        // StringBuilder 在循环中构建字符串时效率最高
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // 1. 生成一个 0 到 61 (CHARACTER_POOL.length() - 1) 之间的随机索引
            int randomIndex = RANDOM.nextInt(CHARACTER_POOL.length());

            // 2. 从字符池中获取该索引对应的字符
            char randomChar = CHARACTER_POOL.charAt(randomIndex);

            // 3. 将字符追加到 StringBuilder
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
