package com.glcamera.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * author:黄汝琪 on 2018/9/17.
 * email:huangruqi88@163.com
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        //将Renderer绘制在GLSurfaceView
        setRenderer(mRenderer);
    }

}
