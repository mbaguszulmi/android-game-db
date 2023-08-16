package co.mbznetwork.gamesforyou.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import co.mbznetwork.gamesforyou.databinding.ItemGameBinding
import co.mbznetwork.gamesforyou.model.ui.GameItemDisplay

class GameAdapter(
    private val onItemClick: (Int) -> Unit
): PagingDataAdapter<GameItemDisplay, GameAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(
        private val binding: ItemGameBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GameItemDisplay) {
            binding.apply {
                game = item
                root.setOnClickListener {
                    onItemClick(item.id)
                }
                executePendingBindings()
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GameItemDisplay>() {
    override fun areItemsTheSame(oldItem: GameItemDisplay, newItem: GameItemDisplay): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GameItemDisplay, newItem: GameItemDisplay): Boolean {
        return oldItem == newItem
    }

}
