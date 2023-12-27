package com.aghajari.glestest

import android.content.Context
import android.graphics.Color
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.max
import kotlin.math.min

const val NUMBER_OF_POINTS = 2
const val POINT_BYTES = 5 * 4 // (inY, inColor[r, g, b, a])
const val TOTAL_BYTES = NUMBER_OF_POINTS * POINT_BYTES

class MyRenderer(
    private val context: Context
) : GLSurfaceView.Renderer {

    private var program = 0
    private var fragmentShader = 0
    private var vertexShader = 0

    private var animFraction = 0
    private var isRightToLeft = false

    private var lastTime = 0L
    private var time = 0f

    private val minDeltaTime = 1000f / context.getScreenRefreshRate()
    private val maxDeltaTime = 2f * minDeltaTime

    private val buffer = IntArray(1)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        vertexShader = compileShader(context, GLES31.GL_VERTEX_SHADER, "vertex_shader.glsl")
        fragmentShader = compileShader(context, GLES31.GL_FRAGMENT_SHADER, "fragment_shader.glsl")

        program = GLES31.glCreateProgram()
        GLES31.glAttachShader(program, vertexShader)
        GLES31.glAttachShader(program, fragmentShader)
        GLES31.glLinkProgram(program)
        GLES31.glUseProgram(program)

        animFraction = GLES31.glGetUniformLocation(program, "animFraction")
        time = -1000f // 1s delay

        GLES31.glGenBuffers(buffer.size, buffer, 0)

        val data = ByteBuffer.allocateDirect(TOTAL_BYTES)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(-0.1f)              // y position of the first point
                putColor(Color.CYAN)    // color of the first point
                put(0.1f)               // y position of the second point
                putColor(Color.YELLOW)  // color of the second point
                position(0)  // look at the first byte of data
            }

        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, buffer[0])
        GLES31.glBufferData(GLES31.GL_ARRAY_BUFFER, TOTAL_BYTES, data, GLES31.GL_STATIC_DRAW)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES31.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        val now = System.nanoTime()
        val deltaT = ((now - lastTime) / 1000000f)
        if (deltaT < minDeltaTime) {
            return
        }
        time += min(maxDeltaTime, deltaT)
        lastTime = now

        GLES31.glClear(GLES31.GL_COLOR_BUFFER_BIT)

        val duration = 3000f
        var fraction = time / duration
        fraction = max(0f, fraction)
        fraction = min(1f, fraction)

        if (isRightToLeft) {
            fraction = 1f - fraction
        }
        if (duration <= time) {
            isRightToLeft = !isRightToLeft
            time = 0f
        }

        GLES31.glUniform1f(animFraction, fraction)

        GLES31.glBindBuffer(GLES31.GL_ARRAY_BUFFER, buffer[0])

        GLES31.glVertexAttribPointer(0, 1, GLES31.GL_FLOAT, false, POINT_BYTES, 0)
        GLES31.glEnableVertexAttribArray(0)

        GLES31.glVertexAttribPointer(1, 4, GLES31.GL_FLOAT, false, POINT_BYTES, 4)
        GLES31.glEnableVertexAttribArray(1)

        GLES31.glDrawArrays(GLES31.GL_POINTS, 0, NUMBER_OF_POINTS)
    }

    fun destroy() {
        if (program == 0) {
            return
        }

        GLES31.glDeleteProgram(program)
        GLES31.glDeleteShader(fragmentShader)
        GLES31.glDeleteShader(vertexShader)
        GLES31.glDeleteBuffers(buffer.size, buffer, 0)

        program = 0
    }
}