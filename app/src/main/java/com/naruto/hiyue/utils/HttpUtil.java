package com.naruto.hiyue.utils;

import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2019/4/27 0027
 * @Note
 */
public class HttpUtil {
    private HttpUtil() {
    }

    /**
     * get请求
     *
     * @param url
     * @param paramMap
     * @param callback
     */
    public static void requestByGet(String url, Map<String, String> paramMap, Callback callback) {
        GetParamAdder adder = new GetParamAdder(url);
        fetchParamFromMap(paramMap, adder);
        execute(adder.t, null, callback);
    }

    public static void requestByGet(String url, String[] keyArray, String[] valueArray, Callback callback) {
        GetParamAdder adder = new GetParamAdder(url);
        fetchParamFromArray(keyArray, valueArray, adder);
        execute(adder.t, null, callback);
    }

    public static void requestByGet(String url, Callback callback, Pair<String, String>... params) {
        GetParamAdder adder = new GetParamAdder(url);
        fetchParamFromPairArray(params, adder);
        execute(adder.t, null, callback);
    }

    /**
     * post请求
     *
     * @param url
     * @param paramMap
     * @param callback
     */
    public static void requestByPost(String url, Map<String, String> paramMap, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        PostParamAdder adder = new PostParamAdder(builder);
        fetchParamFromMap(paramMap, adder);
        execute(url, builder.build(), callback);
    }

    public static void requestByPost(String url, String[] keyArray, String[] valueArray, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        PostParamAdder adder = new PostParamAdder(builder);
        fetchParamFromArray(keyArray, valueArray, adder);
        execute(url, builder.build(), callback);
    }

    public static void requestByPost(String url, Callback callback, Pair<String, String>... params) {
        FormBody.Builder builder = new FormBody.Builder();
        PostParamAdder adder = new PostParamAdder(builder);
        fetchParamFromPairArray(params, adder);
        execute(url, builder.build(), callback);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param filePath
     * @param callback
     * @param params
     */
    public static void uploadFile(String url, String filePath, Callback callback, Pair<String, String>... params) {
        File file = new File(filePath);
        if (!(file.exists() || file.isFile())) {
            Log.e("HttpUtil", "--->uploadFile: file not found!");
            return;
        }
        uploadFile(url, filePath, RequestBody.create(getMediaType(filePath), new File(filePath)), callback, params);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param input
     * @param fileName
     * @param callback
     * @param params
     */
    public static void uploadFile(String url, byte[] input, String fileName, Callback callback, Pair<String, String>... params) {
        uploadFile(url, fileName, RequestBody.create(getMediaType(fileName), input), callback, params);
    }


    public static void uploadFile(String url, String fileName, RequestBody body, Callback callback, Pair<String, String>... params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName.substring(fileName.lastIndexOf("/") + 1), body);
        if (params != null) {
            for (Pair<String, String> p : params) {
                builder.addFormDataPart(p.first, p.second);
            }
        }
        execute(url, builder.build(), callback);
    }

    private static MediaType getMediaType(String fileName) {
        String mediaType = "";
        switch (fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase()) {
            case "jpg":
                mediaType = "image/jpg";
                break;
            case "png":
                mediaType = "image/png";
                break;
            default:
                mediaType = "multipart/form-data";
                break;
        }
        return MediaType.parse(mediaType);
    }


    /**
     *
     */
    private static class GetParamAdder extends ParamAdder<String> {

        public GetParamAdder(String s) {
            super(s);
        }

        @Override
        void addParam(String key, String value) {
            t += ("&" + key + "=" + value);
        }
    }

    /**
     *
     */
    private static class PostParamAdder extends ParamAdder<FormBody.Builder> {

        public PostParamAdder(FormBody.Builder builder) {
            super(builder);
        }

        @Override
        void addParam(String key, String value) {
            t.add(key, value);
        }
    }

    private static void fetchParamFromMap(Map<String, String> paramMap, ParamAdder adder) {
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            adder.addParam(entry.getKey(), entry.getValue());
        }
    }

    private static void fetchParamFromArray(String[] keyArray, String[] valueArray, ParamAdder adder) {
        for (int i = 0; i < keyArray.length; i++) {
            adder.addParam(keyArray[i], valueArray[i]);
        }
    }

    private static void fetchParamFromPairArray(Pair<String, String>[] pairArray, ParamAdder adder) {
        for (Pair<String, String> p : pairArray) {
            adder.addParam(p.first, p.second);
        }
    }

    /**
     * 执行
     *
     * @param url
     * @param requestBody
     * @return
     */
    private static void execute(String url, RequestBody requestBody, Callback callback) {
        Log.d("HttpUtil", "--->uploadFile: url=" + url);
        Request.Builder builder = new Request.Builder().url(url);
        if (requestBody != null) {
            builder.post(requestBody);
        }
        Singleton.client.newCall(builder.build()).enqueue(callback);
    }

    /**
     * 静态内部类实现线程安全单例模式
     */
    private static class Singleton {
        static OkHttpClient client = new OkHttpClient();
    }


    private abstract static class ParamAdder<T> {
        T t;

        public ParamAdder(T t) {
            this.t = t;
        }

        abstract void addParam(String key, String value);
    }
}
