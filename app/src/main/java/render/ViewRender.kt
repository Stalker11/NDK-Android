package render

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import com.olegel.androidndkqt.R


class ViewRender {
    companion object{
        fun renderExoPlayer(@NonNull surface:View, @NonNull context:Activity, @NonNull size:RenderSize) {
            val dd = surface.layoutParams
            val displayMetrics = DisplayMetrics()
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            when(size){
                RenderSize.FULL_SCREEN ->{
                    dd.height = height
                    dd.width = width
                    surface.layoutParams = dd
                }
                RenderSize.HALF_SCREEN -> {
                    dd.height = height / 2
                    dd.width = width / 2
                    surface.layoutParams = dd
                }
                RenderSize.QUARTER_SCREEN -> {
                    dd.height = height / 4
                    dd.width = width / 4
                    surface.layoutParams = dd
                    }
                }
            Toast.makeText(context,"Click on surface", Toast.LENGTH_LONG).show()
        }
    }
}