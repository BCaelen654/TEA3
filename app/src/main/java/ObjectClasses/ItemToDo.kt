package ObjectClasses

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "item")
class ItemToDo(
    @PrimaryKey(autoGenerate = true)
    val id : String? = null,

    @ColumnInfo(name = "description")
    val description : String? = null,

    @ColumnInfo(name = "fait")
    var fait : Boolean = false
) {

    override fun toString(): String {
        return ("Item is ${description} and it is currently set to ${fait}.")
    }
}