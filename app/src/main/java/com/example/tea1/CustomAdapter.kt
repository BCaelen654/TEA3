package com.example.tea1

import ObjectClasses.ProfilListeToDo
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val profilListeToDo: ProfilListeToDo) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var button: Button? = null

        init {
            button = view.findViewById<Button>(R.id.btnListei)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_list_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val laListe = profilListeToDo.mesListesToDo[position]

            // Affichage du titre (le 'label' de l'API)
            viewHolder.button?.text = laListe.titreListeToDo

            viewHolder.button?.setOnClickListener { view ->
                val intent = Intent(view.context, ShowListActivity::class.java)

                // On transmet l'ID de la liste pour la prochaine étape
                intent.putExtra("ID_LISTE", laListe.id)

                view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return profilListeToDo.mesListesToDo.size
    }
}