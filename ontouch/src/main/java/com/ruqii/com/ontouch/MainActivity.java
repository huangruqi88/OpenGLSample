package com.ruqii.com.ontouch;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ruqii.com.ontouch.gl.MyGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    private MyGLSurfaceView mGLView;

    private String TAG = "MainActivity111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //GLSurfaceView必须在setContentView之前初始化
        mGLView = new MyGLSurfaceView(this);



        ActivityManager am =(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();

        Log.d(TAG, "onCreate: ------------info = " + info.reqGlEsVersion);

        setContentView(mGLView);
    }
}
