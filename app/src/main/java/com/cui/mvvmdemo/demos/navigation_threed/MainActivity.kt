package com.cui.mvvmdemo.demos.navigation_threed

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.demos.threed_gallery.MainActivity
import com.cui.mvvmdemo.demos.threed_gallery.Rotate3dAnimation
import com.cui.mvvmdemo.utils.DrawableUtil
import com.cui.mvvmdemo.widgets.MainViewPagerAdapter
import com.hiscene.presentation.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main_layout.*

/**
 * @author cuiqingchao
 * @date 2019/9/18
 */
class MainActivity : BaseActivity() {
    private val FRAGMENT_INDEX_MSG = 0
    private val FRAGMENT_INDEX_CONTACT = 1
    private val FRAGMENT_INDEX_WORKBENCH = 2
    private val FRAGMENT_INDEX_MORE = 3

    private lateinit var mContent: BaseFragment
    private lateinit var mMsgFragment: MsgFragment
    private lateinit var mContactFragment: ContactFragment
    private lateinit var mWorkbenchFragment: BaseFragment
    private lateinit var mMoreFragment: BaseFragment
    private lateinit var mPagerAdapter: MainViewPagerAdapter


    private var centerX: Int = 0
    private var centerY: Int = 0
    private val depthZ = 400
    private val duration = 600
    private var openAnimation: Rotate3dAnimation? = null
    private var closeAnimation: Rotate3dAnimation? = null
    private var isOpen = false

    override fun getLayoutId(): Int {
        return R.layout.activity_main_layout
    }

    override fun initView() {
        TAG = "Mainactivity"
        main_viewPager.offscreenPageLimit = 4
    }

    var isTabClick = false
    override fun initListener() {
        main_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                isTabClick = true
                checkTabId(position)
                isTabClick = false
                mContent = mPagerAdapter.getItem(position)
            }
        })


        main_tab_rg.setOnCheckedChangeListener { _, checkedId ->
            if(isTabClick){
                return@setOnCheckedChangeListener
            }
            when (checkedId) {
                R.id.rbtn_main_msg_tab -> {
                    main_viewPager.setCurrentItem(FRAGMENT_INDEX_MSG,false)
                }
                R.id.rbtn_main_contacts_tab -> {
                    main_viewPager.setCurrentItem(FRAGMENT_INDEX_CONTACT,false)
                }
                R.id.rbtn_main_workbench_tab -> {
                    main_viewPager.setCurrentItem(FRAGMENT_INDEX_WORKBENCH,false)
                }
                R.id.rbtn_main_more_tab -> {
                    main_viewPager.setCurrentItem(FRAGMENT_INDEX_MORE,false)
                }
            }
            mContent = mPagerAdapter.getItem(main_viewPager.currentItem)
        }
    }

    override fun initData() {
        initTabContent()
        mMsgFragment = MsgFragment.newInstance()
        mContactFragment = ContactFragment.newInstance()
        mWorkbenchFragment = WorkbenchFragment.newInstance()
        mMoreFragment = MoreFragment.newInstance()

        mPagerAdapter = MainViewPagerAdapter(supportFragmentManager)
        mPagerAdapter.addFragment(mMsgFragment)
        mPagerAdapter.addFragment(mContactFragment)
        mPagerAdapter.addFragment(mWorkbenchFragment)
        mPagerAdapter.addFragment(mMoreFragment)

        main_viewPager.adapter = mPagerAdapter


    }

    override fun requestData() {

    }

    override fun refreshView() {

    }

    private fun initTabContent() {
        DrawableUtil.setRadioButtonDrawableSize(rbtn_main_msg_tab, 28f, 28f, DrawableUtil.TOP)
        DrawableUtil.setRadioButtonDrawableSize(rbtn_main_contacts_tab, 28f, 28f, DrawableUtil.TOP)
        DrawableUtil.setRadioButtonDrawableSize(rbtn_main_workbench_tab, 28f, 28f, DrawableUtil.TOP)
        DrawableUtil.setRadioButtonDrawableSize(rbtn_main_more_tab, 28f, 28f, DrawableUtil.TOP)

        main_viewPager.currentItem = FRAGMENT_INDEX_MSG
        main_tab_rg.check(R.id.rbtn_main_msg_tab)
    }


    fun checkTabId(position: Int) {
        when (position) {
            0 -> main_tab_rg.check(R.id.rbtn_main_msg_tab)
            1 -> main_tab_rg.check(R.id.rbtn_main_contacts_tab)
            2 -> main_tab_rg.check(R.id.rbtn_main_workbench_tab)
            3 -> main_tab_rg.check(R.id.rbtn_main_more_tab)
        }
    }

    fun clickButton(view: View) {
        onClickView()
        var intent = Intent(this,MainActivity::class.java)
        intent.putExtra("position",main_viewPager.currentItem)
        val hileiaTransPair: androidx.core.util.Pair<View, String> = androidx.core.util.Pair(main_viewPager, "transitonName1")

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, hileiaTransPair)
        startActivityForResult(intent,MAIN_REQUESTCODE,options.toBundle())
    }

    companion object{
        val MAIN_REQUESTCODE = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            MAIN_REQUESTCODE ->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let{
                        var position = data.getIntExtra("position", main_viewPager.currentItem)
                        if(position in 0..3){
                            checkTabId(position)
                        }
                    }

                }
            }
        }
    }

    /**
     * 卡牌文本介绍打开效果：注意旋转角度
     */
    private fun initOpenAnim() {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        openAnimation = Rotate3dAnimation(0f, 90f, centerX.toFloat(), centerY.toFloat(), depthZ.toFloat(), true)
        openAnimation!!.setDuration(duration.toLong())
        openAnimation!!.setFillAfter(true)
        openAnimation!!.setInterpolator(AccelerateInterpolator())
        openAnimation!!.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {

                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                val rotateAnimation = Rotate3dAnimation(270f, 360f, centerX.toFloat(),
                        centerY.toFloat(), depthZ.toFloat(), false)
                rotateAnimation.duration = duration.toLong()
                rotateAnimation.fillAfter = true
                rotateAnimation.interpolator = DecelerateInterpolator()
                mContentRl.startAnimation(rotateAnimation)
            }
        })
    }

    /**
     * 卡牌文本介绍关闭效果：旋转角度与打开时逆行即可
     */
    private fun initCloseAnim() {
        closeAnimation = Rotate3dAnimation(360f, 270f, centerX.toFloat(), centerY.toFloat(), depthZ.toFloat(), true)
        closeAnimation!!.setDuration(duration.toLong())
        closeAnimation!!.setFillAfter(true)
        closeAnimation!!.setInterpolator(AccelerateInterpolator())
        closeAnimation!!.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                val rotateAnimation = Rotate3dAnimation(90f, 0f, centerX.toFloat(),
                        centerY.toFloat(), depthZ.toFloat(), false)
                rotateAnimation.duration = duration.toLong()
                rotateAnimation.fillAfter = true
                rotateAnimation.interpolator = DecelerateInterpolator()
                mContentRl.startAnimation(rotateAnimation)
            }
        })
    }

    fun onClickView() {
        //以旋转对象的中心点为旋转中心点，这里主要不要再onCreate方法中获取，因为视图初始绘制时，获取的宽高为0
        centerX = mContentRl.getWidth() / 2
        centerY = mContentRl.getHeight() / 2
        if (openAnimation == null) {
            initOpenAnim()
            initCloseAnim()
        }

        //用作判断当前点击事件发生时动画是否正在执行
        if (openAnimation!!.hasStarted() && !openAnimation!!.hasEnded()) {
            return
        }
        if (closeAnimation!!.hasStarted() && !closeAnimation!!.hasEnded()) {
            return
        }

        //判断动画执行
        if (isOpen) {
            mContentRl.startAnimation(closeAnimation)

        } else {

            mContentRl.startAnimation(openAnimation)
        }

        isOpen = !isOpen
    }
}
