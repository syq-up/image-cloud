package com.shiyq.cloudsystem.entity.DTO;

/**
 * 当前请求的用户上下文信息
 * 请求开始时创建，结束时销毁
 */
public final class UserContext {
    private static final ThreadLocal<Integer> user = new ThreadLocal<>();

    public static void add(String userId) {
        user.set(Integer.valueOf(userId));
    }

    public static void remove() {
        user.remove();
    }

    /**
     * @return 当前登录用户的用户ID
     */
    public static Integer getCurrentUserId() {
        return user.get();
    }
}

