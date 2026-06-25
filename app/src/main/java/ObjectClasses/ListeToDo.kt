package ObjectClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore

@Entity(tableName = "lists")
data class ListeToDo(
    @PrimaryKey
    var id: String = "", // L'ID de l'API est une String. Valeur par défaut pour création locale.

    @ColumnInfo(name = "titreListeToDo")
    var titreListeToDo: String
) {
    @Ignore
    var lesItems: MutableList<ItemToDo> = mutableListOf()

    // Constructeur secondaire pour l'initialisation facile dans le code existant
    constructor(id: String?, titreListeToDo: String, lesItems: MutableList<ItemToDo>) : this(id ?: "", titreListeToDo) {
        this.lesItems = lesItems
    }

    override fun toString(): String {
        return ("la liste ${titreListeToDo} contient : ${lesItems}")
    }
}
