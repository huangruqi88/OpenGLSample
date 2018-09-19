package com.ruqii.com.openglview.gl;

import android.opengl.GLES31;
import android.opengl.GLSurfaceView;

import com.ruqii.com.openglview.gl.view.Square;
import com.ruqii.com.openglview.gl.view.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/9/17.
 * email:huangruqi88@163.com
 *
 * 使用OpenGL需要定义一下几个东西
 *
 * Vertex Shader - 用于渲染形状的顶点的OpenGLES 图形代码。
 * Fragment Shader - 用于渲染形状的外观（颜色或纹理）的OpenGLES 代码。
 * Program - 一个OpenGLES对象，包含了你想要用来绘制一个或多个形状的shader。
 *
 *
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;
    private Square   mSquare;
    /**
     * 在View的OpenGL环境被创建的时候调用。
     * @param gl10
     * @param eglConfig
     */
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES31.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //绘制三角形
        mTriangle = new Triangle();
        //绘制正方形
        mSquare = new Square();
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
        //GLES31.glClear()必须在三角形绘制之前
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);
        mTriangle.draw();
    }

    /**
     * 加载OpenGl需要的Shader
     * @param type
     * @param shaderCode
     * @return
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES31.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        return shader;
    }
}
