package com.naruto.hiyue.base;

import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naruto.hiyue.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2019/4/27 0027
 * @Note
 */
public abstract class MainFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();
    }

    protected void setAdapter(RecyclerView.Adapter adapter){
        recyclerView.setAdapter(adapter);
    };

    protected abstract void getData();
}
