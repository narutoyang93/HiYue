package com.naruto.hiyue.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.naruto.hiyue.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;

public class MyTools {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 注意：看仔细了，dip2px(x)!=dip2px(-x)
     *
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 注意：看仔细了，px2dip(x)!=px2dip(-x)
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    // 检查设备是否连接网络
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // 检查设备是否连接wifi
    public static boolean isConnectedWithWifi(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }


    /**
     * GPS是否已打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsOpen(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsOpen = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGpsOpen;
    }

    /**
     * 检查并申请权限
     *
     * @param activity
     * @param permissionsRequestCode
     * @param permissions
     * @return 是否已经授权，无需申请
     */
    public static boolean checkPermissions(Activity activity, int permissionsRequestCode, String[] permissions) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        List<String> requestPermissionsList = new ArrayList<>();
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsList.add(p);
            }
        }
        if (!requestPermissionsList.isEmpty()) {
            String[] requestPermissionsArray = requestPermissionsList.toArray(new String[requestPermissionsList.size()]);
            ActivityCompat.requestPermissions(activity, requestPermissionsArray, permissionsRequestCode);
            return false;
        } else {
            return true;
        }
    }


    /**
     * @Purpose 默认弹窗点击事件处理接口
     * @Author Naruto Yang
     * @CreateDate 2018/10/12
     * @Note
     */
    public static class DefaultClickListener implements View.OnClickListener {
        private Dialog dialog;
        private View.OnClickListener onClickListener;

        public DefaultClickListener(Dialog dialog, View.OnClickListener onClickListener) {
            this.dialog = dialog;
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View v) {
            dialog.dismiss();
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    /**
     * 上传图片
     *
     * @param maxWidth  图片最大宽度（单位：px）
     * @param maxHeight 图片最大高度
     * @param maxSize   图片最大占用空间（单位：b）
     * @param imgPath   图片文件路径
     * @param webUrl    服务端链接
     * @param paramMap  参数
     * @param callback  回调
     */
    public static void uploadImage(int maxWidth, int maxHeight, int maxSize, String imgPath, String webUrl, Map<String, String> paramMap, Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;// 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
                BitmapFactory.decodeFile(imgPath, options);
                String suffix = imgPath.substring(imgPath.lastIndexOf(".")).toLowerCase();
                int inSampleSize = 1;
                int width = options.outWidth;
                int height = options.outHeight;
                Bitmap bitmap;

                if (width > maxWidth || height > maxHeight) {//如果图片过大，先加载稍大一点的缩略图（优化内存）
/*                    width /= 2;
                    height /= 2;
                    while (width > maxWidth || height > maxHeight) {
                        inSampleSize *= 2;
                        width /= 2;
                        height /= 2;
                    }
                    ;
                    Log.d("MyTools", "--->run: inSampleSize=" + inSampleSize);
                    options.inSampleSize = inSampleSize;
                    options.inJustDecodeBounds = false;
                    Bitmap src = BitmapFactory.decodeFile(imgPath, options);

                    //尺寸压缩
                    bitmap = Bitmap.createScaledBitmap(src, maxWidth, maxHeight, false);
                    if (src != bitmap) {
                        src.recycle();
                    }*/
                    Glide.with(MyApplication.getContext()).asBitmap().load(imgPath).override(maxWidth, maxHeight).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            compressAndUpload(suffix, resource, maxSize, webUrl, paramMap, callback);
                        }
                    });
                } else {
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeFile(imgPath, options);
                    compressAndUpload(suffix, bitmap, maxSize, webUrl, paramMap, callback);
                }
            }
        }).start();
    }

    /**
     * @param suffix
     * @param bitmap
     * @param maxSize
     * @param webUrl
     * @param paramMap
     * @param callback
     */
    public static void compressAndUpload(String suffix, Bitmap bitmap, int maxSize, String webUrl, Map<String, String> paramMap, Callback callback) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int quality = 105;
        int newSize = 0;
        // 质量压缩
        do {
            stream.reset();
            bitmap.compress(Bitmap.CompressFormat.WEBP, quality -= 5, stream);
            newSize = stream.toByteArray().length;
            Log.d("MyTools", "--->run: quality=" + quality + ";size=" + newSize);
        } while (newSize > maxSize && quality > 0);

        bitmap.recycle();
        // Base64图片转码为String
        String encodedString = Base64.encodeToString(stream.toByteArray(), 0);
        paramMap.put("image", encodedString);
        paramMap.put("suffix", suffix);
        HttpUtil.requestByPost(webUrl, paramMap, callback);
    }


    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

}
