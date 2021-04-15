package `fun`.nemo.community.webview

import `fun`.nemo.community.R
import `fun`.nemo.community.interfaces.MJavascriptInterface
import `fun`.nemo.community.utils.Constants
import `fun`.nemo.community.utils.LogUtil
import `fun`.nemo.community.utils.StatusBarUtil
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.*

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/15$ 18:14$
 */
class X5WebView : WebView {

    private val mContext: Context


    init {
        initWebView()
        initWebViewClient()
    }

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, var2: AttributeSet) : super(context, var2) {
        mContext = context
    }


    private fun initWebView() {
        val webSettings = settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSettings.setSupportMultipleWindows(true)
        webSettings.setAppCacheEnabled(true)
        webSettings.domStorageEnabled = true
        webSettings.setGeolocationEnabled(true)
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE)
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        addJavascriptInterface(MJavascriptInterface(mContext), "imagelistener")
        isHorizontalFadingEdgeEnabled = false
        isVerticalScrollBarEnabled = false


    }

    private fun initWebViewClient() {
        webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                view?.settings?.blockNetworkImage = true
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String) {
                view?.settings?.blockNetworkImage = false
                changeStatusBarColor(url)
                synchronizeCookie(view, url)
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, p1: String?): Boolean {
                if (url.startsWith(Constants.HOST_URL)) {
                    view?.loadUrl(url)
                    return true
                }
                openOurWebView(url)
                return true
            }


            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request?.url.toString().contains(Constants.HOST_URL)) {
                        view?.loadUrl(request?.url.toString())
                        return true
                    } else {
                        openOurWebView(request?.url.toString())
                    }
                }
                return true
            }
        }
    }

    private fun synchronizeCookie(view: WebView?, url: String) {
        // 注入js
        if (url.startsWith(Constants.ARTICLE_URL)) {
            view?.loadUrl(Constants.INJECTION_JS)
        }
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        val endCookie = cookieManager.getCookie(url)
        LogUtil.e("endCookie:$endCookie")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync()
        } else {
            cookieManager.flush()
        }
    }


    private fun openOurWebView(url: String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        mContext.startActivity(intent)
    }

    private fun changeStatusBarColor(url: String) {
        Log.e("X5WebView", "url:$url")
        var isWhite = false
        for (whiteUrl in Constants.WHITE_URL) {
            if (url.startsWith(whiteUrl)) {
                isWhite = true
                break
            }
        }
        if (isWhite) {
            StatusBarUtil.setStatusBarMode(mContext as Activity, true, R.color.white)
        } else {
            StatusBarUtil.setStatusBarMode(mContext as Activity, false, R.color.purple_700)
        }
    }

}