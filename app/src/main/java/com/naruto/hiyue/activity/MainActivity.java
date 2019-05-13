package com.naruto.hiyue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.naruto.hiyue.MyApplication;
import com.naruto.hiyue.NetWorkTasks;
import com.naruto.hiyue.R;
import com.naruto.hiyue.adapter.MainActivityAdapter;
import com.naruto.hiyue.base.EventBusActivity;
import com.naruto.hiyue.been.UserInfo;
import com.naruto.hiyue.eventBusEvent.RefreshUserIconEvent;
import com.naruto.hiyue.fragment.DatingFragment;
import com.naruto.hiyue.fragment.TestFragment;
import com.naruto.hiyue.utils.GlideUtil;
import com.naruto.hiyue.utils.MyTools;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends EventBusActivity<RefreshUserIconEvent> {
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.tv_userName)
    TextView tv_userName;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    private UserInfo userInfo;
    private final String[] titleArray = new String[]{"约会", "消息", "好友", "发现"};
    private SparseArray<TextView> tabTextViewMap = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userInfo = MyApplication.getUser();
        //设置抽屉菜单
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                drawerLayout.getChildAt(0).setX(slideOffset * drawerView.getWidth());
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        GlideUtil.showUserIcon(iv_icon);
        tv_userName.setText(userInfo.getUserName());
        MyTools.setSexIcon(userInfo.getSex(), iv_sex);

        //设置viewPager
        List<Fragment> fragmentList = new ArrayList<>();
        int[] iconArray = new int[]{R.drawable.selector_tab_date, R.drawable.selector_tab_message, R.drawable.selector_tab_friends, R.drawable.selector_tab_find};
        fragmentList.add(new DatingFragment());
        for (int i = 2; i < 4; i++) {
            fragmentList.add(TestFragment.newInstance(titleArray[i]));
        }
        viewPager.setAdapter(new MainActivityAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int oldPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTabTextColor(position, true);
                changeTabTextColor(oldPosition, false);
                setToolBarTitle(titleArray[position]);
                oldPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.removeAllTabs();
        for (int i = 0; i < titleArray.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(R.layout.layout_tab);
            View view = tab.getCustomView();
            ((ImageView) view.findViewById(R.id.iv_icon)).setImageResource(iconArray[i]);
            TextView textView = view.findViewById(R.id.tv_title);
            textView.setText(titleArray[i]);
            tabTextViewMap.put(i, textView);
      /*      tab.setText(titleArray[i]);
            tab.setIcon(iconArray[i]);*/
            tabLayout.addTab(tab);
        }
        changeTabTextColor(0, false);
    }


    private void changeTabTextColor(int position, boolean isSelected) {
        tabTextViewMap.get(position).setTextColor(getResources().getColor(isSelected ? R.color.colorTheme : R.color.colorGray));
    }

    @Override
    protected void setToolbarLeftButton(ImageView imageView) {
        Glide.with(this).load(NetWorkTasks.getImgUrl(MyApplication.getUser().getIcon())).circleCrop().into(imageView);
    }

    @OnClick({R.id.iv_toolbar_leftButton, R.id.iv_icon})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_leftButton:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.iv_icon:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
        }
    }

    @Override
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    protected void onMessageEvent(RefreshUserIconEvent event) {
        GlideUtil.showUserIcon(findViewById(R.id.iv_toolbar_leftButton));
        GlideUtil.showUserIcon(iv_icon);
    }
}
