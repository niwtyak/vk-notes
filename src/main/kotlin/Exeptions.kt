class Exeptions {
    class NoteNotFoundException(message: String) : RuntimeException("Note not found!\n Note Id = $message")
    class CommentNotFoundException(message: String) : RuntimeException("Comment not found!\n Comment Id = $message")
    class CommentDeletedException(message: String) : RuntimeException("Comment deleted!\n Comment Id = $message")
    class CommentNotDeletedException(message: String) : RuntimeException("Comment not deleted!\n Comment Id = $message")
}