package cn.sampson.android.xiandou.core.retroft.Api;

import cn.sampson.android.xiandou.core.retroft.base.Result1;
import cn.sampson.android.xiandou.model.CommonField;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by chengyang on 2017/7/20.
 */

public interface PublicApi {

    @POST("/public/imageQiniuToken")
    Observable<Result1<CommonField>> getQiniuToken();

}
