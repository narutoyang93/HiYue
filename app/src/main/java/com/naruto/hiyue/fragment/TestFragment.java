package com.naruto.hiyue.fragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naruto.hiyue.R;
import com.naruto.hiyue.base.BaseFragment;
import com.naruto.hiyue.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public TestFragment() {
        super(R.layout.fragment_main);
    }

    public static TestFragment newInstance(String param1) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    protected void initView() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(mParam1 + i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new BaseRecyclerViewAdapter<String>(dataList) {

            @Override
            protected int getLayoutRes(int viewType) {
                return R.layout.layout_item_test;
            }

            @Override
            protected void setView(VH holder, String data, int position) {
                TextView textView = holder.getView(R.id.tv_text);
                textView.setText(data);
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
