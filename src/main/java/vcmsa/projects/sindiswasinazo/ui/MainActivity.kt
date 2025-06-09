package vcmsa.projects.sindiswasinazo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import vcmsa.projects.sindiswasinazo.auth.LoginActivity
import vcmsa.projects.sindiswasinazo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setupButtons()
    }

    private fun setupButtons() {
        binding.addCategoryButton.setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }

        binding.addEntryButton.setOnClickListener {
            startActivity(Intent(this, AddEntryActivity::class.java))
        }

        binding.viewEntriesButton.setOnClickListener {
            startActivity(Intent(this, EntriesActivity::class.java))
        }

        binding.setGoalsButton.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        binding.viewCategoryTotalsButton.setOnClickListener {
            startActivity(Intent(this, CategoryTotalsActivity::class.java))
        }

        binding.viewBudgetFeedbackButton.setOnClickListener {
            startActivity(Intent(this, BudgetFeedbackActivity::class.java))
        }

        binding.viewGamificationButton.setOnClickListener {
            startActivity(Intent(this, GamificationActivity::class.java))
        }

        binding.viewGraphButton.setOnClickListener {
            startActivity(Intent(this, SpendingGraphActivity::class.java))
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
