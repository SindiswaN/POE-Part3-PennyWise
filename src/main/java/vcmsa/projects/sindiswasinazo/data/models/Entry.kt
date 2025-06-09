package vcmsa.projects.sindiswasinazo.data.models

data class Entry(
    val id: String = "",
    val amount: Double = 0.0,
    var categoryId: String = "",
    val date: String = "",
    val note: String = "",
    val type: String = "expense", // "expense" or "income"
    val imageUrl: String? = null
)
