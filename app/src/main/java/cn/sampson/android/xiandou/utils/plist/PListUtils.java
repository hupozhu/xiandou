package cn.sampson.android.xiandou.utils.plist;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;

import cn.sampson.android.xiandou.utils.MD5Util;
import cn.sampson.android.xiandou.utils.des.DES3Model;

/**
 * Created by chengyang on 2017/6/15.
 */

public class PListUtils {

    public static FileInputStream getFiles(Context context, String fileName) throws Exception {
        // hsk.plist是你要解析的文件，该文件需放在assets文件夹下
        File file = new File(context.getApplicationContext().getFilesDir() + File.separator + MD5Util.encrypt(fileName) + ".plist");
        if (!file.exists()) {
            String filePath = file.getAbsolutePath();
            DES3Model.descryptionInputStream(context.getApplicationContext().getClass().getClassLoader().getResourceAsStream("assets/" + fileName + ".plist"), filePath);
        }
        return new FileInputStream(file);
    }

}
