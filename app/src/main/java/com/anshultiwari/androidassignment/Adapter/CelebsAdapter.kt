package com.anshultiwari.androidassignment.Adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.anshultiwari.androidassignment.Model.Celebrity
import com.anshultiwari.androidassignment.R
import com.bumptech.glide.Glide

class CelebsAdapter(private val mContext: Context, private val mCelebrityList: List<Celebrity>) : RecyclerView.Adapter<CelebsAdapter.ChatUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.celeb_item, parent, false)
        return ChatUserViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        val celeb = mCelebrityList[position]
        val height = celeb.height

        holder.heightTextView.text = centimeterToFeet(height)
        holder.ageTextView.text = String.format("%s years old", celeb.age)
        holder.popularityTextView.text = String.format("%s%% popularity", celeb.popularity)
        Glide.with(mContext).load(celeb.imageUrl).into(holder.celebImageView)

    }

    override fun getItemCount(): Int {
        return mCelebrityList.size
    }

    inner class ChatUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val heightTextView: TextView
        val ageTextView: TextView
        val popularityTextView: TextView
        val celebImageView: ImageView

        init {

            heightTextView = view.findViewById(R.id.celeb_height)
            ageTextView = view.findViewById(R.id.celeb_age)
            popularityTextView = view.findViewById(R.id.celeb_popularity)
            celebImageView = view.findViewById(R.id.celeb_image)

        }

    }

    private fun centimeterToFeet(centemeter: String): String {
        var feetPart = 0
        var inchesPart = 0
        if (!TextUtils.isEmpty(centemeter)) {
            val dCentimeter = java.lang.Double.valueOf(centemeter)
            feetPart = Math.floor(dCentimeter / 2.54 / 12).toInt()
            println(dCentimeter / 2.54 - feetPart * 12)
            inchesPart = Math.ceil(dCentimeter / 2.54 - feetPart * 12).toInt()
        }
        return String.format("%d' %d''", feetPart, inchesPart)
    }

    companion object {
        private val TAG = "CelebsAdapter"
    }

}
