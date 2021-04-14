package `fun`.nemo.community

import `fun`.nemo.community.adapter.PreviewAdapter
import `fun`.nemo.community.utils.StatusBarUtil
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.activity_image_preview.*
import java.util.*
import kotlin.collections.ArrayList

class ImagePreviewActivity : AppCompatActivity() {
    private val tag = "ImagePreviewActivity"
    private val mImageUrls: MutableList<String> = Collections.synchronizedList(ArrayList())
    private var mIndex = 0

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
    }

    private fun initData() {
        val intent = intent
        if (intent != null) {
            val imageUrls = intent.getStringArrayListExtra("imageUrls")
            if (imageUrls != null) {
                mImageUrls.addAll(imageUrls)
            }
            mIndex = intent.getIntExtra("index", 0)
        }
    }


}