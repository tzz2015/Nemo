package `fun`.nemo.community.adapter

import `fun`.nemo.community.utils.DownImageUtil
import `fun`.nemo.community.utils.LogUtil
import `fun`.nemo.community.utils.ScreenUtils
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.chrisbanes.photoview.PhotoView
import java.util.*


/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/14$ 11:05$
 */
class PreviewAdapter(private val imageUrl: MutableList<String>) : PagerAdapter() {


    private val mMap: HashMap<String, PhotoView> = HashMap()

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var url = imageUrl[position]
        var photoView: PhotoView? = mMap[url]
        if (photoView == null) {
            photoView = PhotoView(container.context)
            photoView.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            if (DownImageUtil.isExistLocalFile(url)) {
                url = DownImageUtil.getSavePathName(url)
                Glide.with(container.context)
                    .load(url)
                    .into(photoView)
            } else {
                photoView.layoutParams = LinearLayout.LayoutParams(
                    ScreenUtils.px2dip(container.context, 10f),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                Glide.with(container.context)
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
                            photoView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                            photoView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                            notifyDataSetChanged()
                            return false
                        }

                    })
                    .into(photoView)

            }
            mMap[url] = photoView
        }


        container.addView(photoView)
        return photoView
    }

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return imageUrl.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

}