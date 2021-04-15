package `fun`.nemo.community.webview

import `fun`.nemo.community.R
import `fun`.nemo.community.interfaces.MJavascriptInterface
import `fun`.nemo.community.utils.Constants
import `fun`.nemo.community.utils.LogUtil
import `fun`.nemo.community.utils.StatusBarUtil
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.*
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/15$ 18:14$
 */
class X5WebView : WebView {

    private val mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        initWebView()
        initWebViewClient()
    }

    constructor(context: Context, var2: AttributeSet) : super(context, var2) {
        mContext = context
        initWebView()
        initWebViewClient()
        initWebChromeClient()
    }

    private fun initWebChromeClient() {
        webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                view: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                LogUtil.e("启动文件选择器")
                return true
            }
        }
    }


    @SuppressLint("AddJavascriptInterface", "SetJavaScriptEnabled")
    private fun initWebView() {
        settings.apply {
            //设置默认编码格式为“UTF-8”
            defaultTextEncodingName = "UTF-8"
            //设置DOM存储可用
            domStorageEnabled = true
            //js可用
            javaScriptEnabled = true
            //支持js打开新窗口
            javaScriptCanOpenWindowsAutomatically = true
            //支持自动加载图片
            loadsImagesAutomatically = true
            //设置数据库存储可用
            databaseEnabled = true
            //在File域下，能够执行任意的JavaScript代码，同源策略跨域访问能够对私有目录文件进行访问等
            allowFileAccess = true
            blockNetworkImage = false
            blockNetworkLoads = false
            //设置应用缓存可用
            setAppCacheEnabled(true)
            //设置缓存路径
            setAppCachePath(context.applicationContext.getDir("cache", Context.MODE_PRIVATE).path)
            //支持页面缩放
            setSupportZoom(true)
            //缩放使用放大放小按钮控制
            builtInZoomControls = true
            //不显示边界滑动条
            displayZoomControls = false
            //使用浏览器组件，建议使用
            useWideViewPort = true
            loadWithOverviewMode = true
            //启用地理定位
            setGeolocationEnabled(true)
            //支持Http与Https混合模式
            mixedContentMode = 0
            //设置是否允许通过 file url 加载的 Js代码读取其他的本地文件（有风险，非内部网页建议关闭）
            setAllowFileAccessFromFileURLs(true)
            //设置是否允许通过 file url 加载的 Javascript 可以访问其他的源(包括http、https等源)（有风险，非内部网页建议关闭）
            setAllowUniversalAccessFromFileURLs(true)
            //调整线程优先级（一般不建议调整，默认normal）
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            //取消垂直和水平方向的滑动栏
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            //不允许弹性滑动
            overScrollMode = View.OVER_SCROLL_NEVER
            saveFormData = true
            //硬件加速
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            addJavascriptInterface(MJavascriptInterface(mContext), "imagelistener")
        }
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

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
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
                    if (request?.url.toString().contains(Constants.LAST_HOST)) {
                        view?.loadUrl(request?.url.toString())
                    } else {
                        openOurWebView(request?.url.toString())
                    }
                }
                return true
            }

            override fun onReceivedSslError(p0: WebView?, p1: SslErrorHandler?, p2: SslError?) {
                p1?.proceed()
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