package `fun`.nemo.community

import `fun`.nemo.community.adapter.PreviewAdapter
import `fun`.nemo.community.utils.DownImageUtil
import `fun`.nemo.community.utils.LogUtil
import `fun`.nemo.community.utils.StatusBarUtil
import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_image_preview.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class ImagePreviewActivity : AppCompatActivity() {
    private val tag = "ImagePreviewActivity"
    private val mImageUrls: MutableList<String> = Collections.synchronizedList(ArrayList())
    private var mIndex = 0
    private var mDownUrl: String? = null

    companion object {
        private const val WRITE_EXTERNAL_STORAGE_CODE = 666
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        StatusBarUtil.setStatusBarMode(this, false, R.color.black)
        initData()
        intView()
    }

    private fun intView() {
        viewPager.adapter = PreviewAdapter(mImageUrls)
        viewPager.currentItem = mIndex
        updatePreviewNum(mIndex)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                updatePreviewNum(position)
                mIndex = position
            }
        })
        bt_down.setOnClickListener {
            mDownUrl = mImageUrls[mIndex]
            saveImageToLocal()
        }
    }

    private fun updatePreviewNum(index: Int) {
        val showNum = "${index + 1}/${mImageUrls.size}"
        tv_num.text = showNum
    }

    private fun initData() {
        val intent = intent
        if (intent != null) {
            val imageUrls = intent.getStringArrayListExtra("imageUrls")
            if (imageUrls != null) {
                mImageUrls.addAll(imageUrls)
            }
            mIndex = intent.getIntExtra("index", 0)
            if (mIndex < 0) {
                mIndex = 0
            }
        }
    }

    @AfterPermissionGranted(WRITE_EXTERNAL_STORAGE_CODE)
    private fun saveImageToLocal() {
        val perms = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            DownImageUtil.saveImgToLocal(this, mDownUrl)
        } else {
            EasyPermissions.requestPermissions(
                this, "", WRITE_EXTERNAL_STORAGE_CODE, *perms
            )
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


}