package com.lacourt.dynamiclink

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Message
import android.webkit.*
import android.widget.*
import android.view.MenuItem
import java.nio.file.Files.delete


class MainActivity : AppCompatActivity() {
    //, View.OnKeyListener {
    var tvSite: TextView? = null
    var myWebView: WebView? = null
    var url: String? = null
    var loadingWebPage: ProgressBar? = null
    var webError: TextView? = null
    var backButton: ImageView? = null
    var menuButton: ImageView? = null
    var popup: PopupMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuButton = findViewById(R.id.web_menu_button)
        backButton = findViewById(R.id.btn_arrow)
        loadingWebPage = findViewById(R.id.loading_web_page)
        tvSite = findViewById(R.id.site_path)
        myWebView = findViewById(R.id.webview)
        myWebView?.settings?.javaScriptEnabled = true

        popup = PopupMenu(this, menuButton)
        val inflater = popup?.menuInflater
        inflater?.inflate(R.menu.web_pop_up_menu, popup?.menu)

        menuButton?.setOnClickListener {
            popup?.show()
        }

        popup?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.reload -> {
                    refresh()
                }
                R.id.share -> {
                    url?.let { sharePage(it) }
                }
            }
            true
        }

        myWebView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                loadingWebPage?.visibility = View.INVISIBLE
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                url: String?
            ): WebResourceResponse? {
//                tvSite?.setText(url)
                return super.shouldInterceptRequest(view, url)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
//                tvSite?.setText(url)
                return super.shouldInterceptRequest(view, request)
            }

            override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                return super.shouldOverrideKeyEvent(view, event)
            }

            override fun onSafeBrowsingHit(
                view: WebView?,
                request: WebResourceRequest?,
                threatType: Int,
                callback: SafeBrowsingResponse?
            ) {
                super.onSafeBrowsingHit(view, request, threatType, callback)
            }

            override fun hashCode(): Int {
                return super.hashCode()
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {

            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
//                super.onReceivedError(view, request, error)
//                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
//                    Log.d("web-error", "API greater than 23")
//                    Log.d("web-error", error?.errorCode.toString())
//                    Log.d("web-error", error?.description.toString())
//
//                    webError?.text = error.toString()
//                }
                myWebView?.loadUrl("https://www.google.com/search?q=${tvSite?.text}")
            }

            override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
            ): Boolean {
                return super.onRenderProcessGone(view, detail)
            }

            override fun onReceivedLoginRequest(
                view: WebView?,
                realm: String?,
                account: String?,
                args: String?
            ) {
                super.onReceivedLoginRequest(view, realm, account, args)
            }

            override fun equals(other: Any?): Boolean {
                return super.equals(other)
            }

            override fun toString(): String {
                return super.toString()
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)

            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                loadingWebPage?.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                super.onScaleChanged(view, oldScale, newScale)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
            }

            override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
                super.onUnhandledKeyEvent(view, event)
            }

            override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
                super.onReceivedClientCertRequest(view, request)
            }

            override fun onReceivedHttpAuthRequest(
                view: WebView?,
                handler: HttpAuthHandler?,
                host: String?,
                realm: String?
            ) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
            }

            override fun onTooManyRedirects(
                view: WebView?,
                cancelMsg: Message?,
                continueMsg: Message?
            ) {
                super.onTooManyRedirects(view, cancelMsg, continueMsg)
            }

            override fun onFormResubmission(
                view: WebView?,
                dontResend: Message?,
                resend: Message?
            ) {
                super.onFormResubmission(view, dontResend, resend)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
            }
        }

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
                            url = task.result?.link.toString()
                            tvSite?.text = url
                            url?.let { myWebView?.loadUrl(it) }
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

        backButton?.setOnClickListener {
            navigateBack()
        }

        tvSite?.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.d("link-log", "Enter Key Pressed!")
//                loadPage(tvSite?.text.toString())
                myWebView?.loadUrl(formatUrl(tvSite?.text.toString()))
                true
            }

            false
        }

    }

    private fun refresh() {

    }

    //    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
//        return super.onMenuItemSelected(featureId, item)
//    }
//
//    fun onMenuItemClick(item: MenuItem): Boolean {
//        when (item.getItemId()) {
//            R.id.archive -> {
//                archive(item)
//                return true
//            }
//            R.id.delete -> {
//                delete(item)
//                return true
//            }
//            else -> return false
//        }
//    }
    private fun sharePage(url: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            url
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    private fun navigateBack() {
        if (myWebView?.copyBackForwardList() != null) {
            if (myWebView!!.copyBackForwardList().currentIndex > 0) {
                myWebView!!.goBack()
            } else {
                // Your exit alert code, or alternatively line below to finish
                super.onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        navigateBack()
    }

    private fun formatUrl(url: String): String? {
        return "http://$url"
    }

    private fun checkHost(url: String) {
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
            Log.d("link-log", "inside else")
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                startActivity(this)
            }
        }
    }

    private fun checkHostName(url: String): String {
        var prefix = "http://"
        var lowerCaseUrl = url.toLowerCase()
        var result = ""
        if (lowerCaseUrl.contains("meupag.com.br")) {
            if (!lowerCaseUrl.contains(prefix))
                result = prefix + lowerCaseUrl
        }
        return if (result.subSequence(0, 20).contains("http://meupag.com.br")) {
            Log.d("link-log", "url substring = ${result.subSequence(0, 20)}")
            result
        } else
            "http://meupag.com.br/parceiros"
    }
}

