package `fun`.nemo.community.fragment

import `fun`.nemo.community.R
import `fun`.nemo.community.utils.Constants
import `fun`.nemo.community.utils.DownImageUtil
import `fun`.nemo.community.utils.LogUtil
import `fun`.nemo.community.utils.ScreenUtils
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_preview.*

/**
　　* @Description:
　　* @author 刘宇飞
　　* @date  2021/4/18 10:41
　　*/
class PreviewFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photo_view.post { loadImage() }
    }


    private fun loadImage() {
        val url = arguments?.getString(Constants.BUNDLE_URL)
        if (DownImageUtil.isExistLocalFile(url)) {
            Glide.with(this)
                .load(url)
                .into(photo_view)
        } else {
            Glide.with(this)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        LogUtil.e("加载完毕：$url")
                        return false
                    }

                })
                .into(photo_view)

        }
    }
}