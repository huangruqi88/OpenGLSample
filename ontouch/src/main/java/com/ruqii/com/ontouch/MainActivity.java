package com.ruqii.com.ontouch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruqii.com.ontouch.gl.MyGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //GLSurfaceView必须在setContentView之前初始化
        mGLView = new MyGLSurfaceView(this);

        setContentView(mGLView);
    }
}
