package com.zx.mvvmdemo.module

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.AttributeSet
import android.view.MotionEvent

import com.zx.mvvmdemo.base.MyApplication

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.util.concurrent.Executors

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * View class for photo display
 */
class GLPhotoView
/**
 * Constructor
 * @param context Context
 * @param attrs Argument for resource
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    @Volatile var mAngleX = 0f// 摄像机所在的x坐标
    @Volatile var mAngleY = 0f// 摄像机所在的y坐标
    @Volatile var mAngleZ = 3f// 摄像机所在的z坐标

    internal var startRawX: Float = 0.toFloat()
    internal var startRawY: Float = 0.toFloat()

    internal var xFlingAngle: Double = 0.toDouble()
    internal var xFlingAngleTemp: Double = 0.toDouble()

    internal var yFlingAngle: Double = 0.toDouble()
    internal var yFlingAngleTemp: Double = 0.toDouble()

    var VL = "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec2 a_texCoord;" +
            "varying vec2 v_texCoord;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "  v_texCoord = a_texCoord;" +
            "}"
    var FL = "precision mediump float;" +
            "varying vec2 v_texCoord;" +
            "uniform sampler2D s_texture;" +
            "void main() {" +
            "  gl_FragColor = texture2D( s_texture, v_texCoord );" +
            "}"
    @Volatile
    private var cancel = false

    @Volatile
    private var isPlaying = false

    @Volatile
    private var  mRunnable : Runnable = MyRunnable()

    @Synchronized
    fun play() {
        synchronized(isPlaying){
            if(isPlaying){
                return
            }
            cancel = false

            Executors.newCachedThreadPool().execute (mRunnable)
        }
    }

    inner class MyRunnable : Runnable{
        override fun run() {
            isPlaying = true
            while (!cancel) {
                rotate(0.001f)
            }
            isPlaying = false
        }

    }
    @Synchronized
    fun cancel() {
        cancel = true
    }

    fun setRenderer() {
        super.setRenderer(MyGLRender())
    }

    @Synchronized
    fun rotate(distance: Float) {
        var distanceX = distance
        val distanceY = 0f
        yFlingAngleTemp = distanceY * 180 / (Math.PI * 3)
        if (yFlingAngleTemp + yFlingAngle > Math.PI / 2) {
            yFlingAngleTemp = Math.PI / 2 - yFlingAngle
        }
        if (yFlingAngleTemp + yFlingAngle < -Math.PI / 2) {
            yFlingAngleTemp = -Math.PI / 2 - yFlingAngle
        }
        //这里的0.1f是为了不上摄像机移动的过快
        //            distanceX = 0.1f * (-distanceX) / activity.getWindowManager().getDefaultDisplay().getWidth();
        distanceX = 0.1f * -distanceX / 1920
        xFlingAngleTemp = distanceX * 180 / (Math.PI * 3)

        mAngleX = (3.0 * Math.cos(yFlingAngle + yFlingAngleTemp) * Math.sin(xFlingAngle + xFlingAngleTemp)).toFloat()
        mAngleY = (3 * Math.sin(yFlingAngle + yFlingAngleTemp)).toFloat()
        mAngleZ = (3.0 * Math.cos(yFlingAngle + yFlingAngleTemp) * Math.cos(xFlingAngle + xFlingAngleTemp)).toFloat()

        requestRender()

        xFlingAngle += xFlingAngleTemp
        yFlingAngle += yFlingAngleTemp
    }

    override fun onTouchEvent(me: MotionEvent): Boolean {
        //处理手指滑动事件，我这里的处理是判断手指在横向和竖向滑动的距离
        //这个距离隐射到球体上经度和纬度的距离，根据这个距离计算三维空间的两个
        //夹角，根据这个夹角调整摄像机所在位置
        if (me.action == MotionEvent.ACTION_DOWN) {
            startRawX = me.rawX
            startRawY = me.rawY
        } else if (me.action == MotionEvent.ACTION_MOVE) {

            var distanceX = startRawX - me.rawX
            var distanceY = startRawY - me.rawY

            //这里的0.1f是为了不上摄像机移动的过快
            //            distanceY = 0.1f * (distanceY) / activity.getWindowManager().getDefaultDisplay().getHeight();
            distanceY = 0.1f * distanceY / 1080

            yFlingAngleTemp = distanceY * 180 / (Math.PI * 3)

            if (yFlingAngleTemp + yFlingAngle > Math.PI / 2) {
                yFlingAngleTemp = Math.PI / 2 - yFlingAngle
            }
            if (yFlingAngleTemp + yFlingAngle < -Math.PI / 2) {
                yFlingAngleTemp = -Math.PI / 2 - yFlingAngle
            }
            //这里的0.1f是为了不上摄像机移动的过快
            //            distanceX = 0.1f * (-distanceX) / activity.getWindowManager().getDefaultDisplay().getWidth();
            distanceX = 0.1f * -distanceX / 1920
            xFlingAngleTemp = distanceX * 180 / (Math.PI * 3)


            mAngleX = (3.0 * Math.cos(yFlingAngle + yFlingAngleTemp) * Math.sin(xFlingAngle + xFlingAngleTemp)).toFloat()

            mAngleY = (3 * Math.sin(yFlingAngle + yFlingAngleTemp)).toFloat()


            mAngleZ = (3.0 * Math.cos(yFlingAngle + yFlingAngleTemp) * Math.cos(xFlingAngle + xFlingAngleTemp)).toFloat()

            requestRender()
        } else if (me.action == MotionEvent.ACTION_UP) {
            xFlingAngle += xFlingAngleTemp
            yFlingAngle += yFlingAngleTemp
        }
        return true
    }


    inner class MyGLRender : GLSurfaceView.Renderer {
        internal var verticalsBuffer: FloatBuffer

        internal var CAP = 9//绘制球体时，每次增加的角度
        internal var verticals = FloatArray(180 / CAP * (360 / CAP) * 6 * 3)


        private val mUvTexVertexBuffer: FloatBuffer

        private val UV_TEX_VERTEX = FloatArray(180 / CAP * (360 / CAP) * 6 * 2)

        private var mProgram: Int = 0
        private var mPositionHandle: Int = 0
        private var mTexCoordHandle: Int = 0
        private var mMatrixHandle: Int = 0
        private var mTexSamplerHandle: Int = 0
        internal var mTexNames: IntArray? = null

        private val mProjectionMatrix = FloatArray(16)
        private val mCameraMatrix = FloatArray(16)
        private val mMVPMatrix = FloatArray(16)

        private var mWidth: Int = 0
        private var mHeight: Int = 0

        init {

            val x = 0f
            val y = 0f
            val z = 0f

            val r = 3f//球体半径
            var index = 0
            var index1 = 0
            val d = CAP * Math.PI / 180//每次递增的弧度
            var i = 0
            while (i < 180) {
                val d1 = i * Math.PI / 180
                var j = 0
                while (j < 360) {
                    //获得球体上切分的超小片矩形的顶点坐标（两个三角形组成，所以有六点顶点）
                    val d2 = j * Math.PI / 180
                    verticals[index++] = (x + r.toDouble() * Math.sin(d1 + d) * Math.cos(d2 + d)).toFloat()
                    verticals[index++] = (y + r * Math.cos(d1 + d)).toFloat()
                    verticals[index++] = (z + r.toDouble() * Math.sin(d1 + d) * Math.sin(d2 + d)).toFloat()
                    //获得球体上切分的超小片三角形的纹理坐标
                    UV_TEX_VERTEX[index1++] = (j + CAP) * 1f / 360
                    UV_TEX_VERTEX[index1++] = (i + CAP) * 1f / 180

                    verticals[index++] = (x + r.toDouble() * Math.sin(d1) * Math.cos(d2)).toFloat()
                    verticals[index++] = (y + r * Math.cos(d1)).toFloat()
                    verticals[index++] = (z + r.toDouble() * Math.sin(d1) * Math.sin(d2)).toFloat()

                    UV_TEX_VERTEX[index1++] = j * 1f / 360
                    UV_TEX_VERTEX[index1++] = i * 1f / 180

                    verticals[index++] = (x + r.toDouble() * Math.sin(d1) * Math.cos(d2 + d)).toFloat()
                    verticals[index++] = (y + r * Math.cos(d1)).toFloat()
                    verticals[index++] = (z + r.toDouble() * Math.sin(d1) * Math.sin(d2 + d)).toFloat()

                    UV_TEX_VERTEX[index1++] = (j + CAP) * 1f / 360
                    UV_TEX_VERTEX[index1++] = i * 1f / 180

                    verticals[index++] = (x + r.toDouble() * Math.sin(d1 + d) * Math.cos(d2 + d)).toFloat()
                    verticals[index++] = (y + r * Math.cos(d1 + d)).toFloat()
                    verticals[index++] = (z + r.toDouble() * Math.sin(d1 + d) * Math.sin(d2 + d)).toFloat()

                    UV_TEX_VERTEX[index1++] = (j + CAP) * 1f / 360
                    UV_TEX_VERTEX[index1++] = (i + CAP) * 1f / 180

                    verticals[index++] = (x + r.toDouble() * Math.sin(d1 + d) * Math.cos(d2)).toFloat()
                    verticals[index++] = (y + r * Math.cos(d1 + d)).toFloat()
                    verticals[index++] = (z + r.toDouble() * Math.sin(d1 + d) * Math.sin(d2)).toFloat()

                    UV_TEX_VERTEX[index1++] = j * 1f / 360
                    UV_TEX_VERTEX[index1++] = (i + CAP) * 1f / 180

                    verticals[index++] = (x + r.toDouble() * Math.sin(d1) * Math.cos(d2)).toFloat()
                    verticals[index++] = (y + r * Math.cos(d1)).toFloat()
                    verticals[index++] = (z + r.toDouble() * Math.sin(d1) * Math.sin(d2)).toFloat()

                    UV_TEX_VERTEX[index1++] = j * 1f / 360
                    UV_TEX_VERTEX[index1++] = i * 1f / 180
                    j += CAP


                }
                i += CAP
            }
            verticalsBuffer = ByteBuffer.allocateDirect(verticals.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(verticals)
            verticalsBuffer.position(0)


            mUvTexVertexBuffer = ByteBuffer.allocateDirect(UV_TEX_VERTEX.size * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(UV_TEX_VERTEX)
            mUvTexVertexBuffer.position(0)
        }

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {

        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

            mWidth = width
            mHeight = height
            mProgram = GLES20.glCreateProgram()

            val vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
            GLES20.glShaderSource(vertexShader, VL)
            GLES20.glCompileShader(vertexShader)

            val fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
            GLES20.glShaderSource(fragmentShader, FL)
            GLES20.glCompileShader(fragmentShader)

            GLES20.glAttachShader(mProgram, vertexShader)
            GLES20.glAttachShader(mProgram, fragmentShader)

            GLES20.glLinkProgram(mProgram)

            mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
            mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texCoord")
            mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
            mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram, "s_texture")

            mTexNames = IntArray(1)
            GLES20.glGenTextures(1, mTexNames, 0)
            //这里的全景图需要长宽的比例使2：1，不然上下顶点会出现形变
            var bitmap: Bitmap? = null
            try {
                bitmap = BitmapFactory.decodeStream(MyApplication.getInstance().applicationContext.resources.assets.open("1110.jpg"))
            } catch (e: IOException) {
                e.printStackTrace()
            }

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexNames!![0])
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            bitmap!!.recycle()
            val ratio = height.toFloat() / width
            Matrix.frustumM(mProjectionMatrix, 0, -1f, 1f, -ratio, ratio, 3f, 7f)


        }

        override fun onDrawFrame(gl: GL10) {
            //调整摄像机焦点位置，使画面滚动
            Matrix.setLookAtM(mCameraMatrix, 0, mAngleX, mAngleY, mAngleZ, 0f, 0f, 0f, 0f, 1f, 0f)

            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mCameraMatrix, 0)

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
            GLES20.glUseProgram(mProgram)
            GLES20.glEnableVertexAttribArray(mPositionHandle)
            GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                    12, verticalsBuffer)
            GLES20.glEnableVertexAttribArray(mTexCoordHandle)
            GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0,
                    mUvTexVertexBuffer)
            GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0)
            GLES20.glUniform1i(mTexSamplerHandle, 0)


            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 180 / CAP * (360 / CAP) * 6)

            GLES20.glDisableVertexAttribArray(mPositionHandle)

        }
    }
}
/**
 * Constructor
 * @param context Context
 */