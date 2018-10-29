package com.junxu.wechatrecord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.junxu.wechatrecord.adapter.FileNameAdapter;
import com.junxu.wechatrecord.presenter.MainPresenter;
import com.junxu.wechatrecord.view.MainView;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{

    private static final String TAG = "MainActivity";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
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
        writeCommand();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            getFileInfo();
        }else {
            checkPermission();
//            getPermissions();
        }

    }

    private void getFileInfo() {
        Log.d(TAG, "getFileInfo: 文件信息");
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

    public void getPermissions() {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            Log.d(TAG, "getPermissions: "+permission);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
                Log.d(TAG, "getPermissions: 申请权限");
            }else{
                getFileInfo();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: 申请结果"+requestCode);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    Log.d(TAG, "onRequestPermissionsResult: 未授予");
                    break;
                }
            }

            if (isAllGranted) {
                Log.d(TAG, "onRequestPermissionsResult: 已授予");
//                 如果所有的权限都授予了, 则执行备份代码
                getFileInfo();
            }
        }
    }

    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
            }
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "checkPermission: 已经授权！");
            getFileInfo();
        }
    }

    private void writeCommand(){
        String commands = "chmod -R 777 /data/data/com.tencent.mm/MicroMsg";
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("su");
            Process process = runtime.exec(commands);
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            System.out.println("OUTPUT");
            while ((line = stdoutReader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("ERROR");
            while ((line = stderrReader.readLine()) != null) {
                System.out.println(line);
            }
            int exitVal = process.waitFor();
            System.out.println("process exit value is " + exitVal);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
