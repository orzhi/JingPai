package com.xcode.lockcapture.capture;

import android.os.AsyncTask;

import com.xcode.lockcapture.album.LocalImageManager;
import com.xcode.lockcapture.common.GlobalConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/4/8.
 */
public class SavePictureTask extends AsyncTask<byte[], String, String> {
    SavePicListener listener;

    public SavePictureTask(SavePicListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(byte[]... data) {
        initStorage();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = format.format(date) + ".jpg";
        String fullPath = GlobalConfig.RawImageStoreUrl + fileName;

        writeToFile(data[0], fullPath, fileName);
        return null;
    }

    void writeToFile(byte[] data, String fullPath, String fileName) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fullPath);
            out.write(data);
            out.close();
            LocalImageManager.GetInstance().AddImage(fileName);
        } catch (Exception e) {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        LocalImageManager.GetInstance().RefreshUI();
        listener.saveFinish();
        super.onPostExecute(s);
    }

    void initStorage() {
        File file = new File(GlobalConfig.RawImageStoreUrl);
        if (!file.exists())
            file.mkdirs();
    }

    //监听器
    public interface SavePicListener {
        void saveFinish();
    }
}
