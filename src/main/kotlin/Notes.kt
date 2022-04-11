import java.time.LocalDateTime

object Notes {
    private var noteId: Int = 0
    private var commentId: Int = 0
    private var notes: MutableList<Note> = mutableListOf()
    private var comments: MutableList<Comment> = mutableListOf()


    class NoteNotFoundException(message: String) : RuntimeException("Note not found!\n Note Id = $message")
    class CommentNotFoundException(message: String) : RuntimeException("Comment not found!\n Comment Id = $message")
    class CommentDeletedException(message: String) : RuntimeException("Comment deleted!\n Comment Id = $message")
    class CommentNotDeletedException(message: String) : RuntimeException("Comment not deleted!\n Comment Id = $message")

    fun add(
        title: String,
        text: String,
        privacyView: String = "all",
        privacyComment: String = "all"
    ): Int {
        notes.add(Note(++noteId, title, text, LocalDateTime.now(), 0, 0, "url", privacyView, privacyComment))
        return noteId
    }

    fun createComment(noteId: Int, replyTo: Int? = null, message: String): Int {
        val note = notes.find { it.id == noteId }

        if (note != null) {
            comments.add(Comment(++commentId, 123, noteId, LocalDateTime.now(), message, replyTo))
            notes[notes.indexOf(note)] = note.copy(comments = note.comments + 1)
            return commentId
        } else throw NoteNotFoundException(noteId.toString())
    }


    fun delete(noteId: Int): Boolean {
        return if (notes.removeIf { it.id == noteId }) {
            if (notes.find { it.id == noteId }?.comments != 0 && comments.removeIf { it.noteId == noteId }) {
                true
            } else throw CommentNotFoundException("Indefinite id")
        } else throw NoteNotFoundException(noteId.toString())
    }

    fun deleteComment(commentId: Int): Boolean {
        val comment = comments.find { it.id == commentId }

        return if (comment != null) {
            if (!comment.deleted) {
                comments[comments.indexOf(comment)].deleted = true
                true
            } else throw CommentDeletedException(commentId.toString())
        } else throw CommentNotFoundException(commentId.toString())

    }

    fun edit(
        noteId: Int,
        title: String,
        text: String,
        privacyView: String = "all",
        privacyComment: String = "all"
    ): Boolean {
        val note = notes.find { it.id == noteId }

        return if (note != null) {
            notes[notes.indexOf(note)] =
                note.copy(title = title, text = text, privacyView = privacyView, privacyComment = privacyComment)
            true
        } else throw NoteNotFoundException(noteId.toString())
    }

    fun editComment(commentId: Int, message: String): Boolean {
        val comment = comments.find { it.id == commentId }

        return if (comment != null) {
            if (!comment.deleted) {
                comments[comments.indexOf(comment)] = comment.copy(message = message)
                true
            } else throw CommentDeletedException(commentId.toString())
        } else throw CommentNotFoundException(commentId.toString())
    }


    fun get(noteIds: MutableList<Int>, count: Int = noteIds.size, sort: Boolean = true): List<Note> {

        val takenNotes = notes.filter { noteIds.remove(it.id) }.take(count)
        if (noteIds.isNotEmpty()) throw NoteNotFoundException(noteIds.toString())

        return if (sort) {
            takenNotes.sortedBy { it.date }
        } else
            takenNotes.sortedByDescending { it.date }
    }

    fun getById(noteId: Int): Note {
        return notes.find { it.id == noteId } ?: throw NoteNotFoundException(noteId.toString())
    }

    fun getComments(noteId: Int, count: Int = getById(noteId).comments, sort: Boolean = true): List<Comment> {
        val takenComments =
            comments.filter { (it.noteId == noteId && !it.deleted) }
                .ifEmpty { throw NoteNotFoundException(noteId.toString()) }
                .take(count)

        return if (sort) {
            takenComments.sortedBy { it.date }
        } else
            takenComments.sortedByDescending { it.date }
    }

    fun restoreComment(commentId: Int): Boolean {
        val comment = comments.find { it.id == commentId }

        return if (comment != null) {
            if (comment.deleted) {
                comments[comments.indexOf(comment)] = comment.copy(deleted = true)
                true
            } else throw CommentNotDeletedException(commentId.toString())
        } else throw CommentNotFoundException(commentId.toString())
    }

    fun clear() {
        noteId = 0
        commentId = 0
        notes = mutableListOf()
        comments = mutableListOf()
    }
}
