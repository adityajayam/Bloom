package com.example.bloom.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.bloom.R
import com.example.bloom.data.Media
import com.example.bloom.databinding.SearchListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchListAdapter(private val context: Context, private val listener : (Media)->Unit) :
    ListAdapter<Media, SearchListAdapter.ItemViewHolder>(DiffCallback) {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(val itemViewBinding: SearchListItemBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root)

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout =
            SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        CoroutineScope(Dispatchers.Main).launch {
            val request = ImageRequest.Builder(context).data(item.src.portrait)
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.loading_animation)
                .target(holder.itemViewBinding.searchItemImage)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
            Log.e(TAG, item.id.toString())
            context.imageLoader.execute(request)
        }
        holder.itemViewBinding.searchItem.setOnLongClickListener {
            //ToDo download Image using background task
            listener(item)
            true
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem == newItem
            }
        }
        private const val TAG = "CartListAdapter"
    }
}