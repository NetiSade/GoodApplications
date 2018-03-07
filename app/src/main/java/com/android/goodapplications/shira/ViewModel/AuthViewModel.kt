package com.android.goodapplications.shira.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.android.goodapplications.shira.Model.*
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by nsade on 10-Dec-17.
 */
class AuthViewModel : ViewModel() {
    var model: GoodApplicationModel = GoodApplicationModel()

    fun getCurrentUser(): FirebaseUser? {
        return model.getCurrentUser()
    }

    fun getFirebaseAuth(): FirebaseAuth? {
        return model.getFirebaseAuth()
    }


}