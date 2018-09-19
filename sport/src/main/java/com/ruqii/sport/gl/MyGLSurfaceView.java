package com.ruqii.sport.gl;

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


        // 只有在绘图数据发生更改时才呈现视图。
        // ：为了让三角形自动旋转，这条线被注释掉了:
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
