package cn.sampson.android.xiandou.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.Reader;

import cn.sampson.android.xiandou.utils.des.DES3Model;

/**
 * Created by chengyang on 2017/6/15.
 */

public class FileUtils {

    public static FileInputStream getFiles(Context context, String fileName) throws Exception {
        // hsk.plist是你要解析的文件，该文件需放在assets文件夹下
        File file = new File(context.getApplicationContext().getFilesDir() + File.separator + MD5Util.encrypt(fileName) + ".plist");
        if (!file.exists()) {
            String filePath = file.getAbsolutePath();
            DES3Model.descryptionInputStream(context.getApplicationContext().getClass().getClassLoader().getResourceAsStream("assets/" + fileName + ".plist"), filePath);
        }
        return new FileInputStream(file);
    }

    public static String getJsonStringFromAssestFile(Context context, String fileName) throws Exception {
        // hsk.plist是你要解析的文件，该文件需放在assets文件夹下
        File file = new File(context.getApplicationContext().getFilesDir() + File.separator + MD5Util.encrypt(fileName) + ".json");
        if (!file.exists()) {
            String filePath = file.getAbsolutePath();
            DES3Model.descryptionInputStream(context.getApplicationContext().getClass().getClassLoader().getResourceAsStream("assets/" + fileName + ".json"), filePath);
        }

        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];
        Reader reader = new BufferedReader(new FileReader(file));
        int n;
        while ((n = reader.read(buffer)) != -1) {
            builder.append(buffer, 0, n);
        }
        return builder.toString();
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ContextUtil.getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

}

