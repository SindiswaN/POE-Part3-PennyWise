package vcmsa.projects.sindiswasinazo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.sindiswasinazo.databinding.ItemCategoryTotalBinding
import vcmsa.projects.sindiswasinazo.ui.CategoryTotalsActivity.CategoryTotal

class CategoryTotalAdapter : RecyclerView.Adapter<CategoryTotalAdapter.ViewHolder>() {

    private val items = mutableListOf<CategoryTotal>()

    fun submitList(list: List<CategoryTotal>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCategoryTotalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryTotal) {
            binding.categoryNameTextView.text = item.name
            binding.totalAmountTextView.text = "$${"%.2f".format(item.total)}"
            binding.categoryColorView.setBackgroundColor(android.graphics.Color.parseColor(item.color))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryTotalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
