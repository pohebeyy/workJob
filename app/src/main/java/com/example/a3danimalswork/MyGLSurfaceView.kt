package com.example.a3danimalswork

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector

class MyGLSurfaceView : GLSurfaceView {

    private lateinit var renderer: MyGLRenderer
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var initialCameraX = 0f
    private var initialCameraY = 0f
    private var initialCameraZ = 0f
    private var initialModelX = 0f
    private var initialModelY = 0f
    private var initialModelZ = 2f // Установите начальное значение Z для модели

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer(context, this)
        setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY

        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }

    private var previousX = 0f
    private var previousY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (!scaleGestureDetector.isInProgress) {
                    val dx = x - previousX
                    val dy = y - previousY
                    renderer.angleX += dy * TOUCH_SCALE_FACTOR
                    renderer.angleY += dx * TOUCH_SCALE_FACTOR
                    requestRender()
                }
            }
        }

        previousX = x
        previousY = y
        return true
    }

    fun moveModelToFixedPosition() {
        renderer.moveModelToCameraPosition()
        requestRender()
    }

    fun saveInitialCameraPosition(x: Float, y: Float, z: Float) {
        initialCameraX = x
        initialCameraY = y
        initialCameraZ = z
    }

    fun saveInitialModelPosition(x: Float, y: Float, z: Float) {
        initialModelX = x
        initialModelY = y
        initialModelZ = z
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            renderer.scaleFactor *= detector.scaleFactor
            renderer.scaleFactor = renderer.scaleFactor.coerceIn(0.1f, 10.0f)
            requestRender()
            return true
        }
    }

    companion object {
        private const val TOUCH_SCALE_FACTOR = 45.0f / 320
    }
}
