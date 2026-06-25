package ObjectClasses

class ItemToDo(
    val id : String? = null,
    val description : String? = null,
    var fait : Boolean = false
) {

    override fun toString(): String {
        return ("Item is ${description} and it is currently set to ${fait}.")
    }

}