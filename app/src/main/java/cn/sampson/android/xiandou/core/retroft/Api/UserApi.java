package cn.sampson.android.xiandou.core.retroft.Api;

import java.util.Map;

import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.community.domain.ArticleItem;
import cn.sampson.android.xiandou.ui.community.domain.CommentItem;
import cn.sampson.android.xiandou.ui.mine.domain.User;
import cn.sampson.android.xiandou.ui.mine.domain.UserToken;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by chengyang on 2017/7/5.
 */

public interface UserApi {

    String NICKMAME = "nickname";
    String USERSEX = "user_sex";
    String AREA = "area";
    String BIRTHDAY = "birthday";
    String USERPIC = "user_pic";

    @POST("/login/sms")
    @FormUrlEncoded
    Observable<Result> getAuthCode(@Field("phone") String phone);

    @POST("/login/login")
    @FormUrlEncoded
    Observable<Result<UserToken>> login(@Field("phone") String phone, @Field("code") String code);

    @GET("/users/user")
    Observable<Result<User>> getUserInfo();

    @PATCH("/users/user")
    @FormUrlEncoded
    Observable<Result> updateInfo(@FieldMap Map<String, String> map);

    @GET("/users/comments")
    Observable<Result<ListItem<CommentItem>>> getComments();

    @GET("/users/collects")
    Observable<Result<ListItem<ArticleItem>>> getCollections();
}
