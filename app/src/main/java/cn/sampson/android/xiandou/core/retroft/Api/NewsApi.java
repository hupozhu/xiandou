package cn.sampson.android.xiandou.core.retroft.Api;

import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleDetail;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleItem;
import cn.sampson.android.xiandou.ui.haoyun.domain.Index;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 资讯类型
 */

public interface NewsApi {

    @GET("/index/index")
    Observable<Result<Index>> getIndex();

    @GET("/articles/articles/{new_tag}")
    Observable<Result<ListItem<ArticleItem>>> getNewsByTag(@Path("new_tag") String tag, @Query("page") int page, @Query("limit") int limit);

    @GET("/articles/article/{new_id}")
    Observable<Result<ArticleDetail>> getNewDetail(@Path("new_id") String tag);
}
