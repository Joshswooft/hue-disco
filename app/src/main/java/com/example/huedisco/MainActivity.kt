package com.example.huedisco

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.huedisco.databinding.ActivityMainBinding
import java.util.regex.Pattern

import java.security.KeyStore

import java.io.IOException
import java.security.cert.Certificate

import java.security.cert.CertificateFactory

import java.security.cert.X509Certificate
import javax.net.ssl.*
import javax.net.ssl.HttpsURLConnection

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession
import com.android.volley.toolbox.HurlStack
import java.net.URL

import java.net.HttpURLConnection

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val PARTIAl_IP_ADDRESS: Pattern = Pattern.compile(
        "^((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])\\.){0,3}" +
                "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])){0,1}$"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val ipAddressTextWatcher = RegexMaskTextWatcher(PARTIAl_IP_ADDRESS.pattern());

        binding.editIpAddress.addTextChangedListener(ipAddressTextWatcher)

        binding.connectToBridge.setOnClickListener{connectToBridge()}
    }

    private fun getHostnameVerifier(): HostnameVerifier {
        return object : HostnameVerifier {
            override fun verify(hostname: String, session: SSLSession): Boolean {
                val hv = HttpsURLConnection.getDefaultHostnameVerifier()
                Log.d("hv", hv.toString())
                val ip = binding.editIpAddress.text.toString()
                Log.d("ip to verify: ", ip)
                Log.d("session host: ", session.peerHost)
//                return hv.verify(ip, session)
                return true // insecure as allows any SSL connection with cert
            }
        }
    }

    var hurlStack: HurlStack = object : HurlStack() {
        @Throws(IOException::class)
        override fun createConnection(url: URL): HttpURLConnection {
            val httpsURLConnection = super.createConnection(url) as HttpsURLConnection
            try {
                httpsURLConnection.sslSocketFactory = getSSLSocketFactory()
                httpsURLConnection.hostnameVerifier = getHostnameVerifier()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return httpsURLConnection
        }
    }

    private fun getSSLSocketFactory(): SSLSocketFactory {

            val cf = CertificateFactory.getInstance("X.509");
            val id = this.resources.getIdentifier("my_cert", "raw", this.packageName)

            val caInput = resources.openRawResource(id);
            var ca: Certificate;

            caInput.use { caInput ->
                ca = cf.generateCertificate(caInput) as X509Certificate;
                Log.e("CERT", "ca=" + (ca as X509Certificate).getSubjectDN());
            }


            val keyStoreType = KeyStore.getDefaultType();
            val keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);


            var tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            var tmf: TrustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            var context: SSLContext = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            return context.socketFactory;

    }


    private fun connectToBridge() {

//        validate IP: Patterns.IP_ADDRESS.matcher(url).matches();
        val ip = binding.editIpAddress.text
        Log.d("Ip address: ", ip.toString())

//        verify our connection to the bridge

        val url = "https://$ip/api/12345/config"

//        val url = "https://pokeapi.co/api/v2/ability/7/"
        val queue = Volley.newRequestQueue(this, hurlStack)
        Log.d("queue",  "created new request queue")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("Response: ", response.toString())
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                Log.d("Error: ", error.toString())
            }
        )

        queue.add(jsonObjectRequest)

//        TODO: connect to bridge
//        TODO: if successful then go to next page
    }



}