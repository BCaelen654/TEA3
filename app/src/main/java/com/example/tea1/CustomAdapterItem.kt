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

            kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    val idItem = item.id ?: return@launch // Si pas d'ID, on ne peut rien faire
                    val doneValue = if (isChecked) "1" else "0"

                    TeaApi.retrofitService.updateItem(idListe, idItem, doneValue, hash)
                    android.util.Log.i(
                        "PMR",
                        "Mise à jour réussie pour l'item : ${item.description}"
                    )
                } catch (e: Exception) {
                    android.util.Log.e("PMR", "Erreur update : ${e.message}")
                }
            }
        }
    }



    override fun getItemCount(): Int {
        return listeToDo.lesItems.size
    }
}