package com.ruqii.com.ontouch.gl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.lang.annotation.Target;

/**
 * author:黄汝琪 on 2018/9/17.
 * email:huangruqi88@163.com
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    /**
     * 触摸比例
     */
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;


    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        //将Renderer绘制在GLSurfaceView
        setRenderer(mRenderer);

        // 只有在绘图数据发生更改时才呈现视图。
        // ：为了让三角形自动旋转，这条线被注释掉了:
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                //在中线以上的反方向旋转
                if (y > getHeight() / 2) {
                    dx = dx * -1;
                }

                // 向中线左侧的旋转方向反转
                if (x < getWidth() / 2) {
                    dy = dy * -1;
                }
                mRenderer.setOnTouch(true);
                mRenderer.setAngle(mRenderer.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));
                //请求渲染器渲染画面
                requestRender();
            default:
                break;
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }


}
