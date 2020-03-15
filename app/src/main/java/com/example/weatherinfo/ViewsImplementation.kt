package com.example.weatherinfo

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View

object ViewsImplementation {

    fun showAlertDialog(
        context: Context,
        title: String,
        message: String,
        onClickListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK",onClickListener)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}