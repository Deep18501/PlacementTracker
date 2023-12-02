package com.example.placementtracker.viewmodels


import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import com.example.placementtracker.firebase_auth.FirebaseAuthenticator


class AuthViewModel:ViewModel() {
    private val repository = FirebaseAuthenticator()
    var currentUser by mutableStateOf<FirebaseUser?>(null)


    init {
        println("Viewmodel started")
        Log.d(TAG,"Init Block of LoginViewModel")
        getCurrentUser()
    }

    companion object{
        const val TAG="LoginViewModel"
        const val ERROR_CODE_EMPTY_EMAIL = 1
        const val ERROR_CODE_EMPTY_PASSWORD = 2
        const val ERROR_CODE_PASSWORD_MISMATCH = 3
        const val ERROR_CODE_EMPTY_NAME = 4
        const val ERROR_CODE_EMPTY_ROLLNO = 5
    }

    var stateLoginOrRegister= mutableStateOf(true)
        private set


    private val eventsChannel = Channel<AllEvents>()
    val allEventsFlow = eventsChannel.receiveAsFlow()

    var isLoggingIn = mutableStateOf(false)
    var isSigningUp = mutableStateOf(false)
    fun signInUser(email:String,password:String)=viewModelScope.launch {
        when{
            email.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(1))
                isLoggingIn.value=false
            }
            password.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(2))
                isLoggingIn.value=false
            }
            else->{
                actualSignInUser(email,password)
            }
        }
    }

    fun signUpUser(email: String,name:String,rollNo:String,password: String,confirmPass:String)=viewModelScope.launch {
        when{
            email.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(1))
                isSigningUp.value=false
            }
            password.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(2))
                isSigningUp.value=false
            }
            password !=confirmPass->{
                eventsChannel.send(AllEvents.ErrorCode(3))
                isSigningUp.value=false
            }
            name.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(4))
                isSigningUp.value=false
            }
            rollNo.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(5))
                isSigningUp.value=false
            }
            else->{
                actualSignUpUser(email, name,rollNo,password)
            }
        }
    }

    private fun actualSignUpUser(email: String,name:String,rollNo: String, password: String) =viewModelScope.launch{
        try {
            val user=repository.signUpWithEmailPassword(email,name,rollNo, password)
            user?.let {
                currentUser=it
                eventsChannel.send(AllEvents.Message("Sign Up Success"))
            }?: run {
                eventsChannel.send(AllEvents.Error("Sign Up Failed"))
                isSigningUp.value=false
            }
        }catch (e:Exception){
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            eventsChannel.send(AllEvents.Error(error[1]))
            isSigningUp.value=false
        }
    }

    private fun actualSignInUser(email: String, password: String) =viewModelScope.launch{
        try{
            val user =repository.signInWithEmailPassword(email, password)
            user?.let {
                currentUser=it
                eventsChannel.send(AllEvents.Message("Login Success"))
            }?: run {
                eventsChannel.send(AllEvents.Error("Login Failed"))
                isLoggingIn.value=false
            }
        }catch (e:Exception){
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG,"signInUser: ${error[1]}")
            eventsChannel.send(AllEvents.Error(error[1]))
            isLoggingIn.value=false
        }
    }

    fun getCurrentUser() = viewModelScope.launch {
        val user = repository.getCurrentUser()
        currentUser=user
    }

    fun verifySendPasswordReset(email: String){
        if(email.isEmpty()){
            viewModelScope.launch {
                eventsChannel.send(AllEvents.ErrorCode(1))
            }
        }else{
            sendPasswordResetEmail(email)
        }
    }

    private fun sendPasswordResetEmail(email: String) = viewModelScope.launch {
        try {
            val result = repository.sendPasswordReset(email)
            if (result){
                eventsChannel.send(AllEvents.Message("reset email sent"))
            }else{
                eventsChannel.send(AllEvents.Error("could not send password reset"))
            }
        }catch (e : java.lang.Exception){
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            eventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    sealed class AllEvents {
        data class Message(val message : String) : AllEvents()
        data class ErrorCode(val code : Int):AllEvents()
        data class Error(val error : String) : AllEvents()
    }
    fun getErrorMessage(errorCode: Int): String {
        return when (errorCode) {
            ERROR_CODE_EMPTY_EMAIL -> "Email cannot be empty"
            ERROR_CODE_EMPTY_PASSWORD -> "Password cannot be empty"
            ERROR_CODE_PASSWORD_MISMATCH -> "Passwords do not match"
            ERROR_CODE_EMPTY_NAME -> "Enter your name"
            ERROR_CODE_EMPTY_ROLLNO -> "Enter your roll no"
            // Add error messages for other error codes
            else -> "An error occurred"
        }
    }
}