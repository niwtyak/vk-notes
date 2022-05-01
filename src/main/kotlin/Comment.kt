import java.time.LocalDateTime

data class Comment(
    val id: Long,
    val noteId: Long,
    val date: LocalDateTime?,
    val message: String,
    val replyTo: Int?,
    var deleted:Boolean = false
)
