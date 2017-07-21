package cn.sampson.android.xiandou.core;

import android.text.TextUtils;

import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.sampson.android.xiandou.core.event.GetUserInfoEvent;
import cn.sampson.android.xiandou.core.persistence.UserPreference;
import cn.sampson.android.xiandou.core.retroft.Api.PublicApi;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.core.retroft.base.Result1;
import cn.sampson.android.xiandou.model.CommonField;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.NetworkUtil;
import cn.sampson.android.xiandou.utils.Tip;
import cn.sampson.android.xiandou.utils.ToastUtils;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chengyang on 2017/7/20.
 */

public class UpdatePhotoManager {

    private static UpdatePhotoManager instance = null;
    private UploadManager uploadManager;

    public static String QiNiuToken;


    public static UpdatePhotoManager getInstance() {
        if (instance == null) {
            instance = new UpdatePhotoManager();
        }
        return instance;
    }

    public UpdatePhotoManager() {
        Configuration config = new Configuration.Builder()
                .connectTimeout(20)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .zone(AutoZone.autoZone)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();

        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        uploadManager = new UploadManager(config);
    }

    public void updatePhoto(final File file) {
        if (NetworkUtil.isNetworkAvailable(ContextUtil.getContext())) {
            if (TextUtils.isEmpty(QiNiuToken)) {
                RetrofitWapper.getInstance().getNetService(PublicApi.class).getQiniuToken()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Result1<CommonField>>() {
                            @Override
                            public void call(Result1<CommonField> result) {
                                if (result.data != null) {
                                    QiNiuToken = result.data.uptoken;
                                    qiniuUpdatePhoto(file);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                            }
                        });
            } else {
                qiniuUpdatePhoto(file);
            }
        }
    }

    private void qiniuUpdatePhoto(File file) {
//        data = <File对象、或 文件路径、或 字节数组>
//        String key = <指定七牛服务上的文件名，或 null>;
//        String token = <从服务端SDK获取>;
        uploadManager.put(file, null, QiNiuToken,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            try {
                                updateUserPic(res.getString("keys"));
                                ToastUtils.show("上传成功");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Tip.i("qiniu ==> Upload Success");
                        } else {
                            ToastUtils.show("上传失败");
                            Tip.i("qiniu ==> Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Tip.i("qiniu ==> " + key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }


    private void updateUserPic(final String imgUrl) {
        Map<String, String> map = new HashMap<>();
        map.put(UserApi.USERPIC, imgUrl);
        if (NetworkUtil.isNetworkAvailable(ContextUtil.getContext())) {
            RetrofitWapper.getInstance().getNetService(UserApi.class).updateInfo(map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Result>() {
                        @Override
                        public void call(Result result) {
                            UserPreference.setAvatar(imgUrl);
                            EventBus.getDefault().post(new GetUserInfoEvent());
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                        }
                    });
        }
    }

}
