package com.naruto.hiyue.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.naruto.hiyue.MyApplication;
import com.naruto.hiyue.NetWorkTasks;
import com.naruto.hiyue.R;
import com.naruto.hiyue.base.EventBusActivity;
import com.naruto.hiyue.been.UserInfo;
import com.naruto.hiyue.eventBusEvent.RefreshUserIconEvent;
import com.naruto.hiyue.utils.GlideUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserInfoActivity extends EventBusActivity<RefreshUserIconEvent> {
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.tv_userName)
    TextView tv_userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        if (!MyApplication.getUser().isFull()) {
            NetWorkTasks.getUserData(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(UserInfoActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    UserInfo userInfo = new Gson().fromJson(s, UserInfo.class);
                    if (userInfo != null) {
                        userInfo.setFull(true);
                        MyApplication.setUser(userInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bindData();
                            }
                        });
                    }
                }
            });
        } else {
            bindData();
        }
    }

    private void bindData() {
        GlideUtil.showUserIcon(iv_icon);
        tv_userName.setText(MyApplication.getUser().getUserName());
    }

    @OnClick({R.id.iv_icon})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                startActivity(IconActivity.setIntent(this, IconActivity.TYPE_SHOW_ICON, null));
                break;
        }
    }


    @Override
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    protected void onMessageEvent(RefreshUserIconEvent event) {
        GlideUtil.showUserIcon(iv_icon);
    }
}
