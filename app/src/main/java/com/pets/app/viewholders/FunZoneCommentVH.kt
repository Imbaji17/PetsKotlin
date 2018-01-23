package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.model.FunZoneComment
import com.pets.app.utilities.DateFormatter

/**
 * Created by admin on 18/01/18.
 */

class FunZoneCommentVH(itemView: View, clickListener: View.OnClickListener) : RecyclerViewHolder(itemView) {

    private val ivProfile: ImageView = itemView.findViewById<View>(R.id.ivProfile) as ImageView
    private val tvName: TextView = itemView.findViewById<View>(R.id.tvName) as TextView
    private val tvDate: TextView = itemView.findViewById<View>(R.id.tvDate) as TextView
    private val tvComment: TextView = itemView.findViewById<View>(R.id.tvComment) as TextView
    private val ivDelete: ImageView = itemView.findViewById<View>(R.id.ivDelete) as ImageView

    private val context: Context = itemView.context

    init {
        ivDelete.setOnClickListener(clickListener)
    }

    override fun onBindView(`object`: Any) {
        val funZoneComment = `object` as FunZoneComment
        ivDelete.tag = funZoneComment

        if (funZoneComment.user != null) {
            if (!TextUtils.isEmpty(funZoneComment.user.name)) {
                tvName.text = funZoneComment.user.name
            } else {
                tvName.text = ""
            }
            ImageSetter.loadRoundedImage(context, funZoneComment.user.profile_image, R.drawable.alert_placeholder, ivProfile)
        }

        tvDate.text = DateFormatter.getFormattedDate(DateFormatter.yyyy_mm_dd_HH_mm_ss, funZoneComment.createdDate, DateFormatter.REVIEW_DATE_FORMAT)
        tvComment.text = if (!TextUtils.isEmpty(funZoneComment.comment)) {
            funZoneComment.comment
        } else {
            ""
        }
    }
}
