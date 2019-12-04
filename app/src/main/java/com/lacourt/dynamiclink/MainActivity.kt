package com.lacourt.dynamiclink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    if (task.result != null){
                        Log.d("link-log", "complete, result NOT null")
                        if (task.result?.link != null) {
                            Log.d("link-log", "complete, link = ${task.result?.link}")
                        } else {
                            Log.d("link-log", "complete, link IS null")
                        }
                    } else {
                        Log.d("link-log", "complete, result IS null")
                    }
                }
            }
            .addOnFailureListener { e->
                Log.d("link-log", "getDynamicLink:onFailure", e)
            }

    }
}
