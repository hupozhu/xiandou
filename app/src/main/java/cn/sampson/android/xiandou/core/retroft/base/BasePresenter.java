package cn.sampson.android.xiandou.core.retroft.base;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.sampson.android.xiandou.BaseApp;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.AppCache;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.NetworkUtil;
import cn.sampson.android.xiandou.utils.Tip;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * BasePresenter类还对加载过程中的几个状态进行管理，如：错误页面，加载页面，空白页面
 * Created by Administrator on 2016/4/20.
 */
public abstract class BasePresenter<T extends IView> {

    protected T view;
    /*
    当前view的状态
     */
    protected int allRequestCount;
    protected int exceptionRequestCount;
    protected int successRequestCount;
    protected int errorNetRequestCount;

    private boolean showNetErrorLayout;
    private boolean showLoadingLayout;

    protected ViewGroup rootView;
    protected View errorView;
    protected View loadingView;

    protected boolean showLoading;
    protected boolean showNetError;
    /**
     * 页面初次进入时，是否要对页面进行网络失败刷新处理
     */
    protected boolean shouldObserve; //只在首次加载的时候显示loading

    protected Map<String, Observable> requestMap;

    public void register(T view) {
        this.view = view;
    }

    public BasePresenter(T view) {
        register(view);
    }

    public BasePresenter(T view, ViewGroup rootView) {
        this(view, rootView, true, true);
    }

    public BasePresenter(T view, ViewGroup root, boolean showNetError, boolean showLoading) {
        register(view);
        if (root != null) {
            rootView = root;
            this.showLoading = showLoading;
            this.showNetError = showNetError;
            shouldObserve = true;
            requestMap = new HashMap<>();
        }
    }

    protected void requestData(Observable observable, String key) {
        requestData(observable, key, null);
    }

    /**
     * @param observable
     * @param key
     */
    protected void requestData(Observable observable, final String key, final AnsyResultTask ansyTask) {
        requestComeIn(observable, key);
        if (NetworkUtil.isNetworkAvailable(ContextUtil.getContext())) {
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Result>() {
                        @Override
                        public void call(Result result) {
                            if (view == null) return;
                            switch (result.code) {
                                case 0://获取数据成功
                                    successReturn();
                                    if (null != ansyTask) {
                                        new AnsyProcessResultTask(ansyTask).execute(result);
                                        return;
                                    } else {
                                        onResult(result, key);
                                    }
                                    break;

                                default:
                                    exceptionReturn();
                                    onError(result.code, result.message, key);
                                    break;
                            }

                            requestComeOut();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Tip.d("network  error ====>" + throwable.toString());
                            if (view == null) return;

                            errorNetReturn();
                            requestComeOut();
                        }
                    });
        } else {
            noNetWorkReturn();
            requestComeOut();
        }
    }

    /**
     * 一个请求进来，请求的最初位置调用
     */
    private void requestComeIn(Observable observable, final String key) {
        if (!shouldObserve) return;

        allRequestCount++;
        requestMap.put(key, observable);

        //如果还没有显示加载对话框就显示
        if (showLoading && !showLoadingLayout) {
            showLoadingView();
        }
    }

    /**
     * 没有网络的返回，最先的回调
     */
    private void noNetWorkReturn() {
        if (!shouldObserve) return;

        if (showNetError && !showNetErrorLayout) {//需要显示网络异常layout
            showNetErrorView();
        }
    }

    /**
     * 网络异常的返回，最先的回调
     */
    private void exceptionReturn() {
        if (!shouldObserve) return;

        exceptionRequestCount++;
    }

    private void errorNetReturn() {
        if (!shouldObserve) return;

        errorNetRequestCount++;
    }

    /**
     * 网络成功，最先回掉
     */
    private void successReturn() {
        if (!shouldObserve) return;

        successRequestCount++;
    }

    /**
     * 一个请求完成,请求的最末位置调用
     */
    private void requestComeOut() {
        if (!shouldObserve) return;

        if (allRequestCount == successRequestCount + exceptionRequestCount) {
            shouldObserve = false;
            removeOtherLayout();
        } else if (errorNetRequestCount > 0 && allRequestCount == successRequestCount + errorNetRequestCount + exceptionRequestCount) {
            if (showNetError && !showNetErrorLayout) {//需要显示网络异常layout
                showNetErrorView();
            }
        }
    }

    private void removeOtherLayout() {
        if (showNetError && errorView != null) {
            rootView.removeViewAt(rootView.getChildCount() - 1);
            errorView = null;
        }
        if (showLoading && loadingView != null) {
            rootView.removeViewAt(rootView.getChildCount() - 1);
            loadingView = null;
        }
    }

    /**
     * 重新加载所有请求
     */
    public void onReLoad() {
        Set<String> keySet = requestMap.keySet();
        Iterator<String> keys = keySet.iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            requestData(requestMap.get(key), key);
        }
    }

    public void resetData() {
        allRequestCount = 0;
        exceptionRequestCount = 0;
        successRequestCount = 0;
    }

    private void onRefreshClick() {
        errorView.setVisibility(View.GONE);
        showNetErrorLayout = false;
        resetData();
        onReLoad();
    }


    //重连网络的view关闭就代表初次的所有请求都过来了
    protected void showLoadingView() {
        showLoadingLayout = true;
        loadingView = LayoutInflater.from(ContextUtil.getContext()).inflate(R.layout._loading_view, rootView, false);
        rootView.addView(loadingView, rootView.getChildCount());
    }

    protected void showNetErrorView() {
        showNetErrorLayout = true;
        if (errorView != null && errorView.getVisibility() == View.GONE) {
            errorView.setVisibility(View.VISIBLE);
        } else if (errorView == null) {
            errorView = LayoutInflater.from(ContextUtil.getContext()).inflate(R.layout._loading_error_refresh, rootView, false);
            errorView.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefreshClick();
                }
            });
            rootView.addView(errorView, rootView.getChildCount());
        }
    }


    protected abstract void onResult(Result result, String key);

    protected void setError(int errorCode, String errorStr, String key) {
    }

    protected void onError(int errorCode, String errorStr, String key) {
        Tip.w(key + "===>" + errorStr);

        if (!TextUtils.isEmpty(errorStr))
            Toast.makeText(ContextUtil.getContext(), errorStr, Toast.LENGTH_SHORT).show();

        view.setError(errorCode, errorStr, key);
        setError(errorCode, errorStr, key);
    }

    //（需要显示loading和refresh情况）请求进来，显示loading

    class AnsyProcessResultTask extends AsyncTask<Result, Object, Result> {

        private AnsyResultTask task;

        public AnsyProcessResultTask(AnsyResultTask task) {
            this.task = task;
        }

        @Override
        protected Result doInBackground(Result... params) {
            return task.doInBackground(params[0]);
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            if (view == null) return;

            task.onResult(result);
//            if (!TextUtils.isEmpty(result.show_msg)) {
//                Toaster.showShort(BaseApp.app, result.show_msg);
//            }
            requestComeOut();
        }
    }

    public interface AnsyResultTask<T> {

        Result<T> doInBackground(Result<T> result);

        void onResult(Result<T> result);

    }
}
