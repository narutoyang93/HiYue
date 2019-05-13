package com.naruto.hiyue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.naruto.hiyue.MyApplication;
import com.naruto.hiyue.NetWorkTasks;
import com.naruto.hiyue.R;
import com.naruto.hiyue.base.BaseActivity;
import com.naruto.hiyue.been.UserInfo;
import com.naruto.hiyue.utils.HttpUtil;

import java.io.IOException;

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
/*        UserInfo user = new UserInfo();
        user.setUserId("13751360141");
        user.setIcon("1.jpg");
        MyApplication.setUser(user);
        startActivity(new Intent(this, MainActivity.class));
        finish();*/
        HttpUtil.uploadFile(NetWorkTasks.getActionUrl("uploadFile"), "/storage/emulated/0/00Test/456.zip", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, new Pair<String, String>("folder", "userIcon"));
    }
}