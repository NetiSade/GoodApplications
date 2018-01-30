package com.android.goodapplications.goodapplications.Model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.util.Log
import com.android.goodapplications.goodapplications.BR
import java.util.Date
/**
 * Created by nsade on 12-Nov-17.
 */
class Artist(var biography: String = "",
             var createdAt: Date = Date(),
             var gender: String = "",
             var name: String = "",
             var type: String = "",
             var updatedAt: Date = Date())