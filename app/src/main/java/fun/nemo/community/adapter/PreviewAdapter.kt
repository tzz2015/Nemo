package `fun`.nemo.community.adapter

import `fun`.nemo.community.R
import `fun`.nemo.community.utils.DownImageUtil
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView


/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/14$ 11:05$
 */
class PreviewAdapter(private val imageUrl: MutableList<String>) : PagerAdapter() {
    var myOptions = RequestOptions()
        .centerCrop()
        .override(360, 360)

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var url = imageUrl[position]
        Log.e(javaClass.simpleName, "position:$position url:$url")
        val photoView = PhotoView(container.context)
        if (DownImageUtil.isExistLocalFile(url)) {
            url = DownImageUtil.getSavePathName(url)
            Glide.with(container.context)
                .load(url)
                .into(photoView)
        } else {
            Glide.with(container.context)
                .asBitmap()
                .apply(myOptions)
                .load(url)
                .skipMemoryCache(true)
                .into(photoView)
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