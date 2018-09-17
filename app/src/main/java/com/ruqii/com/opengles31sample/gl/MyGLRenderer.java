package com.ruqii.com.opengles31sample.gl;

import android.opengl.GLES31;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/9/17.
 * email:huangruqi88@163.com
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {


    /**
     * 在View的OpenGL环境被创建的时候调用。
     * @param gl10
     * @param eglConfig
     */
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES31.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    }

    /**
     * 如果视图的几何形状发生变化（例如，当设备的屏幕方向改变时），则调用此方法
     * @param gl10
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES31.glViewport(0, 0, width, height);
    }

    /**
     * 每一次View的重绘都会调用
     * @param gl10
     */
    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);
    }
}
