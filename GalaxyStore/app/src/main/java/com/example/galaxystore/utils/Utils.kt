package com.example.galaxystore.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.example.galaxystore.databinding.ProgressDialogBinding
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate.ProgressInfo
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Utils {

    private var firebaseAuth : FirebaseAuth?=null
    private var dialog: AlertDialog?=null

    fun getFirebaseAuth() : FirebaseAuth {
        if (firebaseAuth==null){
            firebaseAuth = FirebaseAuth.getInstance()
         }
        return firebaseAuth!! }

    fun showToast(context: Context,message: String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }

    fun showDialog(context: Context,message:String)
    {
        val progress = ProgressDialogBinding.inflate(LayoutInflater.from(context))
        progress.status.text = message
        dialog = AlertDialog.Builder(context).setView(progress.root).setCancelable(false).create()
        dialog!!.show() }

    fun hideDialog()    {   dialog!!.dismiss()   }

    fun getCurrentUserId() : String
    {
        return FirebaseAuth.getInstance().currentUser?.uid.toString() }

    fun getRandomId() : String
    {
        return (1..25).map{(('A'..'Z')+('a'..'z')+('0'..'9')).random()}.joinToString("") }

    fun getCurrentDate() : String{
       val currentDate =LocalDate.now()
       val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return currentDate.format(formatter)
    }

}