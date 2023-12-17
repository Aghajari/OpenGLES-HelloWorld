package com.aghajari.glestest

import android.content.Context
import android.opengl.GLES31
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
        GLES31.glDeleteShader(shader)
        throw RuntimeException("Error compiling shader: " + GLES31.glGetShaderInfoLog(shader))
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