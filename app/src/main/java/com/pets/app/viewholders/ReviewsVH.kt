package com.pets.app.viewholders

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.pets.app.R
import com.pets.app.common.ImageSetter
import com.pets.app.model.Reviews

/**
 * Created by admin on 11/01/18.
 */
class ReviewsVH(itemView: View, clickListener: View.OnClickListener) : RecyclerViewHolder(itemView) {

    private val ivReview: ImageView = itemView.findViewById<View>(R.id.ivReview) as ImageView
    private val tvName: TextView = itemView.findViewById<View>(R.id.tvName) as TextView
    private val tvDate: TextView = itemView.findViewById<View>(R.id.tvDate) as TextView
    private val ratingBar: RatingBar = itemView.findViewById<View>(R.id.ratingBar) as RatingBar
    private val tvComment: TextView = itemView.findViewById<View>(R.id.tvComment) as TextView
    private val context: Context = itemView.context

    init {
    }

    override fun onBindView(`object`: Any) {
        val review = `object` as Reviews
        if (review.user != null) {
            if (!TextUtils.isEmpty(review.user.name)) {
                tvName.text = review.user.name
            } else {
                tvName.text = ""
            }

            ImageSetter.loadImage(context, review.user.profile_image, R.drawable.alert_placeholder, ivReview)
        }
        tvDate.text = review.createdDate
        ratingBar.rating = review.rating.toFloat()
        tvComment.text = if (!TextUtils.isEmpty(review.comment)) {
            review.comment
        } else {
            ""
        }
    }
}