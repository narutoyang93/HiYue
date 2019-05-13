package com.naruto.hiyue.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.naruto.hiyue.MyApplication;
import com.naruto.hiyue.NetWorkTasks;
import com.naruto.hiyue.R;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2019/5/1 0001
 * @Note
 */
public class GlideUtil {
    public static void showIcon(String url,ImageView imageView){
        Glide.with(MyApplication.getContext()).load(url).placeholder(R.mipmap.ic_launcher).circleCrop().into(imageView);
    }

    public static void showUserIcon(ImageView imageView){
        showIcon(NetWorkTasks.getImgUrl(MyApplication.getUser().getIcon()),imageView);
    }
}
