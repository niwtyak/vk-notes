import java.time.LocalDateTime

object NotesService:CrudService<Note> {
    private var noteId: Long = 0
    private var notes: MutableList<Note> = mutableListOf()

    fun clear() {
        noteId = 0
        notes = mutableListOf()
    }

    override fun add(entity: Note): Long {
        notes.add(Note(++noteId, entity.title, entity.text, LocalDateTime.now(), 0, 0, "url"))
        return noteId
    }

    override fun delete(id: Long):Boolean {
        return if (notes.removeIf { it.id == noteId }) {
            if (notes.find { it.id == noteId }?.comments != 0 ) {
                for (comment:Comment in CommentsService.read(mutableListOf(id),0,true)){
                    CommentsService.delete(comment.id)
                }
                true
            } else throw Exeptions.CommentNotFoundException("Indefinite id")
        } else throw Exeptions.NoteNotFoundException(noteId.toString())
    }

    override fun edit(entity: Note):Boolean {
        val note = notes.find { it.id == noteId }

        return if (note != null) {
            notes[notes.indexOf(note)] =
                note.copy(title = entity.title, text = entity.text)
            true
        } else throw Exeptions.NoteNotFoundException(noteId.toString())
    }

    override fun read(id: MutableList<Long>, count: Int , sort: Boolean): List<Note> {

        val takenNotes = notes.filter { id.remove(it.id) }.take(count)
        if (id.isNotEmpty()) throw Exeptions.NoteNotFoundException(id.toString())

        return if (sort) {
            takenNotes.sortedBy { it.date }
        } else
            takenNotes.sortedByDescending { it.date }
    }

    override fun getById(id: Long): Note = notes.find { it.id == id } ?: throw Exeptions.NoteNotFoundException(id.toString())

}
