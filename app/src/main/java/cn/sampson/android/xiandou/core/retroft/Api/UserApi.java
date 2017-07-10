package cn.sampson.android.xiandou.core.retroft.Api;

import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.ui.mine.domain.User;
import cn.sampson.android.xiandou.ui.mine.domain.UserToken;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chengyang on 2017/7/5.
 */

public interface UserApi {

    @POST("/login/sms")
    @FormUrlEncoded
    Observable<Result> getAuthCode(@Field("phone") String phone);

    @POST("/login/login")
    @FormUrlEncoded
    Observable<Result<UserToken>> login(@Field("phone") String phone, @Field("code") String code);

    @GET("/users/user")
    Observable<Result<User>> getUserInfo();
}
