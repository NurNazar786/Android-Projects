package com.example.galaxystore.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.galaxystore.models.Users
import com.example.galaxystore.utils.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {
    private val _verificationId = MutableStateFlow<String?>(null)

    private val _otpSent= MutableStateFlow(false)
    val otpSent = _otpSent

    private val _isLoginSuccess= MutableStateFlow(false)
    val isLoginSuccess = _isLoginSuccess

    private val _isLoggedInUser= MutableStateFlow(false)
    val isLoggedInUser = _isLoggedInUser

    init {
        Utils.getFirebaseAuth().currentUser?.let {
            _isLoggedInUser.value = true } }


    fun sendOtp(mobileNumber:String,activity: Activity)
    {
        val  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) { }

            override fun onVerificationFailed(e: FirebaseException) { }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                _verificationId.value = verificationId
                _otpSent.value = true } }

          val options = PhoneAuthOptions.newBuilder(Utils.getFirebaseAuth())
            .setPhoneNumber("+91$mobileNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
          PhoneAuthProvider.verifyPhoneNumber(options)
    }

     fun signInWithPhoneAuthCredential(otp: String, users: Users) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)
         FirebaseMessaging.getInstance().token.addOnCompleteListener{
             users.userToken = it.result

             Utils.getFirebaseAuth().signInWithCredential(credential)
                 .addOnCompleteListener { task ->
                     users.uid = Utils.getCurrentUserId()
                     if (task.isSuccessful) {
                         FirebaseDatabase.getInstance().reference.child("users").child(users.uid!!).setValue(users)
                         _isLoginSuccess.value = true
                     }
                 }
         }

    }
}