package vcmsa.projects.sindiswasinazo.data.models

data class Goal(
    val id: String = "",
    var categoryId: String? = null, // null for overall budget
    val minAmount: Double = 0.0,
    val maxAmount: Double = 0.0,
    var currentAmount: Double = 0.0, // ✅ Changed from val to var
    val period: String = "monthly" // "weekly", "monthly", "yearly"
)
