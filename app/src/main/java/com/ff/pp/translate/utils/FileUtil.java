package com.ff.pp.translate.utils;

import android.content.Context;

import com.ff.pp.translate.Application.MyApplication;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by PP on 2017/6/2.
 */

public class FileUtil {

        //定义文件的名字
        public static final String docName = "record.txt";

        public static void saveString(String content) {
            Context context= MyApplication.getContext();
            FileOutputStream fos = null;
            try {
                //打开文件输出流，接收参数是文件名和模式
                fos = context.openFileOutput(docName,Context.MODE_PRIVATE);
                fos.write(content.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //读取，返回字符串（JSON）
        public static String readString() {
            Context context= MyApplication.getContext();
            FileInputStream fis = null;
            StringBuffer sBuf = new StringBuffer();
            try {
                fis = context.openFileInput(docName);
                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = fis.read(buf)) != -1) {
                    sBuf.append(new String(buf,0,len));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(sBuf != null) {
                return sBuf.toString();
            }
            return null;
        }

        public static String getFilePath() {
            return MyApplication.getContext().getFilesDir().getAbsolutePath();
        }
}
