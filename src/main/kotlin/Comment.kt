import java.time.LocalDateTime

data class Comment(
    val id: Int,
    val uId: Int,
    val noteId: Int,
    val date: LocalDateTime,
    val message: String,
    val replyTo: Int?,
    var deleted:Boolean = false
)
