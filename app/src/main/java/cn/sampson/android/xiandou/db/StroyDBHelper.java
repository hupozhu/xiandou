package cn.sampson.android.xiandou.db;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.sampson.android.xiandou.utils.DBHelper;

/**
 * 爸爸讲故事
 *
 * Created by chengyang on 2017/6/15.
 */

public class StroyDBHelper {

    private DBHelper mDBHelper;

    public StroyDBHelper(Context context) {
        mDBHelper = new DBHelper(context, "story.db", 2);
        try {
            mDBHelper.createDataBase();
            mDBHelper.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getInfos(int id) {
        Map<String, String> infos = new HashMap<>();

        String sql = "select * from article where chapterid = " + id;
        Cursor cursor = mDBHelper.getDataBase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            infos.put(cursor.getString(cursor.getColumnIndex("title")), cursor.getString(cursor.getColumnIndex("content")));
        }
        cursor.close();
        mDBHelper.close();
        return infos;
    }

}
