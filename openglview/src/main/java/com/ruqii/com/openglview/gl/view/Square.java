package com.ruqii.com.openglview.gl.view;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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
public class Square {
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    /**
     * number of coordinates per vertex in this array
     */
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            // top left
            -0.5f,  0.5f, 0.0f,
            // bottom left
            -0.5f, -0.5f, 0.0f,
            // bottom right
            0.5f, -0.5f, 0.0f,
            // top right
            0.5f,  0.5f, 0.0f };
    // order to draw vertices
    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
}
