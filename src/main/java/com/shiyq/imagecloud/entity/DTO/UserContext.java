package com.shiyq.imagecloud.entity.DTO;

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

