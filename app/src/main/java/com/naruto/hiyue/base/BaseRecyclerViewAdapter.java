package com.naruto.hiyue.base;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @Purpose
 * @Author Naruto Yang
 * @CreateDate 2019/4/27 0027
 * @Note
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.VH> {
    private List<T> dataList;

    public BaseRecyclerViewAdapter(List<T> dataList) {
        this.dataList = dataList;
    }

    protected abstract int getLayoutRes(int viewType);

    protected abstract void setView(VH holder, T data, int position);

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(viewType), parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        setView(holder, dataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        SparseArray<View> viewMap = new SparseArray<>();

        public VH(@NonNull View itemView) {
            super(itemView);
        }

        public <E extends View> E getView(int id) {
            E e = (E) viewMap.get(id);
            if (e == null) {
                e = itemView.findViewById(id);
                viewMap.put(id, e);
            }
            return e;
        }

       public void setText(int id,String text){
            TextView textView=getView(id);
            textView.setText(text);
        }
    }
}
