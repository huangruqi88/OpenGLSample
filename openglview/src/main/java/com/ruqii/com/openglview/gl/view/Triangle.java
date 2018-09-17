package com.ruqii.com.openglview.gl.view;

import android.opengl.GLES31;

import com.ruqii.com.openglview.gl.MyGLRenderer;

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

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

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
        int vertexShader = MyGLRenderer.loadShader(GLES31.GL_VERTEX_SHADER,vertexShaderCode);
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


    public void draw() {
        // Add program to OpenGL ES environment 向OpenGL ES环境中添加进APP
        GLES31.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member 获取顶点着色器的vPosition成员的句柄
        mPositionHandle = GLES31.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices 启用三角形顶点的句柄
        GLES31.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data 准备三角坐标数据
        GLES31.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES31.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member  获取fragmentShader的vColor成员句柄
        mColorHandle = GLES31.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle 设置三角形的颜色
        GLES31.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle 绘制三角形
        GLES31.glDrawArrays(GLES31.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array 关闭定点数组
        GLES31.glDisableVertexAttribArray(mPositionHandle);
    }



}
