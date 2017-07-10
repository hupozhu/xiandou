package cn.sampson.android.xiandou.core.retroft.base;

/**
 * Created by chengyang on 2017/6/12.
 */

public class Result<T extends Object> {

    public int code;
    public String massage;
    public String message;
    public T data;

}
