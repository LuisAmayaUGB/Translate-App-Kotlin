package com.c1ctech.translatetextexp

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edtSourceLangText: EditText = findViewById(R.id.edt_source_lang_text)
        val tvTargetLangText: TextView = findViewById(R.id.tv_target_lang_text)
        val btnTranslate: Button = findViewById(R.id.btn_translate)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI)
            .build()

        val englishHindiTranslator = Translation.getClient(options)
        getLifecycle().addObserver(englishHindiTranslator)


        btnTranslate.setOnClickListener({

            //make sure that our EditText is not empty
            if (!edtSourceLangText.text.isEmpty()) {

                progressBar.visibility = View.VISIBLE
                btnTranslate.visibility = View.GONE

                //Downloads the model files required for translation, if they are not already present,
                //when the given conditions are met.
                englishHindiTranslator.downloadModelIfNeeded()
                    .addOnSuccessListener {
                        Log.e(TAG, "Download Successful")
                        progressBar.visibility = View.GONE
                        btnTranslate.visibility = View.VISIBLE

                        //Translates the given input from the source language into the target language.
                        englishHindiTranslator.translate(edtSourceLangText.text.toString())
                            .addOnSuccessListener {
                                //Translation successful
                                tvTargetLangText.setText(it)
                            }
                            .addOnFailureListener {
                                //Error
                                Log.e(TAG, "Error: " + it.localizedMessage)
                            }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "Download Error: " + it.localizedMessage)
                        progressBar.visibility = View.GONE
                        btnTranslate.visibility = View.VISIBLE

                    }
            }
        })
    }
}