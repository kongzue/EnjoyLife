package com.kongzue.enjoylife.request;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.kongzue.enjoylife.BuildConfig;
import com.kongzue.enjoylife.listener.ResponseListener;
import com.kongzue.enjoylife.util.Parameter;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * BaseOkHttp
 * Created by myzcx on 2017/12/27.
 * ver:1.0
 */

public class HttpRequest {

    private static int GET_REQUEST = 1;
    private static int POST_REQUEST = 0;

    //Https请求需要传入Assets目录下的证书文件名称
    private String SSLInAssetsFileName;

    private Parameter headers;

    private static OkHttpClient okHttpClient;
    private static Activity context;

    //单例
    private static HttpRequest httpRequest;

    private HttpRequest() {
    }

    //默认请求创建方法
    public static HttpRequest getInstance(Activity c) {
        if (httpRequest == null) {
            synchronized (HttpRequest.class) {
                if (httpRequest == null) {
                    httpRequest = new HttpRequest();
                    context = c;
                }
            }
        }
        return httpRequest;
    }

    //信任指定证书的Https请求
    public static HttpRequest getInstance(Activity c, String SSLFileNameInAssets) {
        if (httpRequest == null) {
            synchronized (HttpRequest.class) {
                if (httpRequest == null) {
                    httpRequest = new HttpRequest();
                    httpRequest.context = c;
                    httpRequest.SSLInAssetsFileName = SSLFileNameInAssets;
                }
            }
        }
        return httpRequest;
    }

    public String getSSLInAssetsFileName() {
        return SSLInAssetsFileName;
    }

    public HttpRequest setSSLInAssetsFileName(String SSLInAssetsFileName) {
        this.SSLInAssetsFileName = SSLInAssetsFileName;
        return this;
    }

    public Parameter getHeaders() {
        return headers;
    }

    public HttpRequest setHeaders(Parameter headers) {
        this.headers = headers;
        return this;
    }

    public void postRequest(String partUrl, final Parameter parameter,
                            final ResponseListener listener) {
        doRequest(partUrl, parameter, listener, POST_REQUEST);
    }

    public void getRequest(String partUrl, final Parameter parameter,
                           final ResponseListener listener) {
        doRequest(partUrl, parameter, listener, GET_REQUEST);
    }

    private void doRequest(final String partUrl, final Parameter parameter, final ResponseListener listener, int requestType) {

        if (BuildConfig.DEBUG)
            Log.i(">>>", "buildRequest:" + partUrl + "\nparameter:" + parameter.toParameterString());

        try {
            OkHttpClient okHttpClient;
            if (SSLInAssetsFileName == null || SSLInAssetsFileName.isEmpty()) {
                okHttpClient = new OkHttpClient();
            } else {
                okHttpClient = getOkHttpClient(context, context.getAssets().open(SSLInAssetsFileName));
            }

            RequestBody requestBody = parameter.toOkHttpParameter();

            //创建请求
            okhttp3.Request request;
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
            //请求类型处理
            if (requestType == GET_REQUEST) {
                builder.url(partUrl.contains("?") ? partUrl : partUrl + "?" + parameter.toParameterString());
            } else {
                builder.url(partUrl);
                builder.post(requestBody);
            }
            //请求头处理
            if (parameter != null) {
                if (headers != null && !headers.entrySet().isEmpty()) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            request = builder.build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    Log.e(">>>", "failure:" + partUrl + "\nparameter:" + parameter.toParameterString() + "\ninfo:" + e);
                    //回到主线程处理
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onResponse(null, e);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    final String strResponse = response.body().string();
                    if (BuildConfig.DEBUG)
                        Log.i(">>>", "request:" + partUrl + "\nparameter:" + parameter.toParameterString() + "\nresponse:" + strResponse);

                    //回到主线程处理
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onResponse(new JSONObject(strResponse), null);
                            } catch (Exception e) {
                                listener.onResponse(null, e);
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static OkHttpClient getOkHttpClient(Context context, InputStream... certificates) {
        if (okHttpClient == null) {
            File sdcache = context.getExternalCacheDir();
            int cacheSize = 10 * 1024 * 1024;
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
            if (certificates != null) {
                builder.sslSocketFactory(getSSLSocketFactory(certificates));
            }
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    private static SSLSocketFactory getSSLSocketFactory(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
