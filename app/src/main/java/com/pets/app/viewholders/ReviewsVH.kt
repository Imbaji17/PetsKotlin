package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.model.Reviews
import com.pets.app.utilities.DateFormatter

/**
 * Created by admin on 11/01/18.
 */
class ReviewsVH(itemView: View, clickListener: View.OnClickListener) : RecyclerViewHolder(itemView) {

    private val ivReview: ImageView = itemView.findViewById<View>(R.id.ivReview) as ImageView
    private val tvName: TextView = itemView.findViewById<View>(R.id.tvName) as TextView
    private val tvDate: TextView = itemView.findViewById<View>(R.id.tvDate) as TextView
    private val ratingBar: RatingBar = itemView.findViewById<View>(R.id.ratingBar) as RatingBar
    private val tvComment: TextView = itemView.findViewById<View>(R.id.tvComment) as TextView
    private val ivDelete: ImageView = itemView.findViewById<View>(R.id.ivDelete) as ImageView
    private val llReview: LinearLayout = itemView.findViewById<View>(R.id.llReview) as LinearLayout

    private val context: Context = itemView.context

    init {
        ivDelete.setOnClickListener(clickListener)
        llReview.setOnClickListener(clickListener)
    }

    override fun onBindView(`object`: Any) {
        val review = `object` as Reviews
        ivDelete.tag = review
        llReview.tag = review
        if (review.user != null) {
            if (!TextUtils.isEmpty(review.user.name)) {
                tvName.text = review.user.name
            } else {
                tvName.text = ""
            }
            ImageSetter.loadRoundedImage(context, review.user.profile_image, R.drawable.alert_placeholder, ivReview)
        }

        tvDate.text = DateFormatter.getFormattedDate(DateFormatter.yyyy_mm_dd_HH_mm_ss, review.createdDate, DateFormatter.REVIEW_DATE_FORMAT)
        ratingBar.rating = review.rating.toFloat()
        tvComment.text = if (!TextUtils.isEmpty(review.comment)) {
            review.comment
        } else {
            ""
        }
    }
}