package cn.sampson.android.xiandou.utils.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import cn.sampson.android.xiandou.utils.CacheUtil;
import cn.sampson.android.xiandou.utils.DataUtil;
import cn.sampson.android.xiandou.utils.imageloader.transformation.BlurTransformation;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


/**
 * Created by Administrator on 2016/5/20.
 */
public class ImageLoader {

    ProgressListener listener;

    static private Picasso picasso;
    static private OkHttpClient client;
    static private HttpLoggingInterceptor loggingInterceptor;

    public static void load(Context context, String url, ImageView img) {
        if (!TextUtils.isEmpty(url))
            getPicasso(context).load(url).config(Bitmap.Config.RGB_565).fit().centerCrop().into(img);
    }

    public static void loadWithBlur(Context context, String url, ImageView img) {
        if (!TextUtils.isEmpty(url))
            getPicasso(context).load(url).transform(new BlurTransformation(context, 1, 10)).config(Bitmap.Config.RGB_565).fit().centerCrop().into(img);
    }

    /**
     * Singleton Picasso shared cache with OkH ttp/Retrofit
     */
    static public Picasso getPicasso(Context context) {
        if (picasso == null) {
            synchronized (ImageLoader.class) {
                picasso = getPicasso(context, null);
            }
        }
        return picasso;
    }


    /**
     * Download Big Image only, Not singleton but shared cache
     */

    static public Picasso getPicasso(Context context, ProgressListener listener) {
        if (null != listener) {
            OkHttpClient client = getProgressBarClient(listener);
            OkHttpDownLoader downloader = new OkHttpDownLoader(client);

            return new Picasso.Builder(context)
                    .downloader(downloader)
                    .memoryCache(com.squareup.picasso.Cache.NONE)
                    .build();
        } else {
            return Picasso.with(context);
        }
    }

    static public synchronized OkHttpClient getClient() {
        if (client == null) {
            File cache = CacheUtil.createDefaultCacheDir();
            client = new OkHttpClient.Builder()
                    //Interceptor -> cache -> NetworkInterceptor
                    .addNetworkInterceptor(getLogger())
                    .cache(new Cache(cache, CacheUtil.calculateDiskCacheSize(cache)))
                    .build();
        }
        return client;
    }

    private static OkHttpClient getProgressBarClient(final ProgressListener listener) {
        return getClient().newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), listener))
                        .build();
            }
        }).build();
    }

    private static synchronized Interceptor getLogger() {
        if (loggingInterceptor == null) {
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("okhttp", message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.HEADERS);
        }
        return loggingInterceptor;
    }

    public interface ProgressListener {
        @WorkerThread
        void update(@IntRange(from = 0, to = 100) int percent, String capacity);
    }


    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {

            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    if (progressListener != null) {
                        progressListener.update(((int) ((100 * totalBytesRead) / responseBody.contentLength())), DataUtil.getFormatSize(totalBytesRead));
                    }
                    return bytesRead;
                }
            };
        }
    }

}
