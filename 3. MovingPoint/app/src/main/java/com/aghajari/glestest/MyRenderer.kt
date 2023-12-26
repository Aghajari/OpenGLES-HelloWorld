package com.aghajari.glestest

import android.content.Context
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.max
import kotlin.math.min

class MyRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    private var program = 0
    private var fragmentShader = 0
    private var vertexShader = 0

    private var startTime = 0L
    private var animFraction = 0
    private var isRightToLeft = false

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        vertexShader = compileShader(context, GLES31.GL_VERTEX_SHADER, "vertex_shader.glsl")
        fragmentShader = compileShader(context, GLES31.GL_FRAGMENT_SHADER, "fragment_shader.glsl")

        program = GLES31.glCreateProgram()
        GLES31.glAttachShader(program, vertexShader)
        GLES31.glAttachShader(program, fragmentShader)
        GLES31.glLinkProgram(program)
        GLES31.glUseProgram(program)

        animFraction = GLES31.glGetUniformLocation(program, "animFraction")
        startTime = System.currentTimeMillis() + 1000
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        val duration = 3000L
        val now = System.currentTimeMillis()
        var fraction = (now - startTime).toFloat() / duration
        fraction = max(0f, fraction)
        fraction = min(1f, fraction)

        if (isRightToLeft) {
            fraction = 1f - fraction
        }
        if (startTime + duration <= now) {
            isRightToLeft = !isRightToLeft
            startTime = now
        }

        GLES31.glUniform1f(animFraction, fraction)
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