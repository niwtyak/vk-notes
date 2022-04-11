import org.junit.Assert.*
import org.junit.Test

class NotesTest {

    @org.junit.After
    fun tearDown() {
        Notes.clear()
    }

    @Test
    fun add() {
        assertEquals(Notes.add("Note #2", "text"), 1)
    }

    @Test
    fun createCommentToExistingNote() {
        val noteId = Notes.add("Note #2", "text")
        assertEquals(Notes.createComment(noteId, message = "message"), 1)
    }

    @Test(expected = Notes.NoteNotFoundException::class)
    fun createCommentToNotExistingNote() {
        Notes.createComment(1, message = "message")
    }

    @Test
    fun delete() {
        val noteId = Notes.add("Note #1", "text")
        assertTrue(Notes.delete(noteId))
    }

    @Test(expected = Notes.NoteNotFoundException::class)
    fun deleteNotExistingNote() {
        Notes.delete(1)
    }

    @Test
    fun deleteComment() {
        val noteId = Notes.add("Note #1", "text")
        val commentId = Notes.createComment(noteId, message = "message")
        assertTrue(Notes.deleteComment(commentId))
    }

    @Test(expected = Notes.CommentNotFoundException::class)
    fun deleteNotExistingComment() {
        Notes.deleteComment(1)
    }

    @Test(expected = Notes.CommentDeletedException::class)
    fun deleteDeletedComment() {
        Notes.add("Note #1", "text")
        Notes.createComment(1, message = "message")
        Notes.deleteComment(1)
        Notes.deleteComment(1)
    }



    @Test
    fun edit() {
        val noteId = Notes.add("Note #1", "text")
        assertTrue(Notes.edit(noteId, "Edited title", "Edited text"))
    }

    @Test(expected = Notes.NoteNotFoundException::class)
    fun editNotExistingNote() {
        Notes.edit(1, "Edited title", "Edited text")
    }

    @Test
    fun editComment() {
        val noteId = Notes.add("Note #1", "text")
        val commentId = Notes.createComment(noteId, message = "message")
        assertTrue(Notes.editComment(commentId, "Edited message"))
    }

    @Test(expected = Notes.CommentNotFoundException::class)
    fun editNotExistingComment() {
        Notes.editComment(1, "Edited message")
    }

    @Test(expected = Notes.CommentDeletedException::class)
    fun editDeletedComment() {
        Notes.add("Note #1", "text")
        Notes.createComment(1, message = "message")
        Notes.deleteComment(1)
        Notes.editComment(1, "Edited message")
    }


    @Test
    fun get() {

        val id1 = Notes.add("Title 1", "text 1")
        Thread.sleep(500)
        val id2 = Notes.add("Title 2", "text 2")
        Thread.sleep(500)
        val id3 = Notes.add("Title 3", "text 3")
        Thread.sleep(500)
        val id4 = Notes.add("Title 4", "text 4")

        val notes = Notes.get(mutableListOf(id1, id2, id3, id4), 3)

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
        val id1 = Notes.add("Title 1", "text 1")
        Thread.sleep(500)
        val id2 = Notes.add("Title 2", "text 2")
        Thread.sleep(500)
        val id3 = Notes.add("Title 3", "text 3")
        Thread.sleep(500)
        val id4 = Notes.add("Title 4", "text 4")

        val notes = Notes.get(mutableListOf(id1, id2, id3, id4), 3, false)

        val actualNotes = listOf(
            Triple(id3, "Title 3", "text 3"),
            Triple(id2, "Title 2", "text 2"),
            Triple(id1, "Title 1", "text 1")
        )
        println(notes)

        var success = true
        for ((i, note) in notes.withIndex()) {
            if (note.id != actualNotes[i].first || note.title != actualNotes[i].second || note.text != actualNotes[i].third)
                success = false
        }
        assertTrue(success)
    }

    @Test(expected = Notes.NoteNotFoundException::class)
    fun getNotExistingElement() {
        Notes.get(mutableListOf(1, 1))
    }

    @Test
    fun getById() {
        val title = "Note #2"
        val text = "text"
        val id = Notes.add(title, text)
        val note = Notes.getById(id)
        assert(note.id == id && note.title == title && note.text == text)
    }

    @Test(expected = Notes.NoteNotFoundException::class)
    fun getNotExistingNoteById() {
        Notes.getById(1)
    }

    @Test
    fun getComments() {
        val noteId = Notes.add("Title 1", "text 1")
        val id1 = Notes.createComment(noteId, message = "message 1")
        Thread.sleep(500)
        val id2 = Notes.createComment(noteId, message = "message 2")
        Thread.sleep(500)
        val id3 = Notes.createComment(noteId, message = "message 3")
        Thread.sleep(500)
        val id4 = Notes.createComment(noteId, message = "message 4 ")

        val comments = Notes.getComments(noteId, 3)

        val actualComments = listOf(
            Pair(id1, "message 1"),
            Pair(id2, "message 2"),
            Pair(id3, "message 3")
        )

        var success = true
        for ((i, note) in comments.withIndex()) {
            if (note.id != actualComments[i].first || note.message != actualComments[i].second)
                success = false
        }
        assertTrue(success)
    }

    @Test
    fun getCommentsByDescending() {
        val noteId = Notes.add("Title 1", "text 1")
        val id1 = Notes.createComment(noteId, message = "message 1")
        Thread.sleep(500)
        val id2 = Notes.createComment(noteId, message = "message 2")
        Thread.sleep(500)
        val id3 = Notes.createComment(noteId, message = "message 3")
        Thread.sleep(500)
        val id4 = Notes.createComment(noteId, message = "message 4 ")

        val comments = Notes.getComments(noteId, 3, false)

        val actualComments = listOf(
            Pair(id3, "message 3"),
            Pair(id2, "message 2"),
            Pair(id1, "message 1")
        )

        var success = true
        for ((i, note) in comments.withIndex()) {
            if (note.id != actualComments[i].first || note.message != actualComments[i].second)
                success = false
        }
        assertTrue(success)
    }

    @Test(expected = Notes.NoteNotFoundException::class)
    fun getCommentToNotExistingNote() {
        Notes.getComments(1, 3, false)
    }

    @Test
    fun restoreComment() {
        val noteId = Notes.add("Note #1", "text")
        val commentId = Notes.createComment(noteId, message = "message")
        Notes.deleteComment(commentId)
        assertTrue(Notes.restoreComment(commentId))
    }

    @Test(expected = Notes.CommentNotDeletedException::class)
    fun restoreNotDeletedComment() {
        val noteId = Notes.add("Note #1", "text")
        val commentId = Notes.createComment(noteId, message = "message")
        Notes.restoreComment(commentId)
    }

    @Test(expected = Notes.CommentNotFoundException::class)
    fun restoreNotExistingComment() {
        Notes.restoreComment(1)
    }

}