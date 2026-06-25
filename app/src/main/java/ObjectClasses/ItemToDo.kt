package ObjectClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemToDo(
    @PrimaryKey
    val id : String, // L'ID de l'API est une String

    @ColumnInfo(name = "description")
    val description : String? = null,

    @ColumnInfo(name = "fait")
    var fait : Boolean = false,

    @ColumnInfo(name = "listId")
    val listId: String, // Lien vers la liste parente

    @ColumnInfo(name = "toSync")
    var toSync: Boolean = false // Flag pour le mode offline
) {
    override fun toString(): String {
        return ("Item is ${description} and it is currently set to ${fait}.")
    }
}
