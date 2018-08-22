package com.junxu.wechatrecord;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.junxu.wechatrecord.adapter.FileNameAdapter;
import com.junxu.wechatrecord.presenter.MainPresenter;
import com.junxu.wechatrecord.view.MainView;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{

    private static final String TAG = "MainActivity";

    MainPresenter presenter;

    @BindView(R.id.tv_main_title)
    TextView tvMainTitle;

    @BindView(R.id.rv_wechat)
    RecyclerView rvWechat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase.loadLibs(this);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this,this);
        final List<String> fileNameList = presenter.getFileName();
        if (fileNameList!=null&&fileNameList.size()>0) {

            rvWechat.setVisibility(View.VISIBLE);
            FileNameAdapter adapter = new FileNameAdapter(fileNameList, this);
            rvWechat.setLayoutManager(new LinearLayoutManager(this));
            rvWechat.setAdapter(adapter);
            adapter.setMyItemClickListener(new FileNameAdapter.MyItemClickListener() {
                @Override
                public void OnClickListener(int position) {
                    Intent intent = new Intent(MainActivity.this, WeChatInfoActivity.class);
                    intent.putExtra("fileName", fileNameList.get(position));
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }else {
            tvMainTitle.setText("未检测到相关微信号文件");
        }

    }

}
