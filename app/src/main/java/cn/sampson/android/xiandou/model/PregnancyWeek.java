package cn.sampson.android.xiandou.model;

import java.util.ArrayList;

/**
 * Created by chengyang on 2017/6/6.
 */

public class PregnancyWeek {

    public String overview;
    public String weeknumber;
    public FirstInfo firstinfo;
    public ArrayList<String> nametype;

    public static class FirstInfo {
        public String imagedev;
        public String contentofdev;
    }

}
