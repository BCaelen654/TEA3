package com.example.tea1

import ObjectClasses.Settings
import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var btnOk: Button? = null
    private var btnPrefs: Button? = null
    private var editText: EditText? = null
    private var editText2: EditText? = null
    private var pseudo: String = ""
    private var mdp: String = ""

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // On affiche le bouton OK même hors ligne si on a déjà un hash en mémoire (mode offline)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val hash = prefs.getString("hash", "")
        
        if (verifReseau(this)) {
            Log.i("PMR","Réseau disponible")
            afficherBtnOk()
            syncOfflineData()
        } else if (!hash.isNullOrBlank()) {
            Log.i("PMR", "Mode Hors Ligne activé")
            afficherBtnOk()
        }
    }

    private fun syncOfflineData() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val hash = prefs.getString("hash", "") ?: ""
        if (hash.isBlank()) return

        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val itemsToSync = db.itemDao().getItemsToSync()
                itemsToSync.forEach { item ->
                    val doneValue = if (item.fait) "1" else "0"
                    TeaApi.retrofitService.updateItem(item.listId, item.id, doneValue, hash)

                    // Mise à jour locale pour marquer comme synchronisé
                    item.toSync = false
                    db.itemDao().update(item)
                    Log.i("PMR", "Item synchronisé automatiquement : ${item.description}")
                }
            } catch (e: Exception) {
                Log.e("PMR", "Sync auto échouée : ${e.message}")
            }
        }
    }

    fun afficherBtnOk() {
        btnOk = findViewById(R.id.btnOK)
        btnOk?.visibility = View.VISIBLE
    }

    fun clickBtnPoints(view: View?) {
        btnPrefs = findViewById(R.id.btnPrefs)
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

    fun clickBtnOk(view: View?) {
        //val intent = Intent(this, ChoixListActivity::class.java)

        editText = findViewById(R.id.editPseudoText)
        pseudo = editText?.text.toString()
        Log.d("pseudo", pseudo)
        Settings.pseudo = pseudo

        editText2 = findViewById(R.id.editPasswordText)
        mdp = editText2?.text.toString()
        lifecycleScope.launch {
            try {
                Log.i("PMR", "Tentative de connexion pour : $pseudo")
                val test = TestViewModel()
                val rawHash = test.authentication(pseudo, mdp)

            // Vérification du résultat
                Log.e("PMR", rawHash)
            if (rawHash != "false" && rawHash.isNotBlank()) {
                Log.i("PMR", "Connexion réussie ! Hash : $rawHash")
                val json = org.json.JSONObject(rawHash)
                val realHash = json.getString("hash")

                Log.i("PMR", "Vrai Hash extrait : $realHash")

                // Sauvegarde persistante
                val prefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                prefs.edit().putString("hash", realHash).apply()
                Settings.pseudo = pseudo

                // Navigation vers l'écran suivant (seulement si succès)
                val intent = Intent(this@MainActivity, ChoixListActivity::class.java)
                startActivity(intent)
            } else {
                Log.e("PMR", "Pseudo ou mot de passe incorrect")
            }
        } catch(e: Exception) {
            Log.e("PMR", "Erreur réseau, vérification du mode hors-ligne : ${e.message}")
            val prefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
            val realHash = prefs.getString("hash", "")
            if (!realHash.isNullOrBlank()) {
                Log.i("PMR", "Accès autorisé via le cache hors-ligne")
                Settings.pseudo = pseudo
                val intent = Intent(this@MainActivity, ChoixListActivity::class.java)
                startActivity(intent)
            } else {
                Log.e("PMR", "Impossible de se connecter et aucun cache disponible")
            }
        }
        }
    }


    sealed interface TeaUiState {
        data class Success(val hash: String) : TeaUiState
        object Error : TeaUiState
        object Loading : TeaUiState
    }

}
