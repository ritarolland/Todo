package com.example.todo.network

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

class CustomHostnameVerifier(private val allowedHost: String) : HostnameVerifier {
    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return hostname == allowedHost
    }
}