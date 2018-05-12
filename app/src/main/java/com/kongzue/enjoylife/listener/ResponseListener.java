package com.kongzue.enjoylife.listener;

import org.json.JSONObject;

/**
 * Created by myzcx on 2017/12/27.
 */

public interface ResponseListener {
    void onResponse(JSONObject main, Exception error);
}