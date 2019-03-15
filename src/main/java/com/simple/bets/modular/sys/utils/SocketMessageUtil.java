package com.simple.bets.modular.sys.utils;

import com.alibaba.fastjson.JSON;

/**
 * @Auther: ymfa
 * @Date: 2019/3/13 17:27
 * @Description:
 */
public class SocketMessageUtil {

    public static final String ENTER = "ENTER";
    public static final String SPEAK = "SPEAK";
    public static final String QUIT = "QUIT";

    private String type;//消息类型

    private String sessionId; //用户id

    private String userImg; //用户头像

    private String username; //发送人

    private String msg; //发送消息

    private int onlineCount; //在线用户数

    public SocketMessageUtil(String type, String sessionId, String userImg, String username, String msg, int onlineCount) {
        this.type = type;
        this.sessionId = sessionId;
        this.userImg = userImg;
        this.username = username;
        this.msg = msg;
        this.onlineCount = onlineCount;
    }

    public SocketMessageUtil(String type, String username, String msg, int onlineCount) {
        this.type = type;
        this.username = username;
        this.msg = msg;
        this.onlineCount = onlineCount;
    }

    public SocketMessageUtil() {
    }

    public static String jsonStr(String type, String username, String msg, int onlineTotal) {
        return JSON.toJSONString(new SocketMessageUtil(type, username, msg, onlineTotal));
    }

    public static String jsonStr(String type, String sessionId, String userImg, String username, String msg, int onlineCount) {
        return JSON.toJSONString(new SocketMessageUtil(type, sessionId, userImg, username,msg,onlineCount));
    }

    public static String getENTER() {
        return ENTER;
    }

    public static String getSPEAK() {
        return SPEAK;
    }

    public static String getQUIT() {
        return QUIT;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
