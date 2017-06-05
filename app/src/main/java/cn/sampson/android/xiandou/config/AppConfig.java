package cn.sampson.android.xiandou.config;

/**
 * Created by chengyang on 2017/1/3.
 */

public class AppConfig {

    public static final String[] ENVIRONMENT = {
            "http://stageapi.getop.cc/",//测试环境
            "http://stj.getop.cc/StjClient/",//模拟环境
            "https://stjia.cn/StjClient/"//线上环境
    };

    public static int index = 0;

    public static int NET_CONNECT_TIMEOUT = 20;
    public static int NET_WRITE_TIMEOUT = 10;
    public static int NET_READ_TIMEOUT = 10;

}
