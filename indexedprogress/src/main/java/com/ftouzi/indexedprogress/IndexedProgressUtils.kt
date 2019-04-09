package com.ftouzi.indexedprogress

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.drawable.VectorDrawable
import java.util.*

class IndexedProgressUtils {

    companion object {

        fun formatValue(value: Float, suffix: String?): String {
            var valueSuffix = suffix
            if (valueSuffix == null)
                valueSuffix = "%"

            return String.format("%s%s", String.format(Locale.getDefault(), "%.0f", value), valueSuffix)
        }

        fun getProgressTranslation(width: Int, value: Float): Float {
            return width * value * 0.01f
        }

        fun roundedRect(
            left: Float, top: Float, right: Float, bottom: Float, rxValue: Float, ryValue: Float,
            tl: Boolean, tr: Boolean, br: Boolean, bl: Boolean
        ): Path {
            var rx = rxValue
            var ry = ryValue
            val path = Path()
            if (rx < 0) rx = 0f
            if (ry < 0) ry = 0f
            val width = right - left
            val height = bottom - top
            if (rx > width / 2) rx = width / 2
            if (ry > height / 2) ry = height / 2
            val widthMinusCorners = width - 2 * rx
            val heightMinusCorners = height - 2 * ry

            path.moveTo(right, top + ry)
            if (tr)
                path.rQuadTo(0f, -ry, -rx, -ry)//top-right corner
            else {
                path.rLineTo(0f, -ry)
                path.rLineTo(-rx, 0f)
            }
            path.rLineTo(-widthMinusCorners, 0f)
            if (tl)
                path.rQuadTo(-rx, 0f, -rx, ry) //top-left corner
            else {
                path.rLineTo(-rx, 0f)
                path.rLineTo(0f, ry)
            }
            path.rLineTo(0f, heightMinusCorners)

            if (bl)
                path.rQuadTo(0f, ry, rx, ry)//bottom-left corner
            else {
                path.rLineTo(0f, ry)
                path.rLineTo(rx, 0f)
            }

            path.rLineTo(widthMinusCorners, 0f)
            if (br)
                path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner
            else {
                path.rLineTo(rx, 0f)
                path.rLineTo(0f, -ry)
            }

            path.rLineTo(0f, -heightMinusCorners)

            path.close()//Given close, last lineto can be removed.

            return path
        }

        fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
            val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
            return bitmap
        }
    }

}