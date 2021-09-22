package `fun`.nemo.community

import androidx.fragment.app.FragmentActivity
import com.umeng.analytics.MobclickAgent

/**
 * @description:
 * @author:  刘宇飞
 * @date :   2021/9/22 9:38
 */
abstract class BaseActivity : FragmentActivity() {
    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(javaClass.name)
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(javaClass.name)
        MobclickAgent.onPause(this)
    }
}