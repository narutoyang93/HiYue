package com.naruto.hiyue;

import android.util.Pair;

import com.naruto.hiyue.utils.HttpUtil;

import okhttp3.Callback;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2019/4/27 0027
 * @Note
 */
public class NetWorkTasks {
    private NetWorkTasks() {
    }

    private static final String[] ips = {"192.168.155.1", "192.168.1.100"};//第一个是开启wifi局域网给真机测试时的ip，第二个是不开启wifi局域网直接用模拟器测试时的ip
    private static final String IP = ips[1];
    private static final String WEB_SITE_URL = "http://" + IP + ":8080/HiYueWeb";//     http://192.168.155.1:8080/HiYueWeb/appAction?t=

    public static String getActionUrl(String type) {
        return WEB_SITE_URL + "/appAction?t=" + type;
    }

    public static String getImgUrl(String imgName) {
        return WEB_SITE_URL + "/image/userIcon/" + imgName;
    }

    /**
     * 登录
     *
     * @param userId
     * @param password
     * @param callBack
     */
    public static void login(String userId, String password, Callback callBack) {
        HttpUtil.requestByPost(getActionUrl("login"), callBack, new Pair<>("userId", userId), new Pair<>("password", password));
    }

    /**
     * 获取邀约广场列表数据
     *
     * @param filtrate
     * @param pageNo
     * @param callBack
     */
    public static void getDatingData(String filtrate, int pageNo, Callback callBack) {
        String[] key = new String[]{"u", "filtrate", "pageNo"};
        String[] value = new String[]{MyApplication.getUser().getUserId(), filtrate, String.valueOf(pageNo)};
        HttpUtil.requestByGet(getActionUrl("4"), key, value, callBack);
    }

    /**
     * 获取用户个人信息
     *
     * @param callBack
     */
    public static void getUserData(Callback callBack) {
        String[] key = new String[]{"u"};
        String[] value = new String[]{MyApplication.getUser().getUserId()};
        HttpUtil.requestByGet(getActionUrl("3"), key, value, callBack);
    }

}
