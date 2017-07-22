package cn.sampson.android.xiandou.core.manager;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class ImageUrlProcesser {

    public static final int POSTER_WIDTH = 375;
    public static final int POSTER_HEIGHT = 135;

    public static String reSetImageUrlWH(String imageUrl, int width, int height) {
        return imageUrl.replace("w/100", "w/" + width).replace("h/100", "h/" + height);
    }

}
