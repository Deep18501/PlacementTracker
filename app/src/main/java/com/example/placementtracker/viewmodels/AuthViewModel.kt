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


    init {
        println("Viewmodel started")
        Log.d(TAG,"Init Block of LoginViewModel")

    }

    companion object{
        const val TAG="LoginViewModel"
    }

    var stateLoginOrRegister= mutableStateOf(true)
        private set
    private val _firebaseUser = MutableLiveData<FirebaseUser?>()


    val currentUser get()=_firebaseUser
    private val eventsChannel = Channel<AllEvents>()
    val allEventsFlow = eventsChannel.receiveAsFlow()


    fun signInUser(email:String,password:String)=viewModelScope.launch {
        when{
            email.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(1))
            }
            password.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(2))
            }
            else->{
                actualSignInUser(email,password)
            }
        }
    }

    fun signUpUser(email: String,password: String,confirmPass:String)=viewModelScope.launch {
        when{
            email.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(1))
            }
            password.isEmpty()->{
                eventsChannel.send(AllEvents.ErrorCode(2))
            }
            password !=confirmPass->{
                eventsChannel.send(AllEvents.ErrorCode(3))
            }
            else->{
                actualSignUpUser(email, password)
            }
        }
    }

    private fun actualSignUpUser(email: String, password: String) =viewModelScope.launch{
        try {
            val user=repository.signUpWithEmailPassword(email, password)
            user?.let {
                _firebaseUser.postValue(it)
                eventsChannel.send(AllEvents.Message("Sign Up Success"))
            }
        }catch (e:Exception){
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG, "signInUser: ${error[1]}")
            eventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    private fun actualSignInUser(email: String, password: String) =viewModelScope.launch{
        try{
            val user =repository.signInWithEmailPassword(email, password)
            user?.let {
                _firebaseUser.postValue(it)
                eventsChannel.send(AllEvents.Message("Login Success"))
            }
        }catch (e:Exception){
            val error = e.toString().split(":").toTypedArray()
            Log.d(TAG,"signInUser: ${error[1]}")
            eventsChannel.send(AllEvents.Error(error[1]))
        }
    }

    fun getCurrentUser() = viewModelScope.launch {
        val user = repository.getCurrentUser()
        _firebaseUser.postValue(user)
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
}