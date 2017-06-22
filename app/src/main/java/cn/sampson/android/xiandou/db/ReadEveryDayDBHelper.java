package cn.sampson.android.xiandou.db;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.sampson.android.xiandou.utils.DBHelper;

/**
 * 怀孕280天
 *
 * Created by chengyang on 2017/6/7.
 */

public class ReadEveryDayDBHelper {

    private DBHelper mDBHelper;

    public ReadEveryDayDBHelper(Context context) {
        mDBHelper = new DBHelper(context, "read_everyday280.db", 3);
        try {
            mDBHelper.createDataBase();
            mDBHelper.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, String> getInfos() {
        Map<Integer, String> infos = new HashMap<>();

        String sql = "select * from todays";
        Cursor cursor = mDBHelper.getDataBase().rawQuery(sql, null);
        while (cursor.moveToNext()) {
            infos.put(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("info")));
        }
        cursor.close();
        mDBHelper.close();
        return infos;
    }

}
