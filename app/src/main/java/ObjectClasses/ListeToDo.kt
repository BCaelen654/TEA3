package ObjectClasses

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "liste")
class ListeToDo(
    @PrimaryKey(autoGenerate = true)
    var id: String? = null,

    @ColumnInfo(name = "titreListeToDo")
    var titreListeToDo: String,

    @ColumnInfo(name = "lesItems")
    var lesItems: MutableList<ItemToDo>,
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