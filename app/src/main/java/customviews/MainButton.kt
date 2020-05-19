package customviews

import android.R.*
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.olegel.androidndkqt.R

class MainButton : AppCompatButton {
    private val cornerRadius = 10f
    private var callBackTTS: CallBackTTS? = null
    constructor(context: Context?) : super(context) {
        setWillNotDraw(false)
        background = getSelectorDrawable()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setWillNotDraw(false)
        background = getSelectorDrawable()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setWillNotDraw(false)
        background = getSelectorDrawable()
    }

    private fun getSelectorDrawable(): Drawable {
        val out = StateListDrawable()
        out.addState(IntArray(1) { attr.state_pressed }, createNormalDrawable())
        out.addState(IntArray(1) { attr.state_enabled }, createEnableDrawable())
        out.addState(IntArray(1) { attr.state_enabled }, createDisableDrawable())
        out.addState(IntArray(1) { attr.state_focused }, createFocused())
        return out
    }

    private fun createNormalDrawable(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f)
        shape.alpha = 100
        shape.setColor(ContextCompat.getColor(context, R.color.colorAccent))
        return shape
    }

    private fun createFocused(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f)
        shape.alpha = 100
        shape.setColor(ContextCompat.getColor(context, R.color.colorAccent))
        return shape
    }

    private fun createEnableDrawable(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f)
        shape.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        return shape
    }

    private fun createDisableDrawable(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f)
        shape.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        return shape
    }
    fun setCallBack(callBackTTS: CallBackTTS){
        this.callBackTTS = callBackTTS
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        callBackTTS?.call(text.toString())
    }
}