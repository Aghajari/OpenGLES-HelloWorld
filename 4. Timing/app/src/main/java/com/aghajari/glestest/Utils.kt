package com.aghajari.glestest

import android.content.Context
import android.opengl.GLES31
import android.os.Build
import android.view.WindowManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun compileShader(context: Context, type: Int, fileName: String): Int {
    val shader = GLES31.glCreateShader(type)
    GLES31.glShaderSource(shader, context.readFromAssets(fileName))
    GLES31.glCompileShader(shader)

    val compileStatus = IntArray(1)
    GLES31.glGetShaderiv(shader, GLES31.GL_COMPILE_STATUS, compileStatus, 0)
    if (compileStatus[0] == 0) {
        val info = GLES31.glGetShaderInfoLog(shader)
        GLES31.glDeleteShader(shader)
        throw RuntimeException("Error compiling shader: $info")
    }

    return shader
}

private fun Context.readFromAssets(fileName: String): String {
    try {
        val inputStream = assets.open(fileName)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()

        var line: String?
        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line).append("\n")
        }

        bufferedReader.close()
        inputStream.close()

        return stringBuilder.toString()
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}

fun Context.getScreenRefreshRate(): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        display?.refreshRate
    } else {
        val manager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        manager?.defaultDisplay?.refreshRate
    } ?: 60f
}