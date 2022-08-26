package com.example.logisticsestimate.data.remote.api

import android.util.Log
import com.example.logisticsestimate.base.App
import com.example.logisticsestimate.R
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * SSL 인증서 설정
 */

class SSLCertificateHelper {
    private val cf = CertificateFactory.getInstance("X.509")
    private val caInput = App.context.resources.openRawResource(R.raw.mydomain)
    private val ca = caInput.use {
        cf.generateCertificate(it) as X509Certificate
    }

    private val keyStoreType = KeyStore.getDefaultType()
    private val keyStore = KeyStore.getInstance(keyStoreType).apply {
        load(null, null)
        setCertificateEntry("ca", ca)
    }

    private val tmAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
    private val tmf = TrustManagerFactory.getInstance(tmAlgorithm).apply {
        init(keyStore)
    }

    private val context = SSLContext.getInstance("TLS").apply {
        init(null, tmf.trustManagers, null)
    }

    fun setSSLTrust(builder: OkHttpClient.Builder, target: String): OkHttpClient.Builder {
        builder.sslSocketFactory(context.socketFactory, tmf.trustManagers[0] as X509TrustManager)

        builder.hostnameVerifier { hostname, _ ->
            if (hostname.contentEquals(target)) {
                Log.d(SSLCertificateHelper::class.java.name, "Approving certificate for host $hostname");
                true
            } else {
                Log.d(SSLCertificateHelper::class.java.name, "fail $hostname");
                false
            }
        }

        return builder
    }
}