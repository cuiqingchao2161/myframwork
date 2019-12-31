package com.cui.mvvmdemo.filebrowser.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.demos.threed_gallery.ZoomOutPageTransformer
import com.cui.mvvmdemo.filebrowser.entity.FileEntity
import com.cui.mvvmdemo.listener.NavigationFinishClickListener
import com.cui.mvvmdemo.widgets.ZoomImageView
import com.hiscene.presentation.filebrowser.ui.FileReceiveActivity
import com.hiscene.presentation.ui.base.BaseActivity
import kotlinx.android.synthetic.main.image_details.*
import kotlinx.android.synthetic.main.include_toolbar_layout.*

/**
 * 查看大图的Activity界面。
 *
 * @author xujiangang
 */
class ImageDetailsActivity : BaseActivity(), ViewPager.OnPageChangeListener {
    private var mFileInfos: ArrayList<FileEntity> = ArrayList()
    override fun getLayoutId(): Int {
        return R.layout.image_details
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val flag = WindowManager.LayoutParams.FLAG_FULLSCREEN
        // 获得当前窗体对象
        val window = window
        // 设置当前窗体为全屏显示
        window.setFlags(flag, flag)
        super.onCreate(savedInstanceState)
    }
    override fun initView() {
        var imagePosition = intent.getIntExtra("image_position", 0)
        mFileInfos.clear()
        for(index in FileReceiveActivity.mFileInfos.indices){
            var fileInfo = FileReceiveActivity.mFileInfos[index]

            fileInfo.let {
                if(fileInfo.fileType != null && fileInfo.fileType.title == "IMG"){
                    mFileInfos.add(fileInfo)
                    if(index == imagePosition){
                        imagePosition = mFileInfos.size - 1
                    }
                }
            }
        }

        val adapter = ViewPagerAdapter()
        view_pager!!.adapter = adapter
        view_pager!!.setPageTransformer(true, ZoomOutPageTransformer())
        view_pager!!.currentItem = imagePosition
        view_pager!!.setOnPageChangeListener(this)
        view_pager!!.isEnabled = false
        // 设定当前的页数和总页数
        page_text!!.text = (imagePosition + 1).toString() + "/" + mFileInfos.size
        tvToolbarTitle.text = mFileInfos[imagePosition].file.name
    }

    override fun initData() {
//        AutoSizeConfig.getInstance().isBaseOnWidth = false
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//        AutoSizeConfig.getInstance().restart()
    }

    override fun initListener() {
        ivToolbarNavigation.setOnClickListener(NavigationFinishClickListener(this))
    }

    override fun requestData() {
    }

    override fun refreshView() {
    }

    /**
     * ViewPager的适配器
     *
     * @author guolin
     */
    internal inner class ViewPagerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return mFileInfos.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var bitmap: Bitmap? = BitmapFactory.decodeFile(mFileInfos[position].path)
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(resources,
                        R.mipmap.default_portrait)
            }
            val view = LayoutInflater.from(this@ImageDetailsActivity).inflate(
                    R.layout.zoom_image_layout, null)
            val zoomImageView = view.findViewById<View>(R.id.zoom_image_view) as ZoomImageView
            zoomImageView.setImageBitmap(bitmap)
            container.addView(view)
            return view
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }

    }

    override fun onPageScrollStateChanged(arg0: Int) {

    }

    override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {

    }

    override fun onPageSelected(currentPage: Int) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        page_text!!.text = (currentPage + 1).toString() + "/" + mFileInfos.size

        tvToolbarTitle.text = mFileInfos[currentPage].file.name
    }

}