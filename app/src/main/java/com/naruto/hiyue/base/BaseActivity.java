package com.naruto.hiyue.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.naruto.hiyue.R;
import com.naruto.hiyue.utils.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (toolbar != null) {//设置标题栏
            setSupportActionBar(toolbar);
            setToolBarTitle(getTitle().toString());
            getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示默认title
            setToolbarLeftButton(toolbar.findViewById(R.id.iv_toolbar_leftButton));
        }
        ViewUtil.initAfterSetContentView(this, toolbar);
    }

    protected void setToolBarTitle(String title) {
        ((TextView) toolbar.findViewById(R.id.tv_toolbar_title)).setText(title);
    }

    protected void setToolbarLeftButton(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
