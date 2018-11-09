package com.android.goodapplications.shira.viewModel

import android.arch.lifecycle.ViewModel
import com.android.goodapplications.shira.model.*
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