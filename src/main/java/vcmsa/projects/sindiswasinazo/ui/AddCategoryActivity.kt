package vcmsa.projects.sindiswasinazo.ui

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.sindiswasinazo.data.FirebaseRepository
import vcmsa.projects.sindiswasinazo.data.models.Category
import vcmsa.projects.sindiswasinazo.databinding.ActivityAddCategoryBinding

class AddCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding
    private val repository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.saveCategoryButton.setOnClickListener {
            val name = binding.categoryNameEditText.text.toString().trim()
            if (name.isEmpty()) {
                Snackbar.make(binding.root, "Please enter category name", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val category = Category(name = name, color = "#2196F3") // default blue

            CoroutineScope(Dispatchers.Main).launch {
                repository.addCategory(category) { success ->
                    if (success) {
                        Snackbar.make(binding.root, "Category added", Snackbar.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Snackbar.make(binding.root, "Failed to add category", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
