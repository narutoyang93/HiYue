package com.naruto.hiyue.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.naruto.hiyue.MyApplication;
import com.naruto.hiyue.NetWorkTasks;
import com.naruto.hiyue.R;
import com.naruto.hiyue.base.BaseActivity;
import com.naruto.hiyue.been.UserInfo;
import com.naruto.hiyue.utils.DialogUtils;
import com.naruto.hiyue.utils.HttpUtil;

import java.io.IOException;
import java.util.Map;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void setToolbarLeftButton(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }

    @OnClick({R.id.bt_login})
    public void onViewClick(View view) {
        doLogin(this, "13751360141", "1");
    }

    public static void doLogin(Activity activity, String userId, String password) {
        NetWorkTasks.login(userId, password, new HttpUtil.MyCallBack(activity) {
            @Override
            public void onResponse(String jsonString) {
                if (jsonString != null && !jsonString.isEmpty()) {
                    Map<String, String> map = new Gson().fromJson(jsonString, new TypeToken<Map<String, String>>() {
                    }.getType());
                    if (map != null) {
                        String resultCode = map.get("resultCode");
                        if (resultCode != null) {
                            if (resultCode.equals("1")) {
                                UserInfo user = new UserInfo();
                                user.setUserId(userId);
                                user.setUserName(map.get("userName"));
                                user.setIcon(map.get("icon"));
                                user.setSex(map.get("sex"));
                                MyApplication.setUser(user);
                                if (activity instanceof LoginActivity) {
                                    //写入本地配置文件
                                    SharedPreferences.Editor editor = MyApplication.getSharedPreferences().edit();
                                    editor.putString("userId", userId);
                                    editor.putString("password", password);
                                    editor.commit();// 提交修改
                                }
                                activity.startActivity(new Intent(activity, MainActivity.class));
                                activity.finish();
                            } else {
                                if (activity instanceof LoginActivity) {
                                    String message = map.get("message");
                                    message = (message == null || message.isEmpty()) ? "登录异常，请重试" : message;
                                    DialogUtils.showDialog(activity, true, null, message, true, "确定", null, null, null);
                                } else {
                                    activity.startActivity(new Intent(activity, LoginActivity.class));
                                    activity.finish();
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}