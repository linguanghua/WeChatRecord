package com.junxu.wechatrecord;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.junxu.wechatrecord.adapter.WeChatInfoAdapter;
import com.junxu.wechatrecord.app.WeChatApplication;
import com.junxu.wechatrecord.bean.Message;
import com.junxu.wechatrecord.bean.UserBean;
import com.junxu.wechatrecord.presenter.ContactPresenter;
import com.junxu.wechatrecord.utils.UinSPUtils;
import com.junxu.wechatrecord.view.ContactView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeChatInfoActivity extends AppCompatActivity implements ContactView, WeChatInfoAdapter.OnItemClickListener {

    @BindView(R.id.rv_wechat_info)
    RecyclerView recyclerView;

    @BindView(R.id.et_uin)
    EditText etUin;

    @BindView(R.id.btn_uin_commit)
    Button btnUinCommit;

    @BindView(R.id.layout_uin)
    LinearLayout layoutUin;

    String filePath;
    String mUin;
    int position = -1;

    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private ContactPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_info);
        ButterKnife.bind(this);
        preferences = WeChatApplication.mContext.getApplicationContext().getSharedPreferences("uin", Context.MODE_PRIVATE);
        editor = preferences.edit();

        Intent intent = getIntent();
        if (intent != null) {
            position = intent.getIntExtra("position", -1);
            String fileName = intent.getStringExtra("fileName");
            if (!TextUtils.isEmpty(fileName)) {
                filePath = "/data/data/com.tencent.mm/MicroMsg/" + fileName + "/EnMicroMsg.db";
            }
            if (position != -1) {
                mUin = preferences.getString("uin" + position, "");
                if(!TextUtils.isEmpty(mUin)){
                    layoutUin.setVisibility(View.GONE);
                    getPermission();
                }
            }
        }

        btnUinCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUin = etUin.getText().toString();
                if (TextUtils.isEmpty(mUin)) {
                    etUin.setText("uin输入为空了，请重新输入！");
                } else {
                    UinSPUtils.put("uin" + position,mUin);
                    if (!TextUtils.isEmpty(filePath)) {
                        getPermission();
                    }

                }
            }
        });


    }

    private void getRecord(){
        if (position!=-1 && !TextUtils.isEmpty(filePath)) {
            presenter = new ContactPresenter(this);
            presenter.init(this, filePath, mUin);
            presenter.showAddressList();
        }
    }


    public void getPermission() {
        /**
         * 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted();
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            getRecord();
            return;
        }

        /**
         * 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSION_REQUEST_CODE);
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                getRecord();

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("恢复记录需要信息读取权限，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public void onSetWeChatContactInfo(List<UserBean> weChatInfoList) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WeChatInfoAdapter weChatInfoAdapter = new WeChatInfoAdapter(this, weChatInfoList);
        weChatInfoAdapter.setmOnItemClickListener(this);
        recyclerView.setAdapter(weChatInfoAdapter);
    }

    @Override
    public void onItemClick(String talker) {
        RecordActivity.startAction(this,filePath,position,talker);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.destroy();
            presenter = null;
        }
    }
}
