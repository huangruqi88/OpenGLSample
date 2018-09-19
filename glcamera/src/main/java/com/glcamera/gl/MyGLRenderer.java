package com.glcamera.gl;

import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.glcamera.gl.view.Square;
import com.glcamera.gl.view.Triangle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/9/17.
 * email:huangruqi88@163.com
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    /**
     * mMVPMatrix is an abbreviation for "Model View Projection Matrix"
     * mMVPMatrix是“模型视图投影矩阵”的缩写
     */
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private Triangle mTriangle;
    private Square mSquare;

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
     * 投影变化的数据是在你GLSurfaceView.Renderer类的onSurfaceChanged()方法中被计算的。
     * 下面的示例代码是获取GLSurfaceView的高和宽，并通过Matrix.frustumM()方法用它们填充到投影变换矩阵中。
     * @param gl10
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES31.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // 这个投影矩阵应用于物体坐标 在onDrawFrame()中进行
        //
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    /**
     * 每一次View的重绘都会调用
     * @param gl10
     */
    @Override
    public void onDrawFrame(GL10 gl10) {
//        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);
//        mTriangle.draw(mMVPMatrix);



        // 设置相机位置(视图矩阵)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // 计算投影和视图变换
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        //此处的glClear
//        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT);  使用投影和相机是不使用GLES31.glClear()
        // 画出形状
        mTriangle.draw(mMVPMatrix);

    }


    /**
     * 加载OpenGl需要的Shader
     * @param type
     * @param shaderCode
     * @return
     */
    public static int loadShader(int type, String shaderCode){

        // 创建一个顶点着色器类型 (GLES20.GL_VERTEX_SHADER)或者片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES31.glCreateShader(type);

        // add the source code to the shader and compile it
        //将源代码添加到着色器中并编译它
        GLES31.glShaderSource(shader, shaderCode);
        GLES31.glCompileShader(shader);

        return shader;
    }
}
