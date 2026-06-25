package com.example.tea1

import ObjectClasses.ListeToDo
import ObjectClasses.ProfilListeToDo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.result.launch
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CustomAdapterItem(
    private val hash: String,
    private val idListe: String,
    private val listeToDo : ListeToDo
) : RecyclerView.Adapter<CustomAdapterItem.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView? = null
        var checkBox : CheckBox? = null

        init {
            textView = view.findViewById<TextView>(R.id.textViewItemi)
            checkBox = view.findViewById<CheckBox>(R.id.checkBoxItem)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_item_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //viewHolder.textView?.text = listeToDo.lesItems[position].description
        val item = listeToDo.lesItems[position]
        viewHolder.textView?.text = item.description

        // Gestion de la sauvegarde du CheckBox
        viewHolder.checkBox?.setOnCheckedChangeListener(null)
        viewHolder.checkBox?.isChecked = item.fait
        viewHolder.checkBox?.setOnCheckedChangeListener { _, isChecked ->
            item.fait = isChecked
            val context = viewHolder.itemView.context
            val db = AppDatabase.getDatabase(context)

            kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    val idItem = item.id
                    val doneValue = if (isChecked) "1" else "0"

                    // Tentative de mise à jour auprès de l'API
                    TeaApi.retrofitService.updateItem(idListe, idItem, doneValue, hash)
                    
                    // Succès : mise à jour SQLite (toSync = false)
                    item.toSync = false
                    db.itemDao().update(item)
                    android.util.Log.i("PMR", "Update API réussie pour : ${item.description}")
                } catch (e: Exception) {
                    // Échec : on garde la modif en local et on marque pour synchro future
                    item.toSync = true
                    db.itemDao().update(item)
                    android.util.Log.e("PMR", "Offline : Item marqué pour synchro future")
                }
            }
        }
    }



    override fun getItemCount(): Int {
        return listeToDo.lesItems.size
    }
}