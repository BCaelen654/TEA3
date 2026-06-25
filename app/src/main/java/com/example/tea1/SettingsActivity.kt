package com.example.tea1

import ObjectClasses.Settings
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    private var btnPrefs: Button? = null
    private var textViewPseudo: TextView? = null
    private var textViewUrl: TextView? = null
    private var editText: EditText? = null
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pseudo : String? = Settings.pseudo
        textViewPseudo = findViewById(R.id.textViewPseudo2)
        textViewPseudo?.text = pseudo
    }

    fun clickBtnPrefs(view: View?) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    fun clickBtnPoints(view: View?) {
        btnPrefs = findViewById(R.id.btnPrefs2)
        if (btnPrefs?.visibility == View.INVISIBLE) {
            btnPrefs?.visibility = View.VISIBLE
        } else {
            btnPrefs?.visibility = View.INVISIBLE
        }
    }

    fun clickBtnOk(view: View?) {
        editText = findViewById(R.id.editTextText)
        url = editText?.text.toString()
        Log.d("url", url)
        Settings.url = url
        changerUrl(url)
    
        textViewUrl = findViewById(R.id.textViewPseudo3)
        textViewUrl?.text = url
    }
}