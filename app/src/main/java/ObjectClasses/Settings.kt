package ObjectClasses

object Settings {
    public var pseudo : String = "Pseudo"
    public var profilActuel = ProfilListeToDo(pseudo, mutableListOf())
    public var listOfUsers : MutableList<ProfilListeToDo> = mutableListOf()
    public var url : String = "http://tomnab.fr/todo-api/"
}