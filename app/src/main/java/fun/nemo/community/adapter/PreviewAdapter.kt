package `fun`.nemo.community.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView

/**
 * @description:
 * @author: 刘宇飞
 * @date :  2021/4/14$ 11:05$
 */
class PreviewAdapter(private val imageUrl: MutableList<String>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val url = imageUrl[position]
        val photoView = PhotoView(container.context)
        photoView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        Glide.with(container.context).load(url).into(photoView)
        container.addView(
            photoView,
            ViewPager.LayoutParams.MATCH_PARENT,
            ViewPager.LayoutParams.MATCH_PARENT
        );
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

}