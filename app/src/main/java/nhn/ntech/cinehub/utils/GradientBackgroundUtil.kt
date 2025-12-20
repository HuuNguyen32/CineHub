package nhn.ntech.cinehub.utils

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import androidx.core.graphics.toColorInt

object GradientBackgroundUtil {
    fun gradientBackground(gradientView: View){
        val colors = intArrayOf(
            "#FF0B1D30".toColorInt(), // Mốc 7% (Đậm 100%)
            "#000B1D30".toColorInt(), // Mốc 50% (Trong suốt 0%)
            "#B30B1D30".toColorInt(), // Mốc 69% (Đậm 70%)
            "#FF0B1D30".toColorInt()  // Mốc 95% (Đậm 100%)
        )
        val positions = floatArrayOf(0.07f, 0.50f, 0.69f, 0.95f)

        val paintDrawable = PaintDrawable().apply {
            shape = RectShape()
            shaderFactory = object : ShapeDrawable.ShaderFactory() {
                override fun resize(width: Int, height: Int): Shader {
                    return LinearGradient(
                        0f, 0f, 0f, height.toFloat(),
                        colors,
                        positions,
                        Shader.TileMode.CLAMP
                    )
                }
            }
        }

        gradientView.background = paintDrawable
    }
}