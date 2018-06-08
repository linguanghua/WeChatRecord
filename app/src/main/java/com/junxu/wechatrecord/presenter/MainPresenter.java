package com.junxu.wechatrecord.presenter;

import android.content.Context;
import android.util.Log;


import com.junxu.wechatrecord.view.MainView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linguanghua on 2018/6/5.
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
        List<String> fileNameList = new ArrayList<>();
        try {
            File fileList = new File("/data/data/com.tencent.mm/MicroMsg/");
            if (fileList.exists()) {
                File[] files = fileList.listFiles();
                if (files != null && files.length > 0) {
                    for (File f : files) {
                        Log.e(TAG, "getFileName: " + f.getName());
                        if (f.getName().length() == 32){
                            fileNameList.add(f.getName());
                        }
                    }

                }
            }
            for (String s:fileNameList) {
                Log.e(TAG, "getFileName: "+s );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileNameList;
    }


}
