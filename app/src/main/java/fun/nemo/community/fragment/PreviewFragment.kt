package `fun`.nemo.community.fragment

import `fun`.nemo.community.R
import `fun`.nemo.community.glide.GlideLoadStatusListener
import `fun`.nemo.community.glide.GlideUtil
import `fun`.nemo.community.glide.ProgressInterceptor
import `fun`.nemo.community.utils.Constants
import `fun`.nemo.community.utils.DownImageUtil
import `fun`.nemo.community.utils.LogUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.fragment_preview.*

/**
　　* @Description:
　　* @author 刘宇飞
　　* @date  2021/4/18 10:41
　　*/
class PreviewFragment : Fragment() {
    private val mProgressView by lazy {
        KProgressHUD.create(context)
            .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
            .setLabel("加载中...")
            .setMaxProgress(100)
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

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
            mProgressView.setProgress(0)
            mProgressView.show()
            GlideUtil.load(context,url,photo_view,object :GlideLoadStatusListener{
                override fun onProgress(progress: Int) {
                    mProgressView.setProgress(progress)
                }

                override fun onLoadFailed() {
                    mProgressView.dismiss()
                    ProgressInterceptor.removeListener(url)
                }

                override fun onLoadSuccess() {
                    mProgressView.dismiss()
                    ProgressInterceptor.removeListener(url)
                    LogUtil.e("加载完毕：$url")
                }

            })
            ProgressInterceptor.addListener(url
            ) { progress -> mProgressView.setProgress(progress) }

            /*GlideApp.with(this)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        mProgressView.dismiss()
                        ProgressInterceptor.removeListener(url)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        mProgressView.dismiss()
                        ProgressInterceptor.removeListener(url)
                        LogUtil.e("加载完毕：$url")
                        return false
                    }

                })
                .into(photo_view)*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mProgressView.isShowing) {
            mProgressView.dismiss()
        }
    }
}