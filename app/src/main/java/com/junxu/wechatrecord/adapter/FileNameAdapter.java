package com.junxu.wechatrecord.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junxu.wechatrecord.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by linguanghua on 2018/6/8.
 */

public class FileNameAdapter extends RecyclerView.Adapter<FileNameAdapter.ViewHolder> {
    List<String> fileNameList;
    Context context;

    public FileNameAdapter(List<String> fileNameList, Context context) {
        this.fileNameList = fileNameList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_file_name_item,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (fileNameList!=null && fileNameList.size()>0){
            holder.tvFileName.setText("微信号"+(position+1)+":"+fileNameList.get(position));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onMyItemClickListener!=null){
                    onMyItemClickListener.OnClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_file_name)
        TextView tvFileName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,120));
        }
    }

    private MyItemClickListener onMyItemClickListener;

    public void setMyItemClickListener(MyItemClickListener onMyItemClickListener) {
        this.onMyItemClickListener = onMyItemClickListener;
    }

    public interface MyItemClickListener{
        void OnClickListener(int position);
    }
}
