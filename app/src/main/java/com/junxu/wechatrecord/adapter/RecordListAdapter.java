package com.junxu.wechatrecord.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junxu.wechatrecord.R;
import com.junxu.wechatrecord.bean.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by linguanghua on 2018/8/22.
 */

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private List<Message> messageList = new ArrayList<>();
    private static final String TAG = "RecordListAdapter";

    public RecordListAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: ");
        View view;
        Log.e(TAG, "onCreateViewHolder: "+viewType );
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_record_item_receive, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_record_item_send, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (messageList.size()>0){
            if (messageList.get(position).isSend() == 0) {
                holder.tvWechatName.setText(messageList.get(position).getUsername()+":");
            }
            holder.tvRecordContent.setText(messageList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: ");
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Log.e(TAG, "getItemViewType: ");
        return messageList.get(position).isSend();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_wechat_name)
        TextView tvWechatName;
        @BindView(R.id.tv_record_content)
        TextView tvRecordContent;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
