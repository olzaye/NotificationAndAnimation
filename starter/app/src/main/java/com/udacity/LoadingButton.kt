package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap

    private var widthSize = 0
    private var heightSize = 0
    private var currentSweepAngle = 0
    private var widthButtonProgress = 0f

    private val defaultTextSize = resources.getDimension(R.dimen.default_text_size)
    private val buttonName = resources.getString(R.string.button_name)

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = defaultTextSize
    }

    private val circlePaint = Paint().apply {
        color = Color.YELLOW
    }

    private val rectanglePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
    }


    private val rect = RectF(0f, 0f, 50f, 50f)

    private val valueAnimator = ValueAnimator.ofInt(0, 360)

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, null)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::extraBitmap.isInitialized) extraBitmap.recycle()

        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    override fun performClick(): Boolean {
        valueAnimator.duration = 3000
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(p0: Animator?) {
                currentSweepAngle = 0
                invalidate()
            }
        })

        valueAnimator.addUpdateListener {
            currentSweepAngle = valueAnimator.animatedValue as Int
            invalidate()
        }
        valueAnimator.start()

        val buttonValueAnimator = ValueAnimator.ofFloat(0f, widthSize.toFloat())
        buttonValueAnimator.duration = 3000
        buttonValueAnimator.interpolator = LinearInterpolator()
        buttonValueAnimator.addUpdateListener {
            widthButtonProgress = (buttonValueAnimator.animatedValue as Float)
            invalidate()
        }

        buttonValueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(p0: Animator?) {
                widthButtonProgress = 0f
                invalidate()
            }
        })

        buttonValueAnimator.start()
        return super.performClick()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)

        canvas.save()
        canvas.drawRect(0f, 0f, widthButtonProgress, heightSize.toFloat(), rectanglePaint)
        canvas.restore()

        canvas.save()
        canvas.drawText(
            buttonName,
            widthSize / 2f,
            heightSize / 2f - (textPaint.descent() + textPaint.ascent()) / 2f,
            textPaint
        )
        canvas.restore()

        canvas.save()
        canvas.translate(widthSize - 100f, (heightSize / 2f) - 20)
        canvas.drawArc(rect, 0f, currentSweepAngle.toFloat(), true, circlePaint)
        canvas.restore()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
}