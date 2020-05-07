package com.example.ugame

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap

/*密封类 子类必须跟父类在同一个文件 使用方法跟枚举一样 跟枚举不一样的是 密封类 子类可以创建多个实例*/
sealed class Bar (context: Context){

    protected open val bmp = context.getDrawable(R.mipmap.bar)!!.toBitmap()

    protected abstract val srcRect : Rect

    private lateinit var dstRect : Rect

    private val paint = Paint()

    var h = 0F
        set(value) {
            field = value
            dstRect = Rect(0,0,w.toInt(),h.toInt())
        }
    var w = 0F
        set(value) {
            field = value
            dstRect = Rect(0,0,w.toInt(),h.toInt())
        }
    var x = 0F
        set(value) {
           view.x = value
            field = value
        }
    val y
       get() = view.y

    internal val view by lazy {
        BarView(context){
            it?.apply {
                drawBitmap(bmp,srcRect,dstRect,paint)
            }
        }
    }

}

/*屏幕上方障碍物*/
class UpBar(context: Context,container:ViewGroup):Bar(context){

    private val _srcRect by lazy(LazyThreadSafetyMode.NONE) {
        Rect(0, (bmp.height * (1 - (h / container.height))).toInt(), bmp.width, bmp.height)
    }

    override val srcRect: Rect
        get() = _srcRect
}

/**
 * 屏幕下方障碍物
 */
class DnBar(context: Context, container: ViewGroup) : Bar(context) {

    override val bmp = super.bmp.let {
        Bitmap.createBitmap(
            it, 0, 0, it.width, it.height,
            Matrix().apply { postRotate(-180F) }, true
        )
    }

    private val _srcRect by lazy(LazyThreadSafetyMode.NONE) {
        Rect(0, 0, bmp.width, (bmp.height * (h / container.height)).toInt())
    }

    override val srcRect: Rect
        get() = _srcRect
}

internal class BarView  constructor(
    context: Context,private val block:(Canvas?)->Unit
) : View(context) {

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        block(canvas)
    }
}