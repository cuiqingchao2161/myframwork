package com.cui.mvvmdemo.widgets

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.hiscene.presentation.ui.base.BaseFragment
import java.util.*

/*
* 主页TAB适配器
* FragmentPagerAdapter刷新fragment最完美解决方案
*/
internal class MainViewPagerAdapter(var fm: FragmentManager) : FragmentPagerAdapter(fm) {
    companion object {
        private val TAG = "MainViewPagerAdapter"
    }

    private var fragments: MutableList<BaseFragment> = ArrayList()
    private var fragmentsUpdateFlag: MutableList<Boolean> = ArrayList()

    fun setFragments(fragments:ArrayList<BaseFragment>){
        this.fragments = fragments
        for (f in fragments){
            this.fragmentsUpdateFlag.add(false)
        }
    }

    fun addFragment(fragment: BaseFragment){
        this.fragments.add(fragment)
        this.fragmentsUpdateFlag.add(false)
    }


    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): BaseFragment {
        val fragment = fragments[position % fragments.size]
        Log.i(TAG, "getItem:position=" + position + ",fragment:"
                + fragment.javaClass.name + ",fragment.tag="
                + fragment.tag)
        return fragments[position % fragments.size]
    }


    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //得到缓存的fragment
        var fragment = super.instantiateItem(container, position) as BaseFragment
        //得到tag，这点很重要
        val fragmentTag = fragment.tag

        if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.size]) {
            //如果这个fragment需要更新

            val ft = fm.beginTransaction()
            //移除旧的fragment
            ft.remove(fragment)
            //换成新的fragment
            fragment = fragments[position % fragments.size]
            //添加新fragment时必须用前面获得的tag，这点很重要
            ft.add(container.id, fragment, fragmentTag)
            ft.attach(fragment)
            ft.commit()

            //复位更新标志
            fragmentsUpdateFlag[position % fragmentsUpdateFlag.size] = false
        }

        return fragment
    }

}
