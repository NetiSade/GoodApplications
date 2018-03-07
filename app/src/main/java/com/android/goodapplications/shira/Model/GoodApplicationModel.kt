package com.android.goodapplications.shira.Model

import com.android.goodapplications.shira.Model.DAL.FirebaseDAL
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Created by nsade on 25-Feb-18.
 */
open class GoodApplicationModel()
{
    var firebaseDAL: FirebaseDAL = FirebaseDAL()

    fun getCurrentUser(): FirebaseUser? {
       return firebaseDAL.getCurrentUser()
    }

    fun getFirebaseAuth(): FirebaseAuth? {
        return firebaseDAL.getFirebaseAuth()
    }


}