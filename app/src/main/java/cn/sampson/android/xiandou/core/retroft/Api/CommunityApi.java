package cn.sampson.android.xiandou.core.retroft.Api;

import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.community.domain.ArticleItem;
import cn.sampson.android.xiandou.ui.community.domain.CommunityIndex;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chengyang on 2017/7/12.
 */

public interface CommunityApi {

    @GET("/community/index")
    Observable<Result<CommunityIndex>> getUserInfo(@Query("page") int page, @Query("limit") int limit);

    @GET("/community/articles/{artTag}")
    Observable<Result<ListItem<ArticleItem>>> getArticleList(@Path("artTag") String tag, @Query("page") int page, @Query("limit") int limit);

}
