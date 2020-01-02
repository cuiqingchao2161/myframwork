package com.cui.mvvmdemo.demos.lottie_animation_view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.airbnb.lottie.LottieComposition

import com.cui.mvvmdemo.R
import com.cui.mvvmdemo.navigation.Navigator
import com.cui.mvvmdemo.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_lottie_demo_layout.*
import java.io.File
import com.airbnb.lottie.ImageAssetDelegate
import com.cui.mvvmdemo.constant.MainConstant.CHOOSE_IMAGES_REQUEST_CODE
import com.cui.mvvmdemo.constant.MainConstant.CHOOSE_JSON_REQUEST_CODE
import java.io.FileInputStream
import java.io.FileNotFoundException


/**
 * description : TODO:类的作用
 * author : cuiqingchao
 * date : 2019/11/30 17:56
 */
class LottieDemoActivity : BaseActivity() {
    var mJsonPath:String = ""
    var mImagesPath:String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_lottie_demo_layout
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun initListener() {
        btn_select_json_file.setOnClickListener {
            chooseJson()
        }
        btn_select_images_file.setOnClickListener {
            chooseImages()
        }

        btn_start_load.setOnClickListener {
            loadRes()
        }
    }

    override fun requestData() {
    }

    override fun refreshView() {

    }

    /**
     * 从手机上选择图片
     */
    private fun chooseJson() {
        Log.i(TAG, "chooseFromAlbum: ")
        Navigator.navigateToFilePicker(this,CHOOSE_JSON_REQUEST_CODE)
    }

    /**
     * 从手机上选择图片
     */
    private fun chooseImages() {
        Log.i(TAG, "chooseFromAlbum: ")
        Navigator.navigateToFilePicker(this,CHOOSE_IMAGES_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CHOOSE_JSON_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    var jsonPath = data?.getStringExtra("path")
                    Log.i(TAG, "jsonPath:$jsonPath")
                    jsonPath?.let {
                        tv_file_json_path.text = "json path:$jsonPath"
                        var file = File(jsonPath)
                        file?.let {
                            mJsonPath = jsonPath
                        }
                    }
                }
            }
            CHOOSE_IMAGES_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    var imgPath = data?.getStringExtra("path")
                    Log.i(TAG, "imgPath:$imgPath")
                    imgPath?.let {
                        tv_file_images_path.text = "images path:$imgPath"
                        var file = File(imgPath)
                        file?.let {
                            if(file.isFile){
                                mImagesPath = file.parent
                            }else{
                                mImagesPath = imgPath
                            }
                        }
                    }
                }
            }
        }
    }


    private fun loadRes(){
        var JSON_FILE = File(mJsonPath)
        var IMAGES_FILES  = File(mImagesPath)
        var fis: FileInputStream? = null
        if (JSON_FILE.exists()) {
            try {
                fis = FileInputStream(JSON_FILE)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }
        if (fis == null) {
            Log.i(TAG, "JSON文件不存在")
            return
        }

        if (!IMAGES_FILES.exists()) {
            Log.i(TAG, "文件不存在")
        }else{
            val absolutePath = IMAGES_FILES.getAbsolutePath()
            // 开启硬件加速
//        lottieAnimationView.useHardwareAcceleration(true)
            // 设置动画文件夹代理类
            lottieAnimationView.setImageAssetDelegate(ImageAssetDelegate { asset ->
                val opts = BitmapFactory.Options()
                opts.inScaled = true
//            opts.inDensity = UtilPhoneParam.densityDpi
                var bitmap: Bitmap? = null
                try {
                    bitmap = BitmapFactory.decodeFile(absolutePath + File.separator + asset.fileName, opts)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                bitmap
            })
        }

        // 设置动画
        LottieComposition.Factory.fromInputStream(fis) { composition ->
            composition?.let {
                lottieAnimationView.setComposition(composition!!)
                lottieAnimationView.playAnimation()
            }
        }
    }
}
