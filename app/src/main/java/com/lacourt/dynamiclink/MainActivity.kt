package com.lacourt.dynamiclink

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    //, View.OnKeyListener {
    var siteEditText: EditText? = null
    var myWebView: WebView? = null
    var site: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        siteEditText = findViewById<EditText>(R.id.site_path)
        myWebView = findViewById(R.id.webview)
        myWebView?.webViewClient = WebViewClient()
        site = "https://meupag.com.br"

//        event.action == KeyEvent.ACTION_DOWN &&



        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    if (task.result != null) {
//                        textView.text = "complete, result NOT null"
                        Log.d("link-log", "complete, result NOT null")
                        if (task.result?.link != null) {
//                            textView.text = task.result?.link.toString()
                            Log.d("link-log", "complete, link = ${task.result?.link}")
                            site = task.result?.link.toString()
                            siteEditText?.setText(site)
                            site?.let { loadPage(it) }
                        } else {
//                            textView.text = "complete, link IS null"
                            Log.d("link-log", "complete, link IS null")
                        }
                    } else {
//                        textView.text = "complete, result IS null"
                        Log.d("link-log", "complete, result IS null")
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.d("link-log", "getDynamicLink:onFailure", e)
            }

        siteEditText?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d("link-log", "Enter Key Pressed!")
                loadPage(siteEditText?.text.toString())
                true
            }

            false
        }

    }

    private fun checkHost(url: String){
        Log.d("link-log", "url = $url")
        Log.d("link-log", "Uri.parse(url).host = $url")
        checkHostName(url)

    }

    private fun loadPage(url: String) {
        Log.d("link-log", "loadPage called, link = $url")

        if (url.contains("meupag")) {
            Log.d("link-log", "inside if")
            myWebView?.loadUrl(checkHostName(url))
        } else {
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(url)
//            val chooser = Intent.createChooser(intent, "Escolha um navegador")
//            startActivity(this, chooser, null)

            Log.d("link-log", "inside else")
//            Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                startActivity(this)
            }
        }
    }

    private fun checkHostName(url: String): String{
        var prefix = "http://"
        var lowerCaseUrl = url.toLowerCase()
        var result = ""
        if(lowerCaseUrl.contains("meupag.com.br")) {
            if(!lowerCaseUrl.contains(prefix))
                result = prefix + lowerCaseUrl
        }
        return if(result.subSequence(0, 20).contains("http://meupag.com.br")) {
            Log.d("link-log", "url substring = ${result.subSequence(0, 20)}" )
            result
        }
        else
            "http://meupag.com.br/parceiros"

    }
}

