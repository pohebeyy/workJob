package com.example.a3danimalswork

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLUtils
import android.opengl.Matrix
import android.graphics.BitmapFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Model(private val context: Context, private val objFileName: String, private val textureFiles: List<String>) {

    private val vertexShaderCode = """
        uniform mat4 uMVPMatrix;
        attribute vec4 vPosition;
        attribute vec2 aTexCoord;
        varying vec2 vTexCoord;
        
        void main() {
            gl_Position = uMVPMatrix * vPosition;
            vTexCoord = aTexCoord;
        }
    """.trimIndent()

    private val fragmentShaderCode = """
        precision mediump float;
        uniform sampler2D uTexture;
        varying vec2 vTexCoord;
        
        void main() {
            gl_FragColor = texture2D(uTexture, vTexCoord);
        }
    """.trimIndent()

    private val mProgram: Int
    private val textureHandles = IntArray(textureFiles.size)
    private val mvpMatrixHandle: Int
    private val positionHandle: Int
    private val texCoordHandle: Int
    private val textureHandle: Int

    private lateinit var vertexBuffer: FloatBuffer
    private lateinit var texCoordBuffer: FloatBuffer
    private var vertexCount = 0

    init {
        // Компилируем шейдеры и связываем программу
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }

        mvpMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
        texCoordHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoord")
        textureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")

        // Загружаем текстуры
        textureFiles.forEachIndexed { index, fileName ->
            textureHandles[index] = loadTexture(context, fileName)
        }

        // Загружаем данные модели
        loadModelData()
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)

        // Pass the MVP matrix to the shader
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        // Bind the texture (assuming textureHandles[1] is the texture you want to use)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[1])
        GLES20.glUniform1i(textureHandle, 0)

        // Enable vertex arrays and pass data
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        GLES20.glEnableVertexAttribArray(texCoordHandle)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)

        // Draw the model
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        // Disable vertex arrays
        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }

    private fun loadTexture(context: Context, fileName: String): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] != 0) {
            val options = BitmapFactory.Options()
            options.inScaled = false // No pre-scaling

            try {
                context.assets.open(fileName).use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

                    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
                    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

                    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

                    bitmap.recycle()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw RuntimeException("Error loading texture: $fileName")
            }
        }

        if (textureHandle[0] == 0) {
            throw RuntimeException("Error loading texture.")
        }

        return textureHandle[0]
    }

    private fun loadModelData() {
        val vertices = mutableListOf<Float>()
        val texCoords = mutableListOf<Float>()

        try {
            context.assets.open(objFileName).bufferedReader().use { reader ->
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    val tokens = line!!.split("\\s+".toRegex())

                    when (tokens[0]) {
                        "v" -> {
                            vertices.add(tokens[1].toFloat())
                            vertices.add(tokens[2].toFloat())
                            vertices.add(tokens[3].toFloat())
                        }
                        "vt" -> {
                            texCoords.add(tokens[1].toFloat())
                            texCoords.add(1.0f - tokens[2].toFloat()) // Invert V coordinate
                        }
                        // Вы можете добавить обработку других частей файла .obj при необходимости
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException("Error loading model data: $objFileName")
        }

        vertexCount = vertices.size / 3

        // Initialize vertex buffer
        val vertexByteBuffer = ByteBuffer.allocateDirect(vertices.size * 4)
        vertexByteBuffer.order(ByteOrder.nativeOrder())
        vertexBuffer = vertexByteBuffer.asFloatBuffer()
        vertexBuffer.put(vertices.toFloatArray())
        vertexBuffer.position(0)

        // Initialize texture coordinate buffer
        val texCoordByteBuffer = ByteBuffer.allocateDirect(texCoords.size * 4)
        texCoordByteBuffer.order(ByteOrder.nativeOrder())
        texCoordBuffer = texCoordByteBuffer.asFloatBuffer()
        texCoordBuffer.put(texCoords.toFloatArray())
        texCoordBuffer.position(0)
    }

    companion object {
        fun loadShader(type: Int, shaderCode: String): Int {
            return GLES20.glCreateShader(type).also { shader ->
                GLES20.glShaderSource(shader, shaderCode)
                GLES20.glCompileShader(shader)
            }
        }
    }
}
