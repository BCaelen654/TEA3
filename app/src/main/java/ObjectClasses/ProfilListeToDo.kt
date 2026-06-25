package ObjectClasses

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "user")
class ProfilListeToDo(
    @PrimaryKey(autoGenerate = true)
    var uid: Int,

    @ColumnInfo(name = "pseudo")
    var pseudo: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "mesListesToDo")
    var mesListesToDo : MutableList<ListeToDo>
) {
    private fun AjouterListe(listeToDo: ListeToDo) {
        mesListesToDo.add(listeToDo)
    }

    override fun toString(): String {
        return ("le profil de ${pseudo} a les listes suivantes : ${mesListesToDo}")
    }
}