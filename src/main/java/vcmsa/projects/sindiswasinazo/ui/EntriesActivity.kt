package vcmsa.projects.sindiswasinazo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vcmsa.projects.sindiswasinazo.adapters.EntryAdapter
import vcmsa.projects.sindiswasinazo.data.FirebaseRepository
import vcmsa.projects.sindiswasinazo.data.models.Entry
import vcmsa.projects.sindiswasinazo.databinding.ActivityEntriesBinding


class EntriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntriesBinding
    private val repository = FirebaseRepository()
    private lateinit var adapter: EntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = EntryAdapter()
        binding.entriesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.entriesRecyclerView.adapter = adapter

        loadEntries()
    }

    private fun loadEntries() {
        lifecycleScope.launch {
            repository.getEntriesFlow().collect { entries ->
                adapter.submitList(entries)
                updateTotal(entries)
            }
        }
    }


    private fun updateTotal(entries: List<Entry>) {
        val totalExpense = entries.filter { it.type == "expense" }.sumOf { it.amount }
        val totalIncome = entries.filter { it.type == "income" }.sumOf { it.amount }
        val balance = totalIncome - totalExpense

        binding.totalExpenseTextView.text = "Total Expense: $${"%.2f".format(totalExpense)}"
        binding.totalIncomeTextView.text = "Total Income: $${"%.2f".format(totalIncome)}"
        binding.balanceTextView.text = "Balance: $${"%.2f".format(balance)}"
    }
}