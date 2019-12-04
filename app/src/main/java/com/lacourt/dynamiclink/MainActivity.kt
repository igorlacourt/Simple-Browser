package com.lacourt.dynamiclink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class MainActivity : AppCompatActivity(), View.OnKeyListener {
    var siteEditText: EditText? = null
    var myWebView: WebView? = null
    var site: String? = null

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(event != null) {
            if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                event.getKeyCode() == KeyEvent.KEYCODE_ENTER
            ) {

                if (!event.isShiftPressed()) {
                    Log.v("AndroidEnterKeyActivity", "Enter Key Pressed!");
                    if(v?.getId() == R.id.site_path)
                        loadPage(siteEditText?.text.toString())
                    return true
                }

            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        siteEditText = findViewById<EditText>(R.id.site_path)
        myWebView = findViewById(R.id.webview)
        site = "https://www.meupag.com.br"

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    if (task.result != null){
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
            .addOnFailureListener { e->
                Log.d("link-log", "getDynamicLink:onFailure", e)
            }

    }

    private fun loadPage(link: String) {
        myWebView?.loadUrl(link)
    }
}
