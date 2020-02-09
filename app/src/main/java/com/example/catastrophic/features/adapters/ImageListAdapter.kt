package com.example.catastrophic.features.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.catastrophic.R
import com.example.catastrophic.model.ImageModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_image.*

class ImageListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<ImageModel>()
    private val showOverlay = mutableListOf<Int>()


    init {
        setHasStableIds(true)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_image,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageViewHolder).onBind(position)
    }

    fun updateData(list: List<ImageModel>) {

        val diffCallback = ImageDiffCallback(this.list, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.list.clear()
        this.list.addAll(list)

        diffResult.dispatchUpdatesTo(this)
    }

    fun updateOverlay(showOverlay: MutableList<Int>, firstPosition: Int, lastPosition: Int) {
        this.showOverlay.clear()
        this.showOverlay.addAll(showOverlay)

        notifyDataSetChanged()
    }

    inner class ImageViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {

        fun onBind(position: Int) {
            val imageModel = list[position]

            Glide.with(context).load(imageModel.getUrl()).into(ivImage)

            if (showOverlay.contains(position))
                ivOverlay.visibility = View.VISIBLE
            else
                ivOverlay.visibility = View.GONE

        }
    }

}


class ImageDiffCallback(
    private val oldList: List<ImageModel>,
    private val newList: List<ImageModel>
) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getImageId() == newList[newItemPosition].getImageId()
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].getImageId() == newList[newItemPosition].getImageId()
    }

}
