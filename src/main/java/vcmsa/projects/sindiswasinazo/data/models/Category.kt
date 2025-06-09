package vcmsa.projects.sindiswasinazo.data.models

data class Category(
    var id: String? = null,
    var name: String = "",
    var icon: String? = null,
    var color: String = "#0000FF" // default blue if not customized
)
