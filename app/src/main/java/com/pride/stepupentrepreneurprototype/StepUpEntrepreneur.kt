package com.pride.stepupentrepreneurprototype

import android.app.Application
import android.net.Uri
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class StepUpEntrepreneur : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.getApps(this)

        val builder = Picasso.Builder(this)
        builder.listener(fun(_: Picasso, _: Uri, exception: Exception){
            exception.printStackTrace()
        })
        val built = builder.build()
        built.setIndicatorsEnabled(false)
        built.isLoggingEnabled = true
        Picasso.setSingletonInstance(built)
    }
}