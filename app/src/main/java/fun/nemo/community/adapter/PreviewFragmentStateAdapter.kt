package `fun`.nemo.community.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
　　* @Description:
　　* @author 刘宇飞
　　* @date  2021/4/18 10:39
　　*/
class PreviewFragmentStateAdapter (fragmentActivity: FragmentActivity, private val list: List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
