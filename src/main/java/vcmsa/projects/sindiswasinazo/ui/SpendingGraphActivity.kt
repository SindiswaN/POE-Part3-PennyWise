package vcmsa.projects.sindiswasinazo.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import vcmsa.projects.sindiswasinazo.R
import vcmsa.projects.sindiswasinazo.ui.views.CustomGraphView

class SpendingGraphActivity : AppCompatActivity() {

    private lateinit var customGraphView: CustomGraphView
    private lateinit var summaryTextView: TextView
    private lateinit var viewDetailsBtn: Button

    private val auth = FirebaseAuth.getInstance()
    private val dbRef = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_graph)

        customGraphView = findViewById(R.id.graphView)
        summaryTextView = findViewById(R.id.chartSummary)
        viewDetailsBtn = findViewById(R.id.viewDetailsBtn)

        // Set min/max goal lines (example values)
        customGraphView.setMinLine(100f)   // min goal, e.g., R100
        customGraphView.setMaxLine(1000f)  // max goal, e.g., R1000

        loadSpendingData()

        viewDetailsBtn.setOnClickListener {
            Toast.makeText(this, "View details not implemented yet.", Toast.LENGTH_SHORT).show()
            // You can start a new Activity here if you want
        }
    }

    private fun loadSpendingData() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid

        dbRef.child("entries").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryTotals = mutableMapOf<String, Float>()
                    var totalSpent = 0f

                    for (entrySnapshot in snapshot.children) {
                        val category =
                            entrySnapshot.child("categoryId").getValue(String::class.java)
                                ?: "Unknown"
                        val amount =
                            entrySnapshot.child("amount").getValue(Double::class.java)?.toFloat()
                                ?: 0f

                        if (amount > 0f) {
                            categoryTotals[category] =
                                categoryTotals.getOrDefault(category, 0f) + amount
                            totalSpent += amount
                        }
                    }

                    if (categoryTotals.isEmpty()) {
                        Toast.makeText(
                            this@SpendingGraphActivity,
                            "No spending data found",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val categories = categoryTotals.keys.toList()
                    val amounts = categoryTotals.values.toList()

                    customGraphView.setBarChartData(categories, amounts)
                    summaryTextView.text = "Total spent this month: R%.2f".format(totalSpent)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@SpendingGraphActivity,
                        "Failed to load data: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}





