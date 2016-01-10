package com.cmbb.app.net.http;

import android.content.Context;
import android.os.Handler;

import com.cmbb.app.action.common.Logger;
import com.cmbb.app.action.common.Tools;
import com.cmbb.app.action.environment.Environments;
import com.cmbb.app.application.CMBBApplication;
import com.cmbb.app.entity.base.BaseEntity;
import com.cmbb.app.net.iface.RequestCallback;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Storm on 2015/11/16.
 * 网络访问
 */
public class HttpRequest {
    public static HttpRequest instance;
    private final String NetError = "网络不给力，请检查你的网络设置";

    public static HttpRequest getInstance() {
        if (instance == null) {
            instance = new HttpRequest();
        }
        return instance;
    }

    /**
     * @Directions:普通网络数据请求
     * @author: liman
     * @date: 2015-8-12
     * @tag:@param params 参数表
     * @tag:@param type 实体类型 >>>> 例: LoginEntiy.class
     * @tag:@param callback 结果回调
     */
    public void request(Map<String, String> paramsMap, final Type type, String method, String pv,
                        final RequestCallback callback) {
        Context context = CMBBApplication.getInstance();
        if (Tools.checkNetworkEnable(context)) {
            String url = Environments.getServerUrl(pv, method);

            url = OkHttpUtil.attachHttpGetParam(url, paramsMap);
            Logger.i("请求url:", url);
            Request request = new Request.Builder().url(url).build();
            final long starTime = System.currentTimeMillis();
            OkHttpUtil.enqueue(request, new MyCallback(starTime, type, callback));
        } else {
            failed(NetError, callback, 1000);
        }
    }

    public String checkJson(String value) {
        if (Tools.isEmpty(value) || value.startsWith("<")) {
            return null;
        }
        return value;
    }

    class MyCallback implements Callback {
        private long startTime;
        private Type type;
        private RequestCallback callback;

        public MyCallback(long startTime, Type type, RequestCallback callback) {
            this.startTime = startTime;
            this.type = type;
            this.callback = callback;
        }

        @Override
        public void onFailure(Request request, IOException e) {
            failed(NetError, callback, 0);
            RequestBody body = request.body();
            if (body != null) {
                Logger.i("服务器返回失败 = >", body.toString());
            }
            printTimeDelay(startTime);
            e.printStackTrace();
        }

        @Override
        public void onResponse(Response response) throws IOException {
            String result = response.body().string();
            Logger.i("请求返回成功:", result);
            result = checkJson(result);
            Logger.i("请求返回成功(处理之后):", result);

            if (!Tools.isEmpty(result)) {
                try {
                    if (type != null) {
                        BaseEntity entity = (BaseEntity) new GsonBuilder()
                                .serializeNulls().create()
                                .fromJson(result, type);
                        sucess(entity, result, callback);
                    } else {
                        sucess(null, result, callback);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    Logger.i("httpRequest", "Json解析出错");
                    failed(NetError, callback, 0);
                }
            } else {
                failed(NetError, callback, 0);
            }

            printTimeDelay(startTime);
        }
    }

    private void printTimeDelay(long startTime) {
        Logger.i("服务器响应时长:", String.valueOf(System.currentTimeMillis() - startTime));
    }

    private Handler handler = new Handler();

    private void sucess(final BaseEntity entity, final String result, final RequestCallback callback) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onRequestSucess(entity, result);
                }
            }
        },0);
    }

    private void failed(final String result, final RequestCallback callback, long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onRequestFailed(result);
                }
            }
        }, delay);
    }
}
