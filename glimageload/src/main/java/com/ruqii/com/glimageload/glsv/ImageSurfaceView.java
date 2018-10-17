package com.ruqii.com.glimageload.glsv;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.ruqii.com.glimageload.renderer.ImageRenderer;

/**
 * author:黄汝琪 on 2018/10/17.
 * email:huangruqi88@163.com
 */
public class ImageSurfaceView extends GLSurfaceView {

    public ImageSurfaceView(Context context) {
        super(context);
        initEGLContext(context);
    }

    public ImageSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initEGLContext(context);
    }

    /**
     *  创建 OpenGL ES 3.0 上下文
     * @param context
     */
    private void initEGLContext(Context context) {

        setEGLContextClientVersion(3);

        setRenderer(new ImageRenderer(context));
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        requestRender();

    }
}
