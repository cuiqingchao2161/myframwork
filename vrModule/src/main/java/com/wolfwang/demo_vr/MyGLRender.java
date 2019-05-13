package com.wolfwang.demo_vr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * class description here
 * Created by qingchao.cui on 2019/5/10.
 */

public class MyGLRender implements GLSurfaceView.Renderer {
    FloatBuffer verticalsBuffer;

    int CAP = 9;//绘制球体时，每次增加的角度
    float[] verticals = new float[(180/CAP) * (360/CAP) * 6 * 3];


    private final FloatBuffer mUvTexVertexBuffer;

    private final float[] UV_TEX_VERTEX = new float[(180/CAP) * (360/CAP) * 6 * 2];

    private int mProgram;
    private int mPositionHandle;
    private int mTexCoordHandle;
    private int mMatrixHandle;
    private int mTexSamplerHandle;
    int[] mTexNames;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mCameraMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];

    private int mWidth;
    private int mHeight;

    public MyGLRender() {

        float x = 0;
        float y = 0;
        float z = 0;

        float r = 3;//球体半径
        int index = 0;
        int index1 = 0;
        double d = CAP * Math.PI / 180;//每次递增的弧度
        for (int i = 0; i < 180; i += CAP) {
            double d1 = i * Math.PI / 180;
            for (int j = 0; j < 360; j += CAP) {
                //获得球体上切分的超小片矩形的顶点坐标（两个三角形组成，所以有六点顶点）
                double d2 = j * Math.PI / 180;
                verticals[index++] = (float) (x + r * Math.sin(d1 + d) * Math.cos(d2 + d));
                verticals[index++] = (float) (y + r * Math.cos(d1 + d));
                verticals[index++] = (float) (z + r * Math.sin(d1 + d) * Math.sin(d2 + d));
                //获得球体上切分的超小片三角形的纹理坐标
                UV_TEX_VERTEX[index1++] = (j + CAP) * 1f / 360;
                UV_TEX_VERTEX[index1++] = (i + CAP) * 1f / 180;

                verticals[index++] = (float) (x + r * Math.sin(d1) * Math.cos(d2));
                verticals[index++] = (float) (y + r * Math.cos(d1));
                verticals[index++] = (float) (z + r * Math.sin(d1) * Math.sin(d2));

                UV_TEX_VERTEX[index1++] = j * 1f / 360;
                UV_TEX_VERTEX[index1++] = i * 1f / 180;

                verticals[index++] = (float) (x + r * Math.sin(d1) * Math.cos(d2 + d));
                verticals[index++] = (float) (y + r * Math.cos(d1));
                verticals[index++] = (float) (z + r * Math.sin(d1) * Math.sin(d2 + d));

                UV_TEX_VERTEX[index1++] = (j + CAP) * 1f / 360;
                UV_TEX_VERTEX[index1++] = i * 1f / 180;

                verticals[index++] = (float) (x + r * Math.sin(d1 + d) * Math.cos(d2 + d));
                verticals[index++] = (float) (y + r * Math.cos(d1 + d));
                verticals[index++] = (float) (z + r * Math.sin(d1 + d) * Math.sin(d2 + d));

                UV_TEX_VERTEX[index1++] = (j + CAP) * 1f / 360;
                UV_TEX_VERTEX[index1++] = (i + CAP) * 1f / 180;

                verticals[index++] = (float) (x + r * Math.sin(d1 + d) * Math.cos(d2));
                verticals[index++] = (float) (y + r * Math.cos(d1 + d));
                verticals[index++] = (float) (z + r * Math.sin(d1 + d) * Math.sin(d2));

                UV_TEX_VERTEX[index1++] = j * 1f / 360;
                UV_TEX_VERTEX[index1++] = (i + CAP) * 1f / 180;

                verticals[index++] = (float) (x + r * Math.sin(d1) * Math.cos(d2));
                verticals[index++] = (float) (y + r * Math.cos(d1));
                verticals[index++] = (float) (z + r * Math.sin(d1) * Math.sin(d2));

                UV_TEX_VERTEX[index1++] = j * 1f / 360;
                UV_TEX_VERTEX[index1++] = i * 1f / 180;


            }
        }
        verticalsBuffer = ByteBuffer.allocateDirect(verticals.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(verticals);
        verticalsBuffer.position(0);


        mUvTexVertexBuffer = ByteBuffer.allocateDirect(UV_TEX_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(UV_TEX_VERTEX);
        mUvTexVertexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        mWidth = width;
        mHeight = height;
        mProgram = GLES20.glCreateProgram();

        int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
//        GLES20.glShaderSource(vertexShader, VL);
        GLES20.glCompileShader(vertexShader);

        int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
//        GLES20.glShaderSource(fragmentShader, FL);
        GLES20.glCompileShader(fragmentShader);

        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);

        GLES20.glLinkProgram(mProgram);

        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_texCoord");
        mMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        mTexSamplerHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");

        mTexNames = new int[1];
        GLES20.glGenTextures(1, mTexNames, 0);
        //这里的全景图需要长宽的比例使2：1，不然上下顶点会出现形变
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(QfAdTvApp.getApp().getApplicationContext().getResources().getAssets().open("1110.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexNames[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        float ratio = (float) height / width;
        Matrix.frustumM(mProjectionMatrix, 0, -1, 1, -ratio, ratio, 3, 7);


    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //调整摄像机焦点位置，使画面滚动
//        Matrix.setLookAtM(mCameraMatrix, 0, OPENGLTestActivity.mAngleX, mAngleY, mAngleZ, 0, 0, 0, 0, 1, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mCameraMatrix, 0);

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                12, verticalsBuffer);
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mTexCoordHandle, 2, GLES20.GL_FLOAT, false, 0,
                mUvTexVertexBuffer);
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glUniform1i(mTexSamplerHandle, 0);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, (180/CAP) * (360/CAP) * 6);

        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
