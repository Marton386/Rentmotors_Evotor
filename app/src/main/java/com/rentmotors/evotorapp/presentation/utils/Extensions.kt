package com.rentmotors.evotorapp.presentation.utils

import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.rentmotors.evotorapp.R

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.showErrorDialog(error: String) {
    val alertDialogBuilder = AlertDialog.Builder(requireContext()).setView(R.layout.dialog_message)
    val dialog = alertDialogBuilder.show()

    dialog.findViewById<TextView>(R.id.tvMessage)?.text = error
    dialog.findViewById<Button>(R.id.btnOk)?.setOnClickListener {
        dialog.dismiss()
    }
}