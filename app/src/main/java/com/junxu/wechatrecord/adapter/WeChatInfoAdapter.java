package com.junxu.wechatrecord.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junxu.wechatrecord.R;
import com.junxu.wechatrecord.bean.UserBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by linguanghua on 2018/6/7.
 */

public class WeChatInfoAdapter extends RecyclerView.Adapter<WeChatInfoAdapter.ViewHolder> {

    private Context context;
    private List<UserBean> weChatInfoList;



    public WeChatInfoAdapter(Context context, List<UserBean> weChatInfoList) {
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

           final String talker = weChatInfoList.get(position).getUsername();
            holder.weChatId.setText(weChatInfoList.get(position).getUsername());
            holder.name.setText(weChatInfoList.get(position).getNickname());
            holder.userName.setText(weChatInfoList.get(position).getAlias());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(talker);
                }
            });
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

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(String talker);
    }



}
