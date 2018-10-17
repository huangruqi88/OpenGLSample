package com.ruqii.com.glimageload.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES31;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * author:黄汝琪 on 2018/10/17.
 * email:huangruqi88@163.com
 */
public class ImageRenderer implements GLSurfaceView.Renderer {
    /**
     * *****************************
     * 一、GLSL 简介
     * GLSL又叫OpenGL着色语言（OpenGL Shading Language），是用来在OpenGL中着色编程的语言，
     * 是一种面向过程的语言，基本的语法和C/C++基本相同，他们是在图形卡的GPU （Graphic Processor Unit图形处理单元）
     * 上执行的，代替了固定的渲染管线的一部分，使渲染管线中不同层次具有可编程性。比如：视图转换、投影转换等。
     * GLSL（GL Shading Language）的着色器代码分成2个部分：Vertex Shader（顶点着色器）和Fragment（片断着色器）。
     *
     * 在前面的学习中，我们基本上使用的都是非常简单的着色器，基本上没有使用过GLSL的内置函数，
     * 但是在后面我们完成其他的功能的时候应该就会用到这些内置函数了。
     * *****************************
     */


    /**
     * 顶点着色器
     */
    private static final String vertexMatrixShaderCode =
            "attribute vec4 vPosition;\n" +
                    "attribute vec2 vCoordinate;\n" +
                    "uniform mat4 vMatrix;\n" +
                    "varying vec2 aCoordinate;\n" +
                    "void main(){\n" +
                    "    gl_Position=vMatrix*vPosition;\n" +
                    "    aCoordinate=vCoordinate;\n" +
                    "}";


    /**
     * 片段着色器
     */
    private static final String fragmentShaderCode =
            "precision mediump float;\n" +
                    "uniform sampler2D vTexture;\n" +
                    "varying vec2 aCoordinate;\n" +
                    "void main(){\n" +
                    "    gl_FragColor=texture2D(vTexture,aCoordinate);\n" +
                    "}";

    private static final float[] sPos = {
            -1.0f, 1.0f,    //左上角
            -1.0f, -1.0f,   //左下角
            1.0f, 1.0f,     //右上角
            1.0f, -1.0f     //右下角
    };

    private static final float[] sCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    private Context mContext;
    private Bitmap mBitmap;
    private int mProgram;
    private int glHPosition;
    private int glHTexture;
    private int glHCoordinate;
    private int glHMatrix;
    private int textureId;

    private FloatBuffer bPos;
    private FloatBuffer bCoord;

    private float[] mViewMatrix = new float[16];
    private float[] mProjectMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private static final String TAG = "ImageRenderer";

    public ImageRenderer(Context context) {
        mContext = context;
        try {
            mBitmap = BitmapFactory.decodeStream(mContext.getResources().getAssets().open("texture/fengj.png"));
            ByteBuffer bb = ByteBuffer.allocateDirect(sPos.length * 4);
            // ByteBufferorder()修改此缓冲区的字节顺序
            //  ByteOrder.nativeOrder()检索基础平台的本机字节顺序。
            bb.order(ByteOrder.nativeOrder());
            bPos = bb.asFloatBuffer();
            bPos.put(sPos);
            //设置缓冲区的位置。如果标记已定义且大于新位置被丢弃。
            bPos.position(0);

            ByteBuffer cc = ByteBuffer.allocateDirect(sCoord.length * 4);
            cc.order(ByteOrder.nativeOrder());
            bCoord = cc.asFloatBuffer();
            bCoord.put(sCoord);
            bCoord.position(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //清除颜色
        GLES31.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //开启2D纹理贴图
        GLES31.glEnable(GLES31.GL_TEXTURE_2D);

        mProgram = createOpenGLProgram(vertexMatrixShaderCode, fragmentShaderCode);

        glHPosition = GLES31.glGetAttribLocation(mProgram, "vPosition");
        glHCoordinate = GLES31.glGetAttribLocation(mProgram, "vCoordinate");
        glHTexture = GLES31.glGetUniformLocation(mProgram, "vTexture");
        glHMatrix = GLES31.glGetUniformLocation(mProgram, "vMatrix");

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES31.glViewport(0, 0, width, height);

        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        float sWH = w / (float) h;
        float sWidthHeight = width / (float) height;

        if (width > height) {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight * sWH, sWidthHeight * sWH, -1, 1, 3, 7);
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -sWidthHeight / sWH, sWidthHeight / sWH, -1, 1, 3, 7);
            }
        } else {
            if (sWH > sWidthHeight) {
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH, 3, 7);
            } else {
                Matrix.orthoM(mProjectMatrix, 0, -1, 1, -sWH / sWidthHeight, sWH / sWidthHeight, 3, 7);
            }
        }

        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 7.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT | GLES31.GL_DEPTH_BUFFER_BIT);
        GLES31.glUseProgram(mProgram);
        GLES31.glUniformMatrix4fv(glHMatrix, 1, false, mMVPMatrix, 0);
        //GLES20.glEnableVertexAttribArray() 启用顶点属性数组
        //GLES20.glDisableVertexAttribArray() 禁用顶点属性数组
        GLES31.glEnableVertexAttribArray(glHPosition);
        GLES31.glEnableVertexAttribArray(glHCoordinate);
        GLES31.glUniform1i(glHTexture, 0);
        textureId = createTexture();
        Log.d(TAG, "onDrawFrame:     textureId = " + textureId);
        //传入顶点坐标
        GLES31.glVertexAttribPointer(glHPosition, 2, GLES31.GL_FLOAT, false, 0, bPos);
        //传入纹理坐标
        GLES31.glVertexAttribPointer(glHCoordinate,2,GLES31.GL_FLOAT,false,0,bCoord);
        GLES31.glDrawArrays(GLES31.GL_TRIANGLE_STRIP,0,4);
    }

    private int createTexture() {
        int[] texture = new int[1];
        if (mBitmap != null && !mBitmap.isRecycled()) {
            //生成纹理
            GLES31.glGenTextures(1,texture,0);
            //生成纹理
            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D,GLES31.GL_TEXTURE_MIN_FILTER,GLES31.GL_LINEAR);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES31.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES31.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES31.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap, 0);
            return texture[0];
        }
        return 0;
    }


    /**
     * 生成OpenGL Program
     *
     * @param vertexSource   顶点着色器代码
     * @param fragmentSource 片元着色器代码
     * @return 生成的OpenGL Program，如果为0，则表示创建失败
     */
    private int createOpenGLProgram(String vertexSource, String fragmentSource) {
        int vertex = loadShader(GLES31.GL_VERTEX_SHADER, vertexSource);
        if (vertex == 0) {
            Log.e(TAG, "loadShader vertex failed");
            return 0;
        }

        int fragment = loadShader(GLES31.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragment == 0) {
            Log.e(TAG, "loadShader fragment failed");
            return 0;
        }

        int program = GLES31.glCreateProgram();

        if (program != 0) {
            GLES31.glAttachShader(program, vertex);
            GLES31.glAttachShader(program, fragment);
            GLES31.glLinkProgram(program);

            int[] linkStaus = new int[1];
            GLES31.glGetProgramiv(program, GLES31.GL_LINK_STATUS, linkStaus, 0);
            if (linkStaus[0] != GLES31.GL_TRUE) {
                Log.e(TAG, "Could not link program:" + GLES31.glGetProgramInfoLog(program));
                GLES31.glDeleteProgram(program);
                program = 0;
            }
        }

        return program;
    }

    /**
     * 加载着色器
     *
     * @param type       加载着色器类型
     * @param shaderCode 加载着色器的代码
     */
    public static int loadShader(int type, String shaderCode) {
        //根据type创建顶点着色器或者片元着色器
        int shader = GLES20.glCreateShader(type);
        //将着色器的代码加入到着色器中
        GLES20.glShaderSource(shader, shaderCode);
        //编译着色器
        GLES20.glCompileShader(shader);
        return shader;
    }
}
