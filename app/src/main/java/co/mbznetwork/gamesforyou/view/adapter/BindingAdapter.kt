package co.mbznetwork.gamesforyou.view.adapter

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import co.mbznetwork.gamesforyou.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

@BindingAdapter("imageUrl", "fitContents", requireAll = false)
fun setImageUrl(image: ImageView, url: String?, fitContents: Boolean = false) {
    if (url.isNullOrBlank()) {
        Glide.with(image.context)
            .load(R.drawable.ic_no_image)
            .centerCrop()
            .into(image)
    } else {
        Glide.with(image.context)
            .load(url).let {
                if (fitContents) it.fitCenter()
                else it.centerCrop()
            }.placeholder(R.drawable.ic_loading)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.ic_no_image)
            .into(image)
    }
}

@BindingAdapter("htmlText")
fun TextView.setHtmlText(htmlText: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
    } else Html.fromHtml(htmlText)
}
