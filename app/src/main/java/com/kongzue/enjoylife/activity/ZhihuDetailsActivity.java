package com.kongzue.enjoylife.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.dialog.listener.OnMenuItemClickListener;
import com.kongzue.dialog.util.BlurView;
import com.kongzue.dialog.v2.BottomMenu;
import com.kongzue.dialog.v2.WaitDialog;
import com.kongzue.enjoylife.R;
import com.kongzue.enjoylife.adapter.ZhihuListAdapter;
import com.kongzue.enjoylife.listener.ResponseListener;
import com.kongzue.enjoylife.request.HttpRequest;
import com.kongzue.enjoylife.util.Parameter;
import com.kongzue.enjoylife.util.SwipBackActivity;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ZhihuDetailsActivity extends SwipBackActivity {

    private WebView webView;
    private RelativeLayout boxTable;
    private BlurView blur;
    private LinearLayout boxTableChild;
    private LinearLayout btnBack;
    private ImageView btnShare;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_zhihu_details);
        setTranslucentStatus(true, true);
    }

    @Override
    public void initViews() {
        webView = findViewById(R.id.webView);
        boxTable = findViewById(R.id.box_table);
        blur = findViewById(R.id.blur);
        boxTableChild = findViewById(R.id.box_table_child);
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
    }

    private ZhihuListAdapter.ZhihuBean zhihuBean;

    @Override
    public void initDatas() {
        blur.setOverlayColor(Color.argb(200, 235, 235, 235));
        blur.setRadius(me, 0, 0);
        boxTable.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(me, 50) + getStatusBarHeight()));
        webView.getView().setPadding(0, dip2px(me, 50) + me.getStatusBarHeight(), 0, 0);

        if (getParameter() == null) {
            finish();
            return;
        }
        if (getParameter().get("ZhihuBean") == null) {
            finish();
            return;
        } else {
            zhihuBean = (ZhihuListAdapter.ZhihuBean) getParameter().get("ZhihuBean");
        }
        initWebView();
        loadPage();
    }

    private void initWebView() {
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private String body;
    private String css;
    private String share_url;

    private void loadPage() {
        WaitDialog.show(me, "加载中...");
        HttpRequest.getInstance(me).getRequest("https://news-at.zhihu.com/api/4/news/" + zhihuBean.getId(), new Parameter(), new ResponseListener() {
            @Override
            public void onResponse(JSONObject main, Exception error) {
                WaitDialog.dismiss();
                try {
                    body = main.getString("body");
                    share_url = main.getString("share_url");
                    css = (String) main.getJSONArray("css").get(0);
                    webView.loadData(htmlText(body, css), "text/html; charset=utf-8", "utf-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void setEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> listMenu = new ArrayList<>();
                listMenu.add("分享此文章");
                BottomMenu.show(me, listMenu, new OnMenuItemClickListener() {
                    @Override
                    public void onClick(String text, int index) {
                        Intent share_intent = new Intent();
                        share_intent.setAction(Intent.ACTION_SEND);
                        share_intent.setType("text/plain");
                        share_intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                        share_intent.putExtra(Intent.EXTRA_TEXT, zhihuBean.getTitle() + " " +share_url + " 分享自知乎网");
                        share_intent = Intent.createChooser(share_intent, "分享");
                        startActivity(share_intent);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public static String htmlText(String htmlContnet, String htmlCss) {
        if (TextUtils.isEmpty(htmlContnet)) {
            return "";
        }
        String html = "<html>\n" +
                "<head>\n " +
                "<meta charset=\"utf-8\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                "<link href=\"" + htmlCss + "\" rel=\"stylesheet\" type=\"text/css\"/>" +
                "<style type=\"text/css\">\n " +
                "html {\n" +
                "line-height: 1.15;\n" +
                "-ms-text-size-adjust: 100%;\n" +
                "-webkit-text-size-adjust: 100%;\n" +
                "width:100%;\n" +
                "height:100%;\n" +
                "margin:0 auto;\n" +
                "padding:0;\n" +
                "}\n" +
                "body {\n" +
                "font-size:16px;\n" +
                "line-height:1.8;\n" +
                "background-color: #fff !important;\n" +
                "color:#000\n" +
                "margin: 0;\n" +
                "padding: 0 10px;\n" +
                "word-wrap: break-word;\n" +
                "outline: 0;\n" +
                "-webkit-tap-highlight-color: rgba(0,0,0,0);\n" +
                "-webkit-tap-highlight-color: transparent;\n" +
                "-webkit-touch-callout: none;\n" +
                "-webkit-user-select: none;\n" +
                "overflow-y: scroll;\n" +
                "}\n" +
                "img.image {\n" +
                "width: 100%;\n" +
                "height: auto;\n" +
                "border: 0;\n" +
                "margin: 5px 0!important;\n" +
                "}" +
                "</style>\n" +
                "<head>\n" +
                "<body>\n" +
                "" + htmlContnet + "\n" +
                "</body>\n" +
                "</html>";
        return html;
    }
}
