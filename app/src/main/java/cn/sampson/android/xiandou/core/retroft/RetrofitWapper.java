package cn.sampson.android.xiandou.core.retroft;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.sampson.android.xiandou.config.AppConfig;
import cn.sampson.android.xiandou.core.persistence.UserPreference;
import cn.sampson.android.xiandou.core.presenter.UserPresenter;
import cn.sampson.android.xiandou.utils.CacheUtil;
import cn.sampson.android.xiandou.utils.JsonUtil;
import cn.sampson.android.xiandou.utils.Tip;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/3/24.
 */
public class RetrofitWapper {

    private static RetrofitWapper instance = null;
    private Retrofit retrofit = null;
    private OkHttpClient okHttpClient = null;
    private Gson gson = null;
    private boolean refresh = false;

    private RetrofitWapper() {
        if (gson == null)
            gson = new GsonBuilder().serializeNulls().create();
    }

    public static RetrofitWapper getInstance() {
        if (instance == null) {
            instance = new RetrofitWapper();
        }
        return instance;
    }

    public void refreshEnvi() {
        refresh = true;
    }

    public <T> T getNetService(Class<T> tClass) {
        return getNetService(tClass, AppConfig.ENVIRONMENT[AppConfig.index]);
    }

    public <T> T getNetService(Class<T> tClass, String baseUrl) {
        if (okHttpClient == null) {
            //okhttp设置拦截器
            Interceptor requestInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    try {
                        Request originalRequest = chain.request();
                        Request newRequest;
                        if (!TextUtils.isEmpty(UserPreference.getToken()) && !TextUtils.isEmpty(UserPreference.getSign())) {
                            newRequest = originalRequest
                                    .newBuilder()
                                    .addHeader("token", UserPreference.getToken())
                                    .addHeader("sign", UserPreference.getSign())
                                    .build();
                        } else {
                            newRequest = originalRequest;
                        }
                        return chain.proceed(newRequest);

                    } catch (SocketTimeoutException exception) {

                    } catch (ConnectException exception) {

                    }
                    return null;
                }
            };

            //https
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[0];
                    }
                }};

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                //log开关
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLogger());
                if (Tip.isTesting)
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                else
                    interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

                File cache = CacheUtil.getDefaultCacheDir();
                okHttpClient = new OkHttpClient().newBuilder()
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        })
                        .sslSocketFactory(sslSocketFactory)
                        .connectTimeout(AppConfig.NET_CONNECT_TIMEOUT, TimeUnit.SECONDS) //设置连接超时时间
                        .writeTimeout(AppConfig.NET_WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(AppConfig.NET_READ_TIMEOUT, TimeUnit.SECONDS)
                        .addInterceptor(requestInterceptor)
                        .cache(new Cache(cache, CacheUtil.calculateDiskCacheSize(cache)))
                        .addInterceptor(interceptor)
                        .build();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        if (retrofit == null || refresh) {
            refresh = false;
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit.create(tClass);
    }

    private class HttpLogger implements HttpLoggingInterceptor.Logger {
        private StringBuilder mMessage = new StringBuilder();

        @Override
        public void log(String message) {
            // 请求或者响应开始
            if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
                mMessage.setLength(0);
            }
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            if ((message.startsWith("{") && message.endsWith("}"))
                    || (message.startsWith("[") && message.endsWith("]"))) {
                message = JsonUtil.formatJson(JsonUtil.decodeUnicode(message));
            }
            mMessage.append(message.concat("\n"));
            // 响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP")) {
                Tip.d(mMessage.toString());
            } else if (message.startsWith("<-- HTTP FAILED")) {
                Tip.d("网络错误");
            }
        }
    }

}
