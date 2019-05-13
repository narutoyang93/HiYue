package com.naruto.hiyue.fragment;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.naruto.hiyue.NetWorkTasks;
import com.naruto.hiyue.R;
import com.naruto.hiyue.base.BaseRecyclerViewAdapter;
import com.naruto.hiyue.base.MainFragment;
import com.naruto.hiyue.been.DatingInfo;
import com.naruto.hiyue.utils.DialogUtils;
import com.naruto.hiyue.utils.MyTools;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2019/4/27 0027
 * @Note
 */
public class DatingFragment extends MainFragment {

    @Override
    protected void initView() {
        super.initView();
    }


    @Override
    protected void getData() {
        DialogUtils.showProgressMaskLayer(getActivity());
        NetWorkTasks.getDatingData("", 1, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<DatingInfo> dataList = new Gson().fromJson(s, new TypeToken<List<DatingInfo>>() {
                        }.getType());
                        setAdapter(new BaseRecyclerViewAdapter<DatingInfo>(dataList) {

                            @Override
                            protected int getLayoutRes(int viewType) {
                                return R.layout.item_dating;
                            }

                            @Override
                            protected void setView(VH holder, DatingInfo data, int position) {
                                holder.setText(R.id.tv_place, data.getPlace());
                                holder.setText(R.id.tv_address, data.getAddress());
                                holder.setText(R.id.tv_activity, data.getActivity());
                                holder.setText(R.id.tv_date, data.getDatingDate());
                                holder.setText(R.id.tv_time, data.getTime());
                                ImageView iv_icon = holder.getView(R.id.iv_icon);
                                Glide.with(DatingFragment.this).load(NetWorkTasks.getImgUrl(data.getUserIcon())).placeholder(R.mipmap.ic_launcher).circleCrop().into(iv_icon);
                                ImageView iv_sex = holder.getView(R.id.iv_sex);
                                MyTools.setSexIcon(data.getUserSex(), iv_sex);
                            }
                        });
                        DialogUtils.dismissProgressDialog(getActivity());
                    }
                });
            }

        });
    }

}
