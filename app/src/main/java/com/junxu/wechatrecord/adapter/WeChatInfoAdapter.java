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
 * Created by linguanghua on 2018/6/7.
 */

public class WeChatInfoAdapter extends RecyclerView.Adapter<WeChatInfoAdapter.ViewHolder> {

    private Context context;
    private List<String> weChatInfoList;



    public WeChatInfoAdapter(Context context, List<String> weChatInfoList) {
        this.context = context;
        this.weChatInfoList = weChatInfoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_rv_item,null,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (weChatInfoList!=null&&weChatInfoList.size()>0){
           String weChatInfo = weChatInfoList.get(position);
           String[] weChatInfos = weChatInfo.split("\\*");
            holder.weChatId.setText(weChatInfos[0]);
            holder.name.setText(weChatInfos[1]);
            holder.userName.setText(weChatInfos[2]);
        }
    }

    @Override
    public int getItemCount() {
        return weChatInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.weChat_id)
        TextView weChatId;
        @BindView(R.id.username)
        TextView userName;
        @BindView(R.id.name)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }



}
