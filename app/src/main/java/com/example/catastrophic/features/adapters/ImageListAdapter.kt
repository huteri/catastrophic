package com.example.catastrophic.features.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.catastrophic.R
import com.example.catastrophic.model.ImageModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_image.*

class ImageListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer<ImageModel>(this, ImageDiffCallback())

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
        return differ.currentList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageViewHolder).onBind(position)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            (holder as ImageViewHolder).onPayloadChanged(position, payloads)
        }
    }

    fun updateData(list: List<ImageModel>) {
        differ.submitList(list.map { it.copy() })
    }

    inner class ImageViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!), LayoutContainer {

        fun onBind(position: Int) {
            val imageModel = differ.currentList[position]

            Glide.with(context).load(imageModel.url).into(ivImage)

        }

        fun onPayloadChanged(position: Int, payloads: MutableList<Any>) {

            val payload = payloads[0] as Bundle

            if(payload.containsKey(ImageDiffCallback.PAYLOAD_IMAGE_URL_CHANGED)) {
                Glide.with(context).load(payload.getString(ImageDiffCallback.PAYLOAD_IMAGE_URL_CHANGED)).into(ivImage)
            }

            if(payload.containsKey(ImageDiffCallback.PAYLOAD_SHOW_OVERLAY_CHANGED)) {
                val newOverlay = payload.getBoolean(ImageDiffCallback.PAYLOAD_SHOW_OVERLAY_CHANGED)

                if (newOverlay)
                    ivOverlay.visibility = View.VISIBLE
                else
                    ivOverlay.visibility = View.GONE
            }
        }
    }

}


class ImageDiffCallback :
    DiffUtil.ItemCallback<ImageModel>() {

    override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
        return oldItem.imageId == newItem.imageId
    }

    override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
        return oldItem.imageId == newItem.imageId && oldItem.showOverlay == newItem.showOverlay && oldItem.url == newItem.url
    }

    override fun getChangePayload(oldItem: ImageModel, newItem: ImageModel): Any? {
        val bundle = Bundle()

        if(oldItem.showOverlay != newItem.showOverlay)
            bundle.putBoolean(PAYLOAD_SHOW_OVERLAY_CHANGED, newItem.showOverlay)

        if(oldItem.url != newItem.url)
            bundle.putString(PAYLOAD_IMAGE_URL_CHANGED, newItem.url)

        return bundle
    }


    companion object {
        const val PAYLOAD_SHOW_OVERLAY_CHANGED = "show_overlay"
        const val PAYLOAD_IMAGE_URL_CHANGED = "image_urL"
    }

}
