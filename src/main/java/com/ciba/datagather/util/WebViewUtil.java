package com.ciba.datagather.util;

import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;

/**
 * @author ciba
 * @date 2018/4/3
 * @description
 */

public class WebViewUtil {
    public static void destroy(WebView webView) {
        releaseWebView(webView, false, true);
    }

    public static void destroyAndClearHistory(WebView webView) {
        releaseWebView(webView, true, true);
    }

    public static void clearWebView(WebView webView) {
        releaseWebView(webView, false, false);
    }

    public static void releaseWebView(WebView webView, boolean clearHistory, boolean release) {
        if (webView == null) {
            return;
        }
        try {
            ViewParent parent = webView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(webView);
            }
            if (clearHistory) {
                webView.clearHistory();
            }
            webView.stopLoading();
            webView.clearView();
            webView.removeAllViews();
            webView.destroy();
            if (release) {
                webView = null;
            }
        } catch (Exception e) {
            DataGatherLog.innerI(e.getMessage());
        }
    }

}
