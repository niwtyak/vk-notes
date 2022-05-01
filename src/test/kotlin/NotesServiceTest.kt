import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class NotesServiceTest {

    @org.junit.After
    fun tearDown() {
        NotesService.clear()
        CommentsService.clear()
    }

    @Test
    fun add() {
        assertEquals(NotesService.add(Note(1, "title", "text", null, 0, 0, "url")), 1)
    }

    @Test
    fun createCommentToExistingNote() {
        val noteId = NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        assertEquals(CommentsService.add(Comment(1, noteId, null, "message", null)), 1)
    }

    @Test(expected = Exeptions.NoteNotFoundException::class)
    fun createCommentToNotExistingNote() {
        assertEquals(CommentsService.add(Comment(1, 1, null, "message", null)), 1)
    }

    @Test
    fun delete() {
        val noteId = NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        CommentsService.add(Comment(1, 1, null, "message", null))
        assertTrue(NotesService.delete(noteId))
    }

    @Test(expected = Exeptions.NoteNotFoundException::class)
    fun deleteNotExistingNote() {
        NotesService.delete(1)
    }

    @Test
    fun deleteComment() {
        val noteId = NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        val commentId = CommentsService.add(Comment(1, noteId, null, "message", null))
        assertTrue(CommentsService.delete(commentId))
    }

    @Test(expected = Exeptions.CommentNotFoundException::class)
    fun deleteNotExistingComment() {
        CommentsService.delete(1)
    }

    @Test(expected = Exeptions.CommentDeletedException::class)
    fun deleteDeletedComment() {
        NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        val commentId = CommentsService.add(Comment(1, 1, null, "message", null))
        assertTrue(CommentsService.delete(commentId))
        assertTrue(CommentsService.delete(commentId))
    }


    @Test
    fun edit() {
        val noteId = NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        assertTrue(NotesService.edit(Note(noteId, "Edited title", "Edited text", null, 0, 0, "url")))
    }

    @Test(expected = Exeptions.NoteNotFoundException::class)
    fun editNotExistingNote() {
        NotesService.edit(Note(1, "title", "text", null, 0, 0, "url"))
    }

    @Test
    fun editComment() {
        val noteId = NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        val commentId = CommentsService.add(Comment(1, noteId, null, "message", null))
        assertTrue(CommentsService.edit(Comment(commentId, noteId, null, "Edited message", null)))
    }

    @Test(expected = Exeptions.CommentNotFoundException::class)
    fun editNotExistingComment() {
        CommentsService.edit(Comment(1, 1, null, "Edited message", null))
    }

    @Test(expected = Exeptions.CommentDeletedException::class)
    fun editDeletedComment() {
        NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        val commentId = CommentsService.add(Comment(1, 1, null, "message", null))
        assertTrue(CommentsService.delete(commentId))
        assertTrue(CommentsService.edit(Comment(commentId, 1, null, "Edited message", null)))
    }


    @Test
    fun get() {

        val id1 = NotesService.add(Note(1, "Title 1", "text 1", null, 0, 0, "url"))
        Thread.sleep(500)
        val id2 = NotesService.add(Note(2, "Title 2", "text 2", null, 0, 0, "url"))
        Thread.sleep(500)
        val id3 = NotesService.add(Note(3, "Title 3", "text 3", null, 0, 0, "url"))
        Thread.sleep(500)
        val id4 = NotesService.add(Note(4, "Title 4", "text 4", null, 0, 0, "url"))

        val notes = NotesService.read(mutableListOf(id1, id2, id3, id4), 3,true)

        val actualNotes = listOf(
            Triple(id1, "Title 1", "text 1"),
            Triple(id2, "Title 2", "text 2"),
            Triple(id3, "Title 3", "text 3")
        )

        var success = true
        for ((i, note) in notes.withIndex()) {
            if (note.id != actualNotes[i].first || note.title != actualNotes[i].second || note.text != actualNotes[i].third)
                success = false
        }
        assertTrue(success)
    }

    @Test
    fun getByDescending() {
        val id1 = NotesService.add(Note(1, "Title 1", "text 1", null, 0, 0, "url"))
        Thread.sleep(500)
        val id2 = NotesService.add(Note(2, "Title 2", "text 2", null, 0, 0, "url"))
        Thread.sleep(500)
        val id3 = NotesService.add(Note(3, "Title 3", "text 3", null, 0, 0, "url"))
        Thread.sleep(500)
        val id4 = NotesService.add(Note(4, "Title 4", "text 4", null, 0, 0, "url"))

        val notes = NotesService.read(mutableListOf(id1, id2, id3, id4), 3,false)

        val actualNotes = listOf(
            Triple(id3, "Title 3", "text 3"),
            Triple(id2, "Title 2", "text 2"),
            Triple(id1, "Title 1", "text 1")
        )

        var success = true
        for ((i, note) in notes.withIndex()) {
            if (note.id != actualNotes[i].first || note.title != actualNotes[i].second || note.text != actualNotes[i].third)
                success = false
        }
        assertTrue(success)
    }

    @Test(expected = Exeptions.NoteNotFoundException::class)
    fun getNotExistingElement() {
        NotesService.read(mutableListOf(1, 1),0,true)
    }

    @Test
    fun getById() {
        val title = "Note #2"
        val text = "text"
        val id = NotesService.add(Note(1, title, text, null, 0, 0, "url"))
        val note = NotesService.getById(id)
        assert(note.id == id && note.title == title && note.text == text)
    }

    @Test(expected = Exeptions.NoteNotFoundException::class)
    fun getNotExistingNoteById() {
        NotesService.getById(1)
    }

    @Test
    fun getComments() {
        val noteId = NotesService.add(Note(1, "Title 1", "text 1", null, 0, 0, "url"))
        val id1 = CommentsService.add(Comment(1, noteId, null, "message 1", null))
        Thread.sleep(500)
        val id2 = CommentsService.add(Comment(1, noteId, null, "message 2", null))
        Thread.sleep(500)
        val id3 = CommentsService.add(Comment(1, noteId, null, "message 3", null))
        Thread.sleep(500)
        val id4 = CommentsService.add(Comment(1, noteId, null, "message 4", null))

        val comments = CommentsService.read(mutableListOf( noteId), 3,true)
        println(comments)

        val actualComments = listOf(
            Pair(id1, "message 1"),
            Pair(id2, "message 2"),
            Pair(id3, "message 3")
        )

        var success = true
        for ((i, comment) in comments.withIndex()) {
            if (comment.id != actualComments[i].first || comment.message != actualComments[i].second)
                success = false
        }
        assertTrue(success)
    }

    @Test
    fun getCommentsByDescending() {
        val noteId = NotesService.add(Note(1, "Title 1", "text 1", null, 0, 0, "url"))
        val id1 = CommentsService.add(Comment(noteId, 1, null, "message 1", null))
        Thread.sleep(500)
        val id2 = CommentsService.add(Comment(noteId, 1, null, "message 2", null))
        Thread.sleep(500)
        val id3 = CommentsService.add(Comment(noteId, 1, null, "message 3", null))
        Thread.sleep(500)
        val id4 = CommentsService.add(Comment(noteId, 1, null, "message 4", null))

        val comments = CommentsService.read(mutableListOf( noteId), 3,false)

        val actualComments = listOf(
            Pair(id3, "message 3"),
            Pair(id2, "message 2"),
            Pair(id1, "message 1")
        )

        var success = true
        for ((i, comment) in comments.withIndex()) {
            if (comment.id != actualComments[i].first || comment.message != actualComments[i].second)
                success = false
        }
        assertTrue(success)
    }

    @Test(expected = Exeptions.NoteNotFoundException::class)
    fun getCommentToNotExistingNote() {
        CommentsService.read(mutableListOf(1), 3, false)
    }

    @Test
    fun restoreComment() {
        val noteId = NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        val commentId = CommentsService.add(Comment(1, noteId, null, "message", null))
        CommentsService.delete(commentId)
        assertTrue(CommentsService.restore(commentId))
    }

    @Test(expected = Exeptions.CommentNotDeletedException::class)
    fun restoreNotDeletedComment() {
        val noteId = NotesService.add(Note(1, "title", "text", null, 0, 0, "url"))
        val commentId = CommentsService.add(Comment(1, noteId, null, "message", null))
        assertTrue(CommentsService.restore(commentId))
    }

    @Test(expected = Exeptions.CommentNotFoundException::class)
    fun restoreNotExistingComment() {
        CommentsService.restore(1)
    }

}