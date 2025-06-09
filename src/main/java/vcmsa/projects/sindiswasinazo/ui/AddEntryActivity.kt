package vcmsa.projects.sindiswasinazo.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import vcmsa.projects.sindiswasinazo.data.FirebaseRepository
import vcmsa.projects.sindiswasinazo.data.models.Entry
import vcmsa.projects.sindiswasinazo.databinding.ActivityAddEntryBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEntryBinding
    private val repository = FirebaseRepository()
    private var selectedCategoryId: String? = null
    private val calendar = Calendar.getInstance()

    private val PICK_IMAGE_REQ = 1001
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatePicker()
        loadCategories()

        binding.uploadReceiptButton.setOnClickListener {
            pickImage()
        }
        binding.saveEntryButton.setOnClickListener {
            saveEntry()
        }
    }

    private fun setupDatePicker() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.dateEditText.setText(dateFormat.format(calendar.time))
        binding.dateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    binding.dateEditText.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun loadCategories() {
        repository.getCategories { categories ->
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categoryNames
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            binding.categorySpinner.adapter = adapter

            if (categories.isNotEmpty()) {
                selectedCategoryId = categories[0].id
            }

            binding.categorySpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parentView: AdapterView<*>,
                        selectedItemView: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedCategoryId = categories[position].id
                    }

                    override fun onNothingSelected(parentView: AdapterView<*>) {
                        // No action needed
                    }
                }
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Receipt"), PICK_IMAGE_REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQ && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            binding.receiptImageView.setImageURI(selectedImageUri)
        }
    }

    private fun saveEntry() {
        val amtText = binding.amountEditText.text.toString()
        if (amtText.isBlank() || selectedCategoryId == null) {
            Snackbar.make(binding.root, "Fill all required fields", Snackbar.LENGTH_SHORT).show()
            return
        }

        val amount = amtText.toDoubleOrNull()
        if (amount == null) {
            Snackbar.make(binding.root, "Invalid amount", Snackbar.LENGTH_SHORT).show()
            return
        }

        val entry = Entry(
            amount = amount,
            categoryId = selectedCategoryId!!,
            date = binding.dateEditText.text.toString(),
            note = binding.noteEditText.text.toString(),
            type = if (binding.expenseRadioButton.isChecked) "expense" else "income",
            imageUrl = selectedImageUri?.toString()
        )

        repository.addEntry(entry) { success ->
            val message = if (success) "Entry saved" else "Save failed"
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            if (success) finish()
        }
    }
}
