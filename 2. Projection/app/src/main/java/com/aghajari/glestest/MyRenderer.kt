package com.aghajari.glestest

import android.content.Context
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    private var program = 0
    private var fragmentShader = 0
    private var vertexShader = 0

    private val position = floatArrayOf(500.0f, 500.0f)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        vertexShader = compileShader(context, GLES31.GL_VERTEX_SHADER, "vertex_shader.glsl")
        fragmentShader = compileShader(context, GLES31.GL_FRAGMENT_SHADER, "fragment_shader.glsl")

        program = GLES31.glCreateProgram()
        GLES31.glAttachShader(program, vertexShader)
        GLES31.glAttachShader(program, fragmentShader)
        GLES31.glLinkProgram(program)
        GLES31.glUseProgram(program)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)

        val projection = FloatArray(16)
        Matrix.orthoM(projection, 0, 0f, width.toFloat(), height.toFloat(), 0f, -1f, 1f)
        val projectionLocation = GLES31.glGetUniformLocation(program, "projection")
        GLES31.glUniformMatrix4fv(projectionLocation, 1, false, projection, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        GLES31.glVertexAttrib2fv(0, position, 0)
        GLES31.glDrawArrays(GLES31.GL_POINTS, 0, 1)
    }

    fun destroy() {
        if (program == 0) {
            return
        }

        GLES31.glDeleteProgram(program)
        GLES31.glDeleteShader(fragmentShader)
        GLES31.glDeleteShader(vertexShader)
        program = 0
    }
}