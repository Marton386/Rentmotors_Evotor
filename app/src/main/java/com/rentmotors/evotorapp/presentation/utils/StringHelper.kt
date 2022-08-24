package com.rentmotors.evotorapp.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class StringHelper @Inject constructor(
    private val context: Context
) {
    fun getString(@StringRes resId: Int) = context.getString(resId)
}