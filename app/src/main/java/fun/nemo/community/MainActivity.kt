package `fun`.nemo.community

import `fun`.nemo.community.utils.StatusBarUtil
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val mWhiteUrl =
        arrayOf(
            "https://www.link-nemo.com/u/",
            "https://www.link-nemo.com/my/article/category",
            "https://www.link-nemo.com/personal/setting",
            "https://www.link-nemo.com/moderator/dashboard",
            "https://www.link-nemo.com/note"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWebView()
        initRefresh()
    }

    private fun initRefresh() {
        fab_refresh.setOnClickListener { webView.reload() }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.loadUrl("https://www.link-nemo.com/")
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小 
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT //关闭webview中缓存 
        webSettings.allowFileAccess = true //设置可以访问文件 
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口 
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String) {
                changeStatusBarColor(url)
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("https://www.link-nemo.com")) {
                    view.loadUrl(url)
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
                    if (request?.url.toString().contains("www.link-nemo.com")) {
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

    private fun changeStatusBarColor(url: String) {
        Log.e("url", "url:$url")
        var isWhite = false
        for (whiteUrl in mWhiteUrl) {
            if (url.startsWith(whiteUrl)) {
                isWhite = true
                break
            }
        }
        if (isWhite) {
            StatusBarUtil.setStatusBarMode(this, true, R.color.white)
        } else {
            StatusBarUtil.setStatusBarMode(this, false, R.color.purple_700)
        }
    }

    private fun openOurWebView(url: String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        startActivity(intent)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack()
            true
        } else {
            moveTaskToBack(true)
            false
        }
    }

}