package cn.sampson.android.xiandou.core.retroft.Api;

import java.util.Map;

import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.community.domain.CommunityCategory;
import cn.sampson.android.xiandou.ui.community.domain.Discovery;
import cn.sampson.android.xiandou.ui.community.domain.PostsDetail;
import cn.sampson.android.xiandou.ui.community.domain.PostsItem;
import cn.sampson.android.xiandou.ui.community.domain.CommentItem;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chengyang on 2017/7/12.
 */

public interface CommunityApi {

    String RE_USER_ID = "re_user_id";
    String RE_COMMENT_ID = "re_comment_id";

    @GET("/community/articles/{artTag}")
    Observable<Result<ListItem<PostsItem>>> getArticleList(@Path("artTag") String tag, @Query("page") int page, @Query("limit") int limit);

    @GET("/community/article/{art_id}")
    Observable<Result<PostsDetail>> getArticleDetail(@Path("art_id") String artId);

    @GET("/community/comments/{artId}")
    Observable<Result<ListItem<CommentItem>>> getCommentList(@Path("artId") String id, @Query("page") int page, @Query("limit") int limit);

    @POST("/community/commentit")
    @FormUrlEncoded
    Observable<Result<String>> commentCommunity(@Field("articleid") long articleid, @Field("content") String content, @FieldMap Map<String, Object> map);

    @POST("/community/article")
    @FormUrlEncoded
    Observable<Result> publishArticle(@Field("title") String title, @Field("content") String content, @Field("tag") String tag);

    @GET("/community/discover")
    Observable<Result<Discovery>> getDiscovery(@Query("page") int page, @Query("limit") int limit);

    @GET("/community/cates")
    Observable<Result<ListItem<CommunityCategory>>> getCategories();
}
