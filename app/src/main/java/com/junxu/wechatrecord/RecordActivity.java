package com.junxu.wechatrecord;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.junxu.wechatrecord.adapter.RecordListAdapter;
import com.junxu.wechatrecord.bean.Message;
import com.junxu.wechatrecord.presenter.RecordPresenter;
import com.junxu.wechatrecord.utils.UinSPUtils;
import com.junxu.wechatrecord.view.RecordView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordActivity extends AppCompatActivity implements RecordView{


    @BindView(R.id.rv_record_list)
    RecyclerView rvRecordList;

    private RecordPresenter presenter;

    public static void startAction(Context context,String filePath,int po,String talker){
        Intent intent = new Intent(context,RecordActivity.class);
        intent.putExtra("filePath",filePath);
        intent.putExtra("position",po);
        intent.putExtra("talker",talker);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        presenter = new RecordPresenter(this);

        String filePath = getIntent().getStringExtra("filePath");
        int po = getIntent().getIntExtra("position",-1);
        String talker = getIntent().getStringExtra("talker");
        String uin = UinSPUtils.getUin("uin" + po);
        presenter.showRecord(this, filePath,uin,talker);
    }

    private void initRecyclerView(List<Message> messages){
        rvRecordList.setLayoutManager(new LinearLayoutManager(this));
        RecordListAdapter adapter = new RecordListAdapter(messages);
        rvRecordList.setAdapter(adapter);
    }


    @Override
    public void onSetRecordData(List<Message> messageList) {
        initRecyclerView(messageList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.destroy();
        }
    }
}
