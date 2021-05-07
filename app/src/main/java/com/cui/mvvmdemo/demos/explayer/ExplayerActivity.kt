package com.cui.mvvmdemo.demos.explayer

import com.cui.mvvmdemo.R
import com.cui.lib.base.BaseActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import kotlinx.android.synthetic.main.activity_explayer.*


class ExplayerActivity : BaseActivity() {
    val defaultUrl = "ss"
    lateinit var player : ExoPlayer
    lateinit var trackSelector : DefaultTrackSelector

    override fun getLayoutId(): Int {
        return R.layout.activity_explayer
    }

    override fun initView() {
        explayer_et.setText(defaultUrl)
    }

    override fun initData() {
        initializePlayer()
    }

    override fun initListener() {}

    override fun requestData() {}

    override fun refreshView() {}

    /**
     * 初始化播放器
     */
    private fun initializePlayer() {
//        if (!this::player.isInitialized) {
//            val adaptiveTrackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory(BANDWIDTH_METER)
//            trackSelector = DefaultTrackSelector(adaptiveTrackSelectionFactory)
//            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
//            player.addListener(this)
//            mSimpleExoPlayerView.setPlayer(player)
//            //设置自动播放
//            player.playWhenReady = mShouldAutoPlay
//            val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this,
//                    Util.getUserAgent(this, "Multimedia"))
//            val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
//            //TODO...更换*.mp4文件地址
//            val videoSource: MediaSource = ExtractorMediaSource(Uri.parse("http://xxxxxx.xxxxxx.mp4"), dataSourceFactory, extractorsFactory, mainHandler,
//                    ExtractorMediaSource.EventListener { error -> Log.e(TAG, "onLoadError: " + error.getMessage()) })
//            val haveResumePosition = resumeWindow !== C.INDEX_UNSET
//            if (haveResumePosition) {
//                player.seekTo(resumeWindow, resumePosition)
//            }
//            player.prepare(videoSource)
//        }
    }

    /**
     * 释放播放器
     */
    private fun releasePlayer() {
//        if (player != null) {
//            updateResumePosition()
//            player.release()
//            player = null
//            trackSelector = null
//        }
    }
}