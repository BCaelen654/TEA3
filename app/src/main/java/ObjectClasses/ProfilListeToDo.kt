package ObjectClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore

@Entity(tableName = "user")
data class ProfilListeToDo(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,

    @ColumnInfo(name = "pseudo")
    var login: String, // Utilisation de login pour cohérence avec le reste du code

    @ColumnInfo(name = "password")
    var password: String = ""
) {
    @Ignore
    var mesListesToDo : MutableList<ListeToDo> = mutableListOf()

    // Constructeur pour compatibilité
    constructor(login: String, mesListesToDo: MutableList<ListeToDo>) : this(0, login, "") {
        this.mesListesToDo = mesListesToDo
    }

    override fun toString(): String {
        return ("le profil de ${login} a les listes suivantes : ${mesListesToDo}")
    }
}
