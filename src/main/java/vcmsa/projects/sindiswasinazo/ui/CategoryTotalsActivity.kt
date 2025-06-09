package vcmsa.projects.sindiswasinazo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import vcmsa.projects.sindiswasinazo.data.FirebaseRepository
import vcmsa.projects.sindiswasinazo.databinding.ActivityCategoryTotalsBinding
import vcmsa.projects.sindiswasinazo.ui.adapters.CategoryTotalAdapter

class CategoryTotalsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryTotalsBinding
    private val repository = FirebaseRepository()
    private lateinit var adapter: CategoryTotalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryTotalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CategoryTotalAdapter()
        binding.categoryTotalsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.categoryTotalsRecyclerView.adapter = adapter

        loadCategoryTotals()
    }

    private fun loadCategoryTotals() {
        lifecycleScope.launch {
            repository.getCategoriesFlow().collect { categories ->
                repository.getEntriesFlow().collect { entries ->
                    val categoryTotals = categories.map { category ->
                        val categoryEntries = entries.filter { it.categoryId == category.id && it.type == "expense" }
                        val total = categoryEntries.sumOf { it.amount }
                        CategoryTotal(category.name, total, category.color)
                    }
                    adapter.submitList(categoryTotals)
                    updateTotalExpense(categoryTotals)
                }
            }
        }
    }

    private fun updateTotalExpense(categoryTotals: List<CategoryTotal>) {
        val total = categoryTotals.sumOf { it.total }
        binding.totalExpenseTextView.text = "Total Expense: $${"%.2f".format(total)}"
    }

    data class CategoryTotal(
        val name: String,
        val total: Double,
        val color: String
    )
}
