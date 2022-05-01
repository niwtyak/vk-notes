interface CrudService<E> {
    fun add(entity: E): Long
    fun delete(id: Long):Boolean
    fun edit(entity: E):Boolean
    fun read(id:MutableList<Long>,count:Int, sort:Boolean): List<E>
    fun getById(id: Long): E
}