package com.ruqii.com.glimageload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ruqii.com.glimageload.glsv.ImageSurfaceView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(new ImageSurfaceView(this)); // 加载图片
    }
}
