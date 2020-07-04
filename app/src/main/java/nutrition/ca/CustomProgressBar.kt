package nutrition.ca

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View

class CustomProgressBar : View {

    private val stroke = 40.0f
    private var backgroundArc:RectF = RectF()
    private var bagPaint: Paint = Paint()

    private val paint: Paint = Paint()
    private var progressBarValue = 95
    private var progressBarColor = 0
    private var progressBarBgColor = 0
    private var bounds: Rect = Rect()
    private var barArc: RectF = RectF()

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        var typedArray: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar)

        progressBarValue = typedArray.getInt(R.styleable.CustomProgressBar_my_progress, 0)
        progressBarColor = typedArray.getColor(R.styleable.CustomProgressBar_my_progress_color, 0)
        progressBarBgColor = typedArray.getColor(R.styleable.CustomProgressBar_my_progress_bg_color, Color.LTGRAY)

        typedArray.recycle()
    }

    fun setValue(value: Int){
        this.progressBarValue = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var metrics: DisplayMetrics = resources.displayMetrics
        var stroke = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.stroke, metrics)

        backgroundArc.set(stroke,stroke,
            width.toFloat() - stroke, height.toFloat() - stroke)

        bagPaint.color = progressBarBgColor
        bagPaint.style = Paint.Style.STROKE
        bagPaint.strokeWidth = 60.0f
        bagPaint.isAntiAlias = true

        canvas?.drawArc(backgroundArc, 0.0f, 360.0f, false, bagPaint)

        paint.color = progressBarColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = stroke - 40.0f
        paint.isAntiAlias = true
        paint.isDither = true // <- deixa a cor melhor
        paint.strokeCap = Paint.Cap.ROUND // <- deixa as pontas redondas

        var progress = (360.0f / 100) * progressBarValue
        canvas?.getClipBounds(bounds)

        barArc.set(stroke, stroke, bounds.right - stroke, bounds.bottom - stroke)
        canvas?.drawArc(barArc, 270.0f, progress, false, paint)
    }
}