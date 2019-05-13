package com.naruto.hiyue;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.naruto.hiyue.been.UserInfo;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2019/4/27 0027
 * @Note
 */
public class MyApplication extends Application {
    private static Context context;
    private static UserInfo user;

    //外置存储根目录
    public static final String EXTERNAL_STORAGE_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    //私有文件外置存储目录(随着卸载自动删除，如果是扩展卡上的不会删除，4.4及以上无需在 Manifest 文件中或者动态申请外部存储空间的文件读写权限。/storage/emulated/0/Android/data/[包名]/files)
    public static String EXTERNAL_PRIVATE_STORAGE_ROOT;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        EXTERNAL_PRIVATE_STORAGE_ROOT=getExternalFilesDir(null).getPath();
    }

    public static Context getContext() {
        return context;
    }

    public static UserInfo getUser() {
        return user;
    }

    public static void setUser(UserInfo user) {
        MyApplication.user = user;
    }
}
