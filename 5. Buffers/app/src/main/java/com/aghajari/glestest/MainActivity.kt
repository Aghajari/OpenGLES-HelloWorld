package com.aghajari.glestest

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val renderer by lazy {
        MyRenderer(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val glSurfaceView = findViewById<GLSurfaceView>(R.id.gl)
        glSurfaceView.setEGLContextClientVersion(3)
        glSurfaceView.setRenderer(renderer)
    }

    override fun onDestroy() {
        super.onDestroy()
        renderer.destroy()
    }
}