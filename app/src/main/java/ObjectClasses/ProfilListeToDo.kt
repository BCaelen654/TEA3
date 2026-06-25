package ObjectClasses

class ProfilListeToDo(
    public var login : String,
    var mesListesToDo : MutableList<ListeToDo>,
) {

    private fun AjouterListe(listeToDo: ListeToDo) {
        mesListesToDo.add(listeToDo)
    }

    override fun toString(): String {
        return ("le profil de ${login} a les listes suivantes : ${mesListesToDo}")
    }
}