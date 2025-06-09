package vcmsa.projects.sindiswasinazo.ui

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import vcmsa.projects.sindiswasinazo.data.FirebaseRepository
import vcmsa.projects.sindiswasinazo.data.models.Goal
import vcmsa.projects.sindiswasinazo.databinding.ActivityGoalsBinding

class GoalsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGoalsBinding
    private val repository = FirebaseRepository()
    private var selectedCategoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCategories()

        binding.saveGoalButton.setOnClickListener {
            saveGoal()
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            repository.getCategoriesFlow().collect { categories ->
                val categoryNames = listOf("Overall Budget") + categories.map { it.name }
                val categoryIds = listOf(null) + categories.map { it.id }

                binding.categorySpinner.adapter = ArrayAdapter(
                    this@GoalsActivity,
                    R.layout.simple_spinner_item,
                    categoryNames
                )

                binding.categorySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            selectedCategoryId = categoryIds[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            }
        }
    }

    private fun saveGoal() {
        val minAmountText = binding.minAmountEditText.text.toString()
        val maxAmountText = binding.maxAmountEditText.text.toString()

        if (minAmountText.isEmpty() || maxAmountText.isEmpty()) {
            Snackbar.make(binding.root, "Please fill all fields", Snackbar.LENGTH_SHORT).show()
            return
        }

        val minAmount = minAmountText.toDoubleOrNull() ?: run {
            Snackbar.make(binding.root, "Invalid minimum amount", Snackbar.LENGTH_SHORT).show()
            return
        }

        val maxAmount = maxAmountText.toDoubleOrNull() ?: run {
            Snackbar.make(binding.root, "Invalid maximum amount", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (minAmount >= maxAmount) {
            Snackbar.make(binding.root, "Minimum must be less than maximum", Snackbar.LENGTH_SHORT).show()
            return
        }

        val goal = Goal(
            categoryId = selectedCategoryId,
            minAmount = minAmount,
            maxAmount = maxAmount
        )

        repository.setGoal(goal) { success ->
            if (success) {
                Snackbar.make(binding.root, "Goal saved", Snackbar.LENGTH_SHORT).show()
                finish()
            } else {
                Snackbar.make(binding.root, "Failed to save goal", Snackbar.LENGTH_SHORT).show()
            }
        }

    }
}