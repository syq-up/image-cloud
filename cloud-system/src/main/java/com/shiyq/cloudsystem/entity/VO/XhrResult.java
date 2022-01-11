package com.shiyq.cloudsystem.entity.VO;

import com.shiyq.cloudsystem.constant.HttpStatus;

import java.util.HashMap;

/**
 * 请求返回结果
 */
public class XhrResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";
    /** 返回内容 */
    public static final String MSG_TAG = "msg";
    /** 数据对象 */
    public static final String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 ResultDTO 对象，使其表示一个空消息。
     */
    public XhrResult() {
    }

    /**
     * 初始化一个新创建的 ResultDTO 对象
     * @param code 状态码
     * @param msg 返回内容
     */
    public XhrResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 ResultDTO 对象
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public XhrResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     * @return 成功消息
     */
    public static XhrResult success() {
        return XhrResult.success("操作成功");
    }

    /**
     * 返回成功数据
     * @return 成功消息
     */
    public static XhrResult success(Object data) {
        return XhrResult.success("操作成功", data);
    }

    /**
     * 返回成功消息
     * @param msg 返回内容
     * @return 成功消息
     */
    public static XhrResult success(String msg) {
        return XhrResult.success(msg, null);
    }

    /**
     * 返回成功消息
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static XhrResult success(String msg, Object data) {
        return new XhrResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     * @return 警告消息
     */
    public static XhrResult error() {
        return XhrResult.error("操作失败");
    }

    /**
     * 返回错误消息
     * @param msg 返回内容
     * @return 警告消息
     */
    public static XhrResult error(String msg) {
        return XhrResult.error(msg, null);
    }

    /**
     * 返回错误消息
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static XhrResult error(String msg, Object data) {
        return new XhrResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static XhrResult error(int code, String msg) {
        return new XhrResult(code, msg, null);
    }
}
