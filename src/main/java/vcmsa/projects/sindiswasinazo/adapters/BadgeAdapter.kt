package vcmsa.projects.sindiswasinazo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vcmsa.projects.sindiswasinazo.R
import vcmsa.projects.sindiswasinazo.data.models.Badge

class BadgeAdapter : ListAdapter<Badge, BadgeAdapter.BadgeViewHolder>(BadgeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_badge, parent, false)
        return BadgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: BadgeViewHolder, position: Int) {
        val badge = getItem(position)
        holder.bind(badge)
    }

    class BadgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val badgeImageView: ImageView = itemView.findViewById(R.id.badgeImageView)
        private val badgeNameTextView: TextView = itemView.findViewById(R.id.badgeNameTextView)
        private val badgeDescriptionTextView: TextView = itemView.findViewById(R.id.badgeDescriptionTextView)
        private val achievedTextView: TextView = itemView.findViewById(R.id.achievedTextView)

        fun bind(badge: Badge) {
            Glide.with(itemView.context)
                .load(badge.imageUrl)
                .placeholder(R.drawable.ic_badge_placeholder)
                .into(badgeImageView)

            badgeNameTextView.text = badge.name
            badgeDescriptionTextView.text = badge.description

            if (badge.achieved) {
                achievedTextView.text = "Achieved on ${badge.dateAchieved}"
                achievedTextView.visibility = View.VISIBLE
                itemView.alpha = 1f
            } else {
                achievedTextView.visibility = View.GONE
                itemView.alpha = 0.5f
            }
        }
    }
}

class BadgeDiffCallback : DiffUtil.ItemCallback<Badge>() {
    override fun areItemsTheSame(oldItem: Badge, newItem: Badge): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Badge, newItem: Badge): Boolean {
        return oldItem == newItem
    }
}