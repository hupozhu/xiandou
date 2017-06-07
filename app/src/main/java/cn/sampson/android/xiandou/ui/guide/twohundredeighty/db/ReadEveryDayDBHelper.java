package cn.sampson.android.xiandou.ui.guide.twohundredeighty.db;

import android.content.Context;
import android.database.Cursor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.sampson.android.xiandou.utils.DBHelper;

/**
 * Created by chengyang on 2017/6/7.
 */

public class ReadEveryDayDBHelper {

    private DBHelper mDBHelper;
    private Context mContext;

    public ReadEveryDayDBHelper(Context context) {
        mContext = context;
        mDBHelper = new DBHelper(context);
        try {
            mDBHelper.createDataBase();
            mDBHelper.openDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, String> getInfos() {
        Map<Integer, String> infos = new HashMap<>();

        String sql = "SELECT * FROM todays";
        Cursor cursor = mDBHelper.getReadableDatabase().rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            infos.put(cursor.getInt(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("info")));
        }
        cursor.close();
        return infos;
    }

}
