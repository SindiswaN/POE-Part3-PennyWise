package vcmsa.projects.sindiswasinazo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.sindiswasinazo.R
import vcmsa.projects.sindiswasinazo.data.models.Entry
import java.text.NumberFormat
import java.util.*

class EntryAdapter : ListAdapter<Entry, EntryAdapter.EntryViewHolder>(EntryDiffCallback()) {

    var onItemClick: ((Entry) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = getItem(position)
        holder.bind(entry)
        holder.itemView.setOnClickListener { onItemClick?.invoke(entry) }
    }

    class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        private val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val noteTextView: TextView = itemView.findViewById(R.id.noteTextView)

        fun bind(entry: Entry) {
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())

            amountTextView.text = currencyFormat.format(entry.amount)
            categoryTextView.text = entry.categoryId
            dateTextView.text = entry.date
            noteTextView.text = entry.note

            amountTextView.setTextColor(
                if (entry.type == "expense") {
                    itemView.context.getColor(R.color.expense_color)
                } else {
                    itemView.context.getColor(R.color.income_color)
                }
            )
        }
    }
}

class EntryDiffCallback : DiffUtil.ItemCallback<Entry>() {
    override fun areItemsTheSame(oldItem: Entry, newItem: Entry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean {
        return oldItem == newItem
    }
}