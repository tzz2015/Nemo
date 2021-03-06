package `fun`.nemo.community

import `fun`.nemo.community.utils.Constants.HOST_URL
import `fun`.nemo.community.utils.LogUtil
import `fun`.nemo.community.webview.X5WebView
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import androidx.appcompat.app.AppCompatActivity
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsDownloader
import com.tencent.smtt.sdk.TbsListener
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class MainActivity : BaseActivity() {

    private var isReLoad = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWebView()
        initRefresh()
        checkStorageManagerPermission()
    }

    @AfterPermissionGranted(110)
    fun checkStorageManagerPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            initTbsListener()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "",
                110,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

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
     * ????????????
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
            startActivityForResult(Intent.createChooser(i, "????????????"), 102)
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

    private fun initTbsSdk() {
        // ?????????TBS??????????????????WebView????????????????????????
        val map: HashMap<String, Any> = HashMap()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        QbSdk.setDownloadWithoutWifi(true)
    }

    private fun initTbsListener() {
        initTbsSdk()
        QbSdk.setTbsListener(object : TbsListener {
            override fun onDownloadFinish(i: Int) {
                Log.e("app", "onDownloadFinish -->??????X5???????????????$i")
            }

            override fun onInstallFinish(i: Int) {
                Log.e("app", "onInstallFinish -->??????X5???????????????$i")
            }

            override fun onDownloadProgress(i: Int) {
                Log.e("app", "onDownloadProgress -->??????X5???????????????$i")
            }
        })
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(success: Boolean) {
                //x5?????????????????????????????????true???x5??????????????????????????????????????????????????????????????????????????????
                Log.e("app", " ???????????? $success")
                if (!success && !isReLoad) {
                    isReLoad = true
                    QbSdk.reset(application)
                    initTbsSdk()
                    TbsDownloader.startDownload(application)
                }
            }

            override fun onCoreInitFinished() {}
        }
        //x5?????????????????????
        QbSdk.initX5Environment(this, cb)
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()

    }


}