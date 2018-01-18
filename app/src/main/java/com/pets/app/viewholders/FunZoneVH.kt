package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.Enums
import com.pets.app.common.ImageSetter
import com.pets.app.model.FunZone
import com.pets.app.utilities.DateFormatter

/**
 * Created by admin on 16/01/18.
 */
class FunZoneVH(itemView: View, clickListener: View.OnClickListener) : RecyclerViewHolder(itemView) {

    private val ivProfile: ImageView = itemView.findViewById<View>(R.id.ivProfile) as ImageView
    private val tvName: TextView = itemView.findViewById<View>(R.id.tvName) as TextView
    private val tvDate: TextView = itemView.findViewById<View>(R.id.tvDate) as TextView
    private val ivFunZone: ImageView = itemView.findViewById<View>(R.id.ivFunZone) as ImageView
    private val tvComment: TextView = itemView.findViewById<View>(R.id.tvComment) as TextView
    private val tvHelpful: TextView = itemView.findViewById<View>(R.id.tvHelpful) as TextView
    private val tvShare: TextView = itemView.findViewById<View>(R.id.tvShare) as TextView
    private val ivMore: ImageView = itemView.findViewById<View>(R.id.ivMore) as ImageView
    private val ivPlay: ImageView = itemView.findViewById<View>(R.id.ivPlay) as ImageView

    private val context: Context = itemView.context

    init {
        ivPlay.setOnClickListener(clickListener)
        ivMore.setOnClickListener(clickListener)
    }

    override fun onBindView(`object`: Any) {
        val funZone = `object` as FunZone
        ivPlay.tag = funZone
        ivMore.tag = funZone

        if (funZone.user != null) {
            if (!TextUtils.isEmpty(funZone.user.name)) {
                tvName.text = funZone.user.name
            } else {
                tvName.text = ""
            }
            ImageSetter.loadRoundedImage(context, funZone.user.profile_image, R.drawable.alert_placeholder, ivProfile)
        }

        tvDate.text = DateFormatter.getFormattedDate(DateFormatter.yyyy_mm_dd_HH_mm_ss, funZone.createdDate, DateFormatter.REVIEW_DATE_FORMAT)
        ImageSetter.loadImage(context, funZone.funZoneImage, R.drawable.alert_placeholder, ivFunZone)
        if (!TextUtils.isEmpty(funZone.type) && funZone.type == Enums.FunZoneType.VIDEO.name) {
            ivPlay.visibility = View.VISIBLE
            ImageSetter.loadImage(context, funZone.videoThumb, R.drawable.alert_placeholder, ivFunZone)
        } else {
            ivPlay.visibility = View.GONE
            ImageSetter.loadImage(context, funZone.funZoneImage, R.drawable.alert_placeholder, ivFunZone)
        }
    }
}