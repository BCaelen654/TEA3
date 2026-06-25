package ObjectClasses

class ListeToDo(
    public var id: String? = null,
    public var titreListeToDo: String,
    public var lesItems: MutableList<ItemToDo>,
) {

    private fun RechercherItem(description : String) : ItemToDo{
        for (i in 0..(lesItems.size)) {
            if (lesItems[i].description == description) {
                return lesItems[i]
            }
        }
        return  ItemToDo(null, null, false)
    }

    override fun toString(): String {
        return ("la liste ${titreListeToDo} contient : ${lesItems}")
    }
}