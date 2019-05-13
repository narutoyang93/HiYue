package com.naruto.hiyue.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.naruto.hiyue.MyApplication;
import com.naruto.hiyue.NetWorkTasks;
import com.naruto.hiyue.R;
import com.naruto.hiyue.base.EventBusActivity;
import com.naruto.hiyue.eventBusEvent.RefreshUserIconEvent;
import com.naruto.hiyue.utils.DialogUtils;
import com.naruto.hiyue.utils.MyTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class IconActivity extends EventBusActivity<RefreshUserIconEvent> {
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.button)
    Button button;
    private static int RESULT_LOAD_IMG = 1;
    private String imgPath;
    private static final String INTENT_KEY_TYPE = "type";
    private static final String INTENT_KEY_IMG_PATH = "imgPath";
    public static final int TYPE_SHOW_ICON = 0;//查看头像
    public static final int TYPE_UPLOAD_ICON = 1;//上传头像
    private int type;
    private Intent intent;
    private static final int PERMISSION_CODE_READ_EXTERNAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);
        intent = getIntent();
        type = intent.getIntExtra(INTENT_KEY_TYPE, TYPE_SHOW_ICON);
        setView();
    }

    public static Intent setIntent(Activity activity, int type, String imgPath) {
        Intent intent = new Intent(activity, IconActivity.class);
        intent.putExtra(INTENT_KEY_TYPE, type);
        intent.putExtra(INTENT_KEY_IMG_PATH, imgPath);
        return intent;
    }

    private void setView() {
        switch (type) {
            case TYPE_SHOW_ICON:
                imgPath = NetWorkTasks.getImgUrl(MyApplication.getUser().getIcon());
                break;
            case TYPE_UPLOAD_ICON:
                setToolBarTitle("上传头像");
                button.setText("使用");
                imgPath = intent.getStringExtra(INTENT_KEY_IMG_PATH);
                break;
        }
        Glide.with(this).load(imgPath).into(iv_icon);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case TYPE_SHOW_ICON://选择图片
                        boolean b = MyTools.checkPermissions(IconActivity.this, PERMISSION_CODE_READ_EXTERNAL, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                        if (b) {
                            jumpToSelectImage();
                        }
                        break;
                    case TYPE_UPLOAD_ICON://上传
                        if (new File(imgPath).exists()) {
                            DialogUtils.showProgressMaskLayer(IconActivity.this);
                            MyTools.uploadImage(960, 960, 200 * 1024, imgPath, NetWorkTasks.getActionUrl("uploadFile"), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DialogUtils.dismissProgressDialog(IconActivity.this);
                                            Toast.makeText(IconActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    DialogUtils.dismissProgressDialog(IconActivity.this);
                                    String jsonString = response.body().string();
                                    if (!jsonString.isEmpty()) {
                                        String fileName = null;
                                        try {
                                            Map<String, String> map = new Gson().fromJson(jsonString, new TypeToken<Map<String, String>>() {
                                            }.getType());
                                            fileName = map.get("fileName");
                                        } catch (JsonSyntaxException e) {
                                            e.printStackTrace();
                                        }
                                        if (fileName != null && !fileName.isEmpty()) {
                                            MyApplication.getUser().setIcon(fileName);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(IconActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                                    EventBus.getDefault().postSticky(new RefreshUserIconEvent());
                                                    finish();
                                                }
                                            });
                                        }
                                    }
                                }
                            }, new Pair<>("folder", "userIcon"), new Pair<>("userId", MyApplication.getUser().getUserId()));
                        }
                        break;
                }
            }
        });
    }

    private void jumpToSelectImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // 获取游标
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);
            cursor.close();
            startActivity(setIntent(this, TYPE_UPLOAD_ICON, path));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE_READ_EXTERNAL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                jumpToSelectImage();
            }
        }
    }

    @Override
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    protected void onMessageEvent(RefreshUserIconEvent event) {
        if (type == TYPE_SHOW_ICON) {
            imgPath = NetWorkTasks.getImgUrl(MyApplication.getUser().getIcon());
            Glide.with(this).load(imgPath).into(iv_icon);
        }
    }
}
