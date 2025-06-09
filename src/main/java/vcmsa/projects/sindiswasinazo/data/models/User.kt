package vcmsa.projects.sindiswasinazo.data.models

data class User(
    val uid: String = "",
    val email: String = "",
    val username: String = "",
    val profileImage: String? = null,
    val totalSavings: Double = 0.0,
    val streakDays: Int = 0,
    val lastLoginDate: String = ""
)