import java.time.LocalDateTime

data class Note(
    val id: Int,
    val title: String,
    val text: String,
    val date: LocalDateTime,
    val comments: Int,
    val readComments: Int = 0,
    val view_url: String,
    val privacyView: String,
    val privacyComment: String,
    val deleted: Boolean = false
)
