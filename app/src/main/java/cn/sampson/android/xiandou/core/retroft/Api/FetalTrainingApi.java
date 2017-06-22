package cn.sampson.android.xiandou.core.retroft.Api;

import java.util.ArrayList;

import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.Musics;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 胎教
 * Created by chengyang on 2017/6/12.
 */

public interface FetalTrainingApi {

    @GET("fjfy/index/getmusiclist")
    Observable<Result<ArrayList<Musics>>> getMusicList(@Query("month") int month);

}
