package com.example.tea1

import ObjectClasses.ListeToDo
import ObjectClasses.ProfilListeToDo
import ObjectClasses.Settings
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChoixListActivity : AppCompatActivity() {

    private var btnPrefs: Button? = null
    private var pseudo: String = ""
    private var editTextListe: EditText? = null

    var adapter: CustomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_choix_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        pseudo = Settings.pseudo
        editTextListe = findViewById(R.id.editTextTextListe)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        var isInList = false
        Settings.listOfUsers.forEach { element ->
            if (pseudo == element.login) {
                adapter = CustomAdapter(element)
                Settings.profilActuel = element
                isInList = true
            }
        }
        if (!isInList) {
            val nouvProfil = ProfilListeToDo(pseudo, mutableListOf())
            Settings.listOfUsers.add(nouvProfil)
            Settings.profilActuel = nouvProfil
            adapter = CustomAdapter(nouvProfil)
        }
        recyclerView.adapter = adapter

        // 1. Récupération du hash sauvegardé
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val hash = prefs.getString("hash", "") ?: ""

        // 2. Appel API dans une coroutine
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            try {
                val jsonResponse = TeaApi.retrofitService.getLists(hash)
                val rootObject = org.json.JSONObject(jsonResponse)
                val jsonArray = rootObject.getJSONArray("lists")

                val listesPourBD = mutableListOf<ListeToDo>()
                Settings.profilActuel.mesListesToDo.clear()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val nomListe = obj.getString("label")
                    val idListe = obj.getString("id")

                    val nouvelleListe = ListeToDo(idListe, nomListe, mutableListOf())
                    Settings.profilActuel.mesListesToDo.add(nouvelleListe)
                    listesPourBD.add(nouvelleListe)
                }
                
                // Sauvegarde en cache
                withContext(Dispatchers.IO) {
                    db.listDao().insertAll(*listesPourBD.toTypedArray())
                }
                
                adapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("PMR", "Erreur réseau, tentative de chargement du cache : ${e.message}")
                
                // Mode Offline : Chargement depuis la BD
                val cacheListes = withContext(Dispatchers.IO) {
                    db.listDao().getAll()
                }
                
                if (cacheListes.isNotEmpty()) {
                    Settings.profilActuel.mesListesToDo.clear()
                    Settings.profilActuel.mesListesToDo.addAll(cacheListes)
                    adapter?.notifyDataSetChanged()
                    Log.i("PMR", "Données chargées depuis le cache SQLite")
                }
            }
        }
    }

    fun clickBtnListe(view: View?) {
        val texte = editTextListe?.text.toString()
        if (texte.isNotBlank()) {
            val newList = ListeToDo(null, texte, mutableListOf())
            Settings.profilActuel.mesListesToDo.add(newList)
            adapter?.notifyItemInserted(Settings.profilActuel.mesListesToDo.size - 1)
            editTextListe?.text?.clear()
        }
    }

    fun clickBtnListei(view: View?) {
        val intent = Intent(this, ShowListActivity::class.java)
        startActivity(intent)
    }

    fun clickBtnPoints(view: View?) {
        btnPrefs = findViewById(R.id.btnPrefs3)
        if (btnPrefs?.visibility == View.INVISIBLE) {
            btnPrefs?.visibility = View.VISIBLE
        } else {
            btnPrefs?.visibility = View.INVISIBLE
        }
    }

    fun clickBtnPrefs(view: View?) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
