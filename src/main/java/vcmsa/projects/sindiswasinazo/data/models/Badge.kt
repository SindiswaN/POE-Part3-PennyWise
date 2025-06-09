package vcmsa.projects.sindiswasinazo.data.models

data class Badge(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val achieved: Boolean = false,
    val criteriaType: String = "", // "budget" or "consistency"
    val criteriaValue: Int = 1, // e.g., days or budget goal
    val dateAchieved: String? = null
)