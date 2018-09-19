package com.glcamera.gl.view;

import android.opengl.GLES31;

import com.glcamera.gl.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * author:黄汝琪 on 2018/9/17.
 * email:huangruqi88@163.com
 *
 *
 * OpenGL ES允许你使用三维空间坐标系定义绘制的图像，所以你在绘制一个三角形之前必须要先定义它的坐标。
 * 在OpenGL中，这样做的典型方法是为坐标定义浮点数的顶点数组。为了获得最大的效率，可以将这些坐标写入ByteBuffer，
 * 并传递到OpenGL ES图形管道进行处理。
 *
 * OpenGL ES采用坐标系，[0,0,0]（X，Y，Z）指定GLSurfaceView框架的中心，[1,1,0]是框架的右上角，[ - 1，-1,0]是框架的左下角
 *
 */
public class Triangle {

    private final int mProgram;
    /**
     * 顶点数据缓存区
     */
    private FloatBuffer vertexBuffer;

    // 这个数组中每个顶点的坐标数
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            // 以逆时针顺序:
            // 三角形顶部点的坐标
            0.0f,  0.622008459f, 0.0f,
            // 底部左边的点的坐标
            -0.5f, -0.311004243f, 0.0f,
            // 底部右边点的坐标
            0.5f, -0.311004243f, 0.0f
    };

    /**
     *  设置颜色以及透明度
     */
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    /**
     * shader的坐标位置
     */
//    private final String vertexShaderCode =
//            "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    "  gl_Position = vPosition;" +
//                    "}";
    private final String vertexShaderCode =
            // 这个矩阵成员变量提供了一个要操作的钩子
            // 使用这个顶点着色器的对象的坐标
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // 该矩阵必须包含为gl_Position的修饰符
                    // Note that the uMVPMatrix factor *must be first* in order
                    // 请注意，uMVPMatrix因子 *must be first* 即必须排序在第一个
                    // for the matrix multiplication product to be correct.
                    // 这样才能得到一个正确的乘积
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    /**
     * Use to access and set the view transformation
     * 用于访问和设置视图转换
     */
    private int mMVPMatrixHandle;

    /**
     * shader的纹理区域渲染
     */
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";



    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    // 4 bytes per vertex
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    public Triangle() {
        // 为形状坐标初始化顶点字节缓冲区
        // 每个浮点数的坐标值* 4字节
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        //使用硬件Native字节顺序
        bb.order(ByteOrder.nativeOrder());

        // 从ByteBuffer创建一个浮点缓冲区
        vertexBuffer = bb.asFloatBuffer();
        // 向FloatBuffer添加坐标
        vertexBuffer.put(triangleCoords);
        // 设置缓冲区以读取第一个坐标
        vertexBuffer.position(0);

        /**
         * 至少需要一个vertexshader来绘制一个形状和一个fragmentshader来为形状上色
         * 这些形状必须被编译然后被添加到一个OpenGLES program中，program之后被用来绘制形状。
         */

        //Vertex shader（顶点着色器），控制顶点的绘制，指定坐标、变换等。
        int vertexShader = MyGLRenderer.loadShader(GLES31.GL_VERTEX_SHADER,vertexShaderCode);
        //Fragment shader（片段着色器），控制形状内区域渲染，纹理填充内容。
        int fragmentShader = MyGLRenderer.loadShader(GLES31.GL_FRAGMENT_SHADER,fragmentShaderCode);

        // 床架空的OpenGL ES Program
        mProgram = GLES31.glCreateProgram();

        // 添加 vertexShader 至 program
        GLES31.glAttachShader(mProgram, vertexShader);

        //  添加 fragmentShader 至 program
        GLES31.glAttachShader(mProgram, fragmentShader);

        // 创建OpenGL ES程序可执行文件
        GLES31.glLinkProgram(mProgram);

    }

    /**
     * 绘制三角形
     * @param mvpMatrix 传入计算出的变换矩阵
     */
    public void draw(float[] mvpMatrix) {
        // 向OpenGL ES环境中添加
        GLES31.glUseProgram(mProgram);

        // 获取顶点着色器的vPosition成员的句柄
        mPositionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition");

        // 启用三角形顶点的句柄
        GLES31.glEnableVertexAttribArray(mPositionHandle);

        // 准备三角坐标数据
        GLES31.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // 获取fragmentShader的vColor成员句柄
        mColorHandle = GLES31.glGetUniformLocation(mProgram, "vColor");

        // 设置三角形的颜色
        GLES31.glUniform4fv(mColorHandle, 1, color, 0);

        // 绘制三角形
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);

        // 关闭定点数组
        GLES31.glDisableVertexAttribArray(mPositionHandle);


        // 获取形状变换矩阵的句柄（mMVPMatrixHandle）
        mMVPMatrixHandle = GLES31.glGetUniformLocation(mProgram, "uMVPMatrix");

        // 将投影和视图转换传递给着色器
        GLES31.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // 绘制三角形
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);

        // 关闭定点数组
        GLES31.glDisableVertexAttribArray(mPositionHandle);


    }



}
