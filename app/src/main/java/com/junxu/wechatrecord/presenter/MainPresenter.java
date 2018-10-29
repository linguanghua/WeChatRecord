package com.junxu.wechatrecord.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.junxu.wechatrecord.view.MainView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author linguanghua
 * @date 2018/6/5
 */

public class MainPresenter {

    private Context context;
    private static final String TAG = "MainPresenter";

    private MainView mainView;

    public MainPresenter(Context context, MainView mainView) {
        this.context = context;
        this.mainView = mainView;
    }




    public List<String> getFileName() {

//        get_root();
//         writeCommand();
//        changePermissions();
        List<String> fileNameList = new ArrayList<>();
        try {
            File file = new File("/data/data/com.tencent.mm/MicroMsg/");

            if (file.exists() && file.isDirectory()) {
                Log.e(TAG, "getFileName: s="+file.canRead());

                Toast.makeText(context, "找到目录", Toast.LENGTH_LONG).show();
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        Log.e(TAG, "getFileName: " + f.getName());
                        if (f.getName().length() == 32){
                            fileNameList.add(f.getName());
                        }
                    }
                }else{
                    Toast.makeText(context, "file list 不存在", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context, "未找到任何文件", Toast.LENGTH_LONG).show();
            }
            for (String s:fileNameList) {
                Log.e(TAG, "getFileName: "+s );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNameList;
    }

    // 获取ROOT权限
    public void get_root(){
        if (is_root()){
            Toast.makeText(context, "已经具有ROOT权限!", Toast.LENGTH_LONG).show();
        }
        else{
            try{
                Runtime.getRuntime().exec("su");
            }
            catch (Exception e){
                Toast.makeText(context, "获取ROOT权限时出错!", Toast.LENGTH_LONG).show();
            }
        }

    }
    // 判断是否具有ROOT权限
    private boolean is_root(){
        boolean res = false;
        try{
            if ((!new File("/system/bin/su").exists()) &&
                    (!new File("/system/xbin/su").exists())){
                res = false;
            }
            else {
                res = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }





    private void changePermissions() {
        try {
            Runtime runtime = Runtime.getRuntime();
            readResult(runtime.exec("ls"));
            readResult(runtime.exec("cd data"));
            readResult(runtime.exec("cd data/data"));
            readResult(runtime.exec("cd /data/data/com.tencent.mm"));
            readResult(runtime.exec("ls"));


            String command = "chmod -R 777 /data/data/com.tencent.mm/MicroMsg/";
            Process proc = runtime.exec(command);
            Log.d(TAG, command);
            readResult(proc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readResult(Process proc){
        try {
            InputStream in = proc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line1;
            Log.i(TAG, "返回结果" + reader.readLine());
            while ((line1 = reader.readLine()) != null) {

                Log.i(TAG, "返回结果=" + line1);
            }
            Log.d(TAG,"----- ----- ----- ----- ----");
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void writeCommand(){
        String commands = "chmod -R 777 /data/data/com.tencent.mm/MicroMsg";
        Runtime runtime = Runtime.getRuntime();
        try {
            Runtime.getRuntime().exec("su");
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
