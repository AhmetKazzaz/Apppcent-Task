package com.example.appcent.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object Util {

    fun searchEvent(firebaseAnalytics: FirebaseAnalytics, searchWord: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT, searchWord)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    fun launchEvent(firebaseAnalytics: FirebaseAnalytics) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.CONTENT, "App Launched")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}