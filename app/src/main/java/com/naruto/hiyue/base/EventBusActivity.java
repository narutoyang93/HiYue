package com.naruto.hiyue.base;

import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2019/5/2 0002
 * @Note
 */
public abstract class EventBusActivity<T> extends BaseActivity {
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    protected abstract void onMessageEvent(T event);
}
