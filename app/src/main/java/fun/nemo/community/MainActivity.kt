package `fun`.nemo.community

import `fun`.nemo.community.utils.Constants.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWebView()
        initRefresh()
    }

    private fun initRefresh() {
        fab_refresh.setOnClickListener { webView.reload() }
    }

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun initWebView() {
        webView.loadUrl(HOST_URL)
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