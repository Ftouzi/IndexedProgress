package com.ftouzi.indexedprogress

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

class IndexedProgress : View {

    // Inner properties
    private var rectHeight: Float = 0.toFloat()

    // Outer properties
    private var baseColor: Int = 0
    private var enableRoundedCorner: Boolean = false

    private var value: Float = 0.toFloat()
    private var valueFont: Typeface? = null
    private var valueColor: Int = 0
    private var valueSuffix: String? = null

    private var progressShader: Drawable? = null
    private var progressColor: Int = 0

    private var separatorColor: Int = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexedProgress, 0, 0)
        try {

            this.baseColor = typedArray.getColor(R.styleable.IndexedProgress_base_color, DEFAULT_SECOND_COLOR)
            this.enableRoundedCorner =
                typedArray.getBoolean(R.styleable.IndexedProgress_enable_rounded_corner, DEFAULT_ROUNDED_CORNER_STATE)

            this.value = typedArray.getFloat(R.styleable.IndexedProgress_value, DEFAULT_VALUE)
            this.valueColor = typedArray.getColor(R.styleable.IndexedProgress_value_color, DEFAULT_BASE_COLOR)
            // val valueFont = typedArray.getResourceId(R.styleable.IndexedProgress_value_font, R.font.montserrat_bold)
            // this.valueFont = ResourcesCompat.getFont(context, valueFont)

            this.valueSuffix = typedArray.getString(R.styleable.IndexedProgress_value_suffix)

            this.progressShader = typedArray.getDrawable(R.styleable.IndexedProgress_progress_shader)
            this.progressColor = typedArray.getColor(R.styleable.IndexedProgress_progress_color, DEFAULT_BASE_COLOR)

            this.separatorColor = typedArray.getColor(R.styleable.IndexedProgress_separator_color, DEFAULT_BASE_COLOR)

            typedArray.recycle()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rectHeight = height * DEFAULT_ITEM_FACTOR

        drawFullRectF(canvas)
        drawProgressRectF(canvas)
        drawSeparator(canvas)
        drawValueText(canvas)
    }

    private fun drawFullRectF(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = this.baseColor
        val top = (height - rectHeight) / 2
        val bottom = (height + rectHeight) / 2
        val radius = rectHeight * DEFAULT_CORNER_RADIUS_FACTOR
        val rectF = RectF(0f, top, width.toFloat(), bottom)
        if (enableRoundedCorner)
            canvas.drawRoundRect(rectF, radius, radius, paint)
        else
            canvas.drawRect(rectF, paint)
    }

    private fun drawProgressRectF(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = progressColor

        // RectF values
        val top = (height - rectHeight) / 2
        val bottom = (height + rectHeight) / 2
        val right = IndexedProgressUtils.getProgressTranslation(width, value)
        val radius = rectHeight * DEFAULT_CORNER_RADIUS_FACTOR

        // Preparing shader
        if (progressShader != null) {
            val bitmap: Bitmap = when (progressShader) {
                is BitmapDrawable -> (progressShader as BitmapDrawable).bitmap
                is VectorDrawable -> IndexedProgressUtils.getBitmap((progressShader as VectorDrawable))
                else -> throw IllegalArgumentException("unsupported drawable type")
            }

            val bitmapShader = BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
            paint.shader = bitmapShader
        }

        if (enableRoundedCorner) {
            val path = IndexedProgressUtils.roundedRect(
                0f, top, right, bottom, radius, radius,
                tl = true, tr = false, br = false, bl = true
            )
            canvas.drawPath(path, paint)
        } else {
            canvas.drawRect(0f, top, right, bottom, paint)
        }

    }

    private fun drawSeparator(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = width * DEFAULT_LINE_STROKE_WIDTH
        paint.color = this.separatorColor

        val translation = IndexedProgressUtils.getProgressTranslation(width, value)
        canvas.drawLine(translation, 0f, translation, height.toFloat(), paint)
    }

    private fun drawValueText(canvas: Canvas) {
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = this.valueColor
        textPaint.typeface = this.valueFont
        textPaint.textSize = rectHeight * DEFAULT_TEXT_VALUE_FACTOR

        val rect = Rect()

        val shownValue = IndexedProgressUtils.formatValue(value, valueSuffix)
        textPaint.getTextBounds(shownValue, 0, shownValue.length, rect)

        val dy = (height / 2 + rect.height() / 2).toFloat()
        val dx =
            IndexedProgressUtils.getProgressTranslation(width, value) + rect.width() * DEFAULT_TEXT_TRANSLATION_SPACE

        val maxDx = width - rect.width() - width * VALUE_TRANSLATION_LIMIT_FACTOR
        if (dx < maxDx)
            canvas.drawText(shownValue, dx, dy, textPaint)
        else
            canvas.drawText(shownValue, maxDx, dy, textPaint)
    }

    fun setValue(value: Float) {
        this.value = value
        invalidate()
    }

    companion object {

        private const val DEFAULT_VALUE = 0f
        private const val DEFAULT_ITEM_FACTOR = 0.75f
        private const val DEFAULT_LINE_STROKE_WIDTH = 0.007f
        private const val DEFAULT_TEXT_VALUE_FACTOR = 0.75f
        private const val DEFAULT_CORNER_RADIUS_FACTOR = 0.13f
        private const val DEFAULT_TEXT_TRANSLATION_SPACE = 0.2f
        private const val DEFAULT_BASE_COLOR = Color.BLACK
        private const val DEFAULT_SECOND_COLOR = Color.LTGRAY
        private const val DEFAULT_ROUNDED_CORNER_STATE = true
        private const val VALUE_TRANSLATION_LIMIT_FACTOR = 0.02f
    }
}