import java.time.LocalDateTime

object CommentsService : CrudService<Comment> {
    private var commentId: Long = 0
    private var comments: MutableList<Comment> = mutableListOf()

    fun clear() {
        commentId = 0
        comments = mutableListOf()
    }

    override fun add(entity: Comment): Long {
        val note = NotesService.getById(entity.noteId)

        comments.add(Comment(++commentId,  entity.noteId, LocalDateTime.now(), entity.message, entity.replyTo))
        NotesService.edit(note.copy(comments = note.comments + 1))
        return commentId

    }

    override fun delete(id: Long ): Boolean {
        val comment = comments.find { it.id == id }

        return if (comment != null) {
            if (!comment.deleted) {
                comments[comments.indexOf(comment)].deleted = true
                true
            } else throw Exeptions.CommentDeletedException(id.toString())
        } else throw Exeptions.CommentNotFoundException(id.toString())

    }

    override fun edit(entity: Comment): Boolean {
        val comment = comments.find { it.id == commentId }

        return if (comment != null) {
            if (!comment.deleted) {
                comments[comments.indexOf(comment)] = comment.copy(message = entity.message)
                true
            } else throw Exeptions.CommentDeletedException(commentId.toString())
        } else throw Exeptions.CommentNotFoundException(commentId.toString())
    }

    override fun read(id: MutableList<Long>, count: Int, sort: Boolean): List<Comment> {

        var takenComments =
            comments.filter { (it.noteId == id.first() && !it.deleted) }
                .ifEmpty { throw Exeptions.NoteNotFoundException(id.toString()) }

        if (count!=0)
            takenComments=takenComments.take(count)

        return if (sort) {
            takenComments.sortedBy { it.date }
        } else
            takenComments.sortedByDescending { it.date }
    }

    override fun getById(id: Long): Comment = comments.find { it.id == id } ?: throw Exeptions.NoteNotFoundException(id.toString())


    fun restore(id: Long): Boolean {
        val comment = comments.find { it.id == id }

        return if (comment != null) {
            if (comment.deleted) {
                comments[comments.indexOf(comment)] = comment.copy(deleted = true)
                true
            } else throw Exeptions.CommentNotDeletedException(id.toString())
        } else throw Exeptions.CommentNotFoundException(id.toString())
    }
}