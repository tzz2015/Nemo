package `fun`.nemo.community

import `fun`.nemo.community.utils.Constants.*
import `fun`.nemo.community.utils.LogUtil
import `fun`.nemo.community.webview.X5WebView
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWebView()
        initRefresh()
    }


    companion object {
        private const val WRITE_EXTERNAL_STORAGE_CODE = 666
    }


    private fun initRefresh() {
        fab_refresh.setOnClickListener { webView.reload() }
    }

    @SuppressLint("SetJavaScriptEnabled", "AddJavascriptInterface")
    private fun initWebView() {
        webView.loadUrl(HOST_URL)
        webView.setWebViewCallBack(object : X5WebView.WebViewCallBack {
            override fun openPhotoSelect() {
                openAlbum()
            }

        })
    }


    /**
     * 打开相册
     */
    @AfterPermissionGranted(WRITE_EXTERNAL_STORAGE_CODE)
    private fun openAlbum() {
        val perms = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "image/*"
            startActivityForResult(Intent.createChooser(i, "选择相册"), 102)
        } else {
            EasyPermissions.requestPermissions(
                this, "", WRITE_EXTERNAL_STORAGE_CODE, *perms
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 102) {
            if (data != null && data.data != null) {
                webView.uploadFile(data.data)
            }
        }
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        } catch (e: Exception) {
            LogUtil.e(e.message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

}