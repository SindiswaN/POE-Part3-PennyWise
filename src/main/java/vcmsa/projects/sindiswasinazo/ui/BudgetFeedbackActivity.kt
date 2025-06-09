package vcmsa.projects.sindiswasinazo.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vcmsa.projects.sindiswasinazo.data.FirebaseRepository
import vcmsa.projects.sindiswasinazo.data.models.Goal
import vcmsa.projects.sindiswasinazo.data.models.Entry
import vcmsa.projects.sindiswasinazo.databinding.ActivityBudgetFeedbackBinding

class BudgetFeedbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetFeedbackBinding
    private val repository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetFeedbackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadBudgetFeedback()
    }

    private fun loadBudgetFeedback() {
        repository.getGoals { goalsMap ->
            repository.getEntries { entriesList ->

                val overallGoal = goalsMap["overall"]
                val categoryGoals = goalsMap.filterKeys { it != "overall" }.values.toList()

                val overallSpending = entriesList
                    .filter { it.type == "expense" }
                    .sumOf { it.amount }

                overallGoal?.currentAmount = overallSpending

                val categorySpending = categoryGoals.associate { goal ->
                    val total = entriesList
                        .filter { it.categoryId == goal.categoryId && it.type == "expense" }
                        .sumOf { it.amount }
                    goal.categoryId!! to total.also { goal.currentAmount = it }
                }

                updateFeedback(overallGoal, categoryGoals, categorySpending)
            }
        }
    }


    private fun updateFeedback(
        overallGoal: Goal?,
        categoryGoals: List<Goal>,
        categorySpending: Map<String, Double>
    ) {
        overallGoal?.let { goal ->
            val progress = (goal.currentAmount / goal.maxAmount).coerceAtMost(1.0)
            binding.overallProgressBar.progress = (progress * 100).toInt()

            when {
                goal.currentAmount < goal.minAmount -> {
                    binding.overallStatusTextView.text = "Under budget! Great job!"
                    binding.overallStatusTextView.setTextColor(Color.GREEN)
                }
                goal.currentAmount > goal.maxAmount -> {
                    binding.overallStatusTextView.text = "Over budget! Try to save more."
                    binding.overallStatusTextView.setTextColor(Color.RED)
                }
                else -> {
                    binding.overallStatusTextView.text = "Within budget. Good progress."
                    binding.overallStatusTextView.setTextColor(Color.BLUE)
                }
            }
        }

        val feedbackText = buildString {
            categoryGoals.forEach { goal ->
                val spent = categorySpending[goal.categoryId] ?: 0.0
                append("${goal.categoryId}:\n")
                append("Spent: $${"%.2f".format(spent)} of $${"%.2f".format(goal.maxAmount)}\n")
                append(
                    when {
                        spent < goal.minAmount -> "Under budget ✓\n\n"
                        spent > goal.maxAmount -> "Over budget ✗\n\n"
                        else -> "Within budget ○\n\n"
                    }
                )
            }
        }

        binding.categoryFeedbackTextView.text = feedbackText
    }
}
