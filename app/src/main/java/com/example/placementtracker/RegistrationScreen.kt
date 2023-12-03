package com.example.placementtracker

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.placementtracker.ui.theme.Bluecolor
import com.example.placementtracker.utils.toaster
import com.example.placementtracker.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val userState by rememberUpdatedState(authViewModel.currentUser)

    var stateLoginOrRegister = authViewModel.stateLoginOrRegister.value

    val eventsChannel = authViewModel.allEventsFlow
    val snackbarHostState = remember { SnackbarHostState() }
    if (userState != null) {
        navController.navigate(Routes.HOME_SCREEN)
    }
    LaunchedEffect(eventsChannel) {
        eventsChannel.collect { event ->
            when (event) {
                is AuthViewModel.AllEvents.Message -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "Dismiss"
                    )
                }
                // Handle other events as needed
                is AuthViewModel.AllEvents.Error -> {
                    snackbarHostState.showSnackbar(
                        message = event.error,
                        actionLabel = "Dismiss"
                    )
                }

                is AuthViewModel.AllEvents.ErrorCode -> {
                    val errorMessage = authViewModel.getErrorMessage(event.code)
                    snackbarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = "Dismiss"
                    )
                }

                else -> {
                    snackbarHostState.showSnackbar(
                        message = "Some Unexpected Error Occured",
                        actionLabel = "Dismiss"
                    )
                }
            }


        }
    }

    val signUpZIndex by animateFloatAsState(if (stateLoginOrRegister) 1f else 0f)
    val registerZIndex by animateFloatAsState(if (!stateLoginOrRegister) 1f else 0f)
    val offset by animateDpAsState(
        targetValue = if (stateLoginOrRegister) (-60).dp else 60.dp,
        animationSpec = tween(durationMillis = 300), label = "btn"
    )
    val signUpButtonColors = if (stateLoginOrRegister) {
        Bluecolor
    } else {
        Color.White
    }
    val registerButtonColors = if (!stateLoginOrRegister) {
        Bluecolor
    } else {
        Color.White
    }
    var txtEmail by rememberSaveable { mutableStateOf("") }
    var txtName by rememberSaveable { mutableStateOf("") }
    var txtRollNo by rememberSaveable { mutableStateOf("") }
    var txtbranch by rememberSaveable { mutableStateOf("") }
    var txtPassword by rememberSaveable { mutableStateOf("") }
    var txtCPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoggingIn by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fREmail = remember { FocusRequester() }
    val fRPassword = remember { FocusRequester() }
    val fRCPassword = remember { FocusRequester() }
    val fRPhone = remember { FocusRequester() }
    var loginButton = remember { FocusRequester() }

    var signUpButton = remember { FocusRequester() }
    val showDialog = remember { mutableStateOf(false) }
    if (showDialog.value)
        CustomDialog(value = "", setShowDialog = {
            showDialog.value = it
        }) {
            authViewModel.verifySendPasswordReset(it)
        }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.fillMaxWidth())
        }) {

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "logo",
                    modifier = Modifier.size(175.dp)
                )
            }
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.White)
                    .clip(RoundedCornerShape(32))
                    .animateContentSize()
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,

                    ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .width(245.dp)
                                .height(45.dp)
                                .background(Color.LightGray, shape = RoundedCornerShape(24.dp))
                        )
                        Box(
                            modifier = Modifier
                                .width(125.dp)
                                .height(50.dp)
                                .offset(x = offset)
                                .background(Bluecolor, shape = RoundedCornerShape(24.dp))
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .wrapContentSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .zIndex(signUpZIndex)
                                    .offset(x = (-60).dp)
                                    .clickable { authViewModel.stateLoginOrRegister.value = true }
                                    .background(
                                        Color.Transparent,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                            ) {
                                Text(
                                    text = "Log In",
                                    modifier = Modifier.padding(
                                        horizontal = 36.dp,
                                        vertical = 5.dp
                                    ),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = registerButtonColors
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .zIndex(signUpZIndex)
                                    .offset(x = 60.dp)
                                    .clickable { authViewModel.stateLoginOrRegister.value = false }
                                    .background(
                                        Color.Transparent,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                            ) {
                                Text(
                                    text = "Sign Up",
                                    modifier = Modifier.padding(
                                        horizontal = 36.dp,
                                        vertical = 5.dp
                                    ),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = signUpButtonColors
                                )
                            }
                        }
                    }



                    Spacer(modifier = Modifier.height(16.dp))
                    if (!stateLoginOrRegister) {
                        OutlinedTextField(
                            value = txtName,
                            onValueChange = { txtName = it },
                            label = { Text(text = "Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            maxLines = 1,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Person Icon"
                                )
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    fREmail.requestFocus()
                                })
                        )

                    }
                    OutlinedTextField(
                        value = txtEmail,
                        onValueChange = { txtEmail = it },
                        label = { Text(text = "Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .focusRequester(fREmail),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon"
                            )
                        },
                        keyboardActions = KeyboardActions(
                            onNext = {
                                if (stateLoginOrRegister) {
                                    fRPassword.requestFocus()
                                } else {
                                    fRPhone.requestFocus()
                                }
                            })
                    )
                    if (!stateLoginOrRegister) {
                        OutlinedTextField(
                            value = txtRollNo,
                            onValueChange = { txtRollNo = it },
                            label = { Text(text = "Roll No") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .focusRequester(fRPhone),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Next
                            ),
                            maxLines = 1,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "Face Icon"
                                )
                            },
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    fRPassword.requestFocus()
                                })
                        )
                        Demo_ExposedDropdownMenuBox(){
                            txtbranch=it
                        }
                    }
                    OutlinedTextField(
                        value = txtPassword,
                        onValueChange = { txtPassword = it },
                        label = { Text(text = "Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .focusRequester(fRPassword),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Image(
                                    painter = if (passwordVisible) painterResource(id = R.mipmap.ic_visiblity_en) else painterResource(
                                        id = R.mipmap.ic_visiblity_di
                                    ),
                                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                                )
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Password Icon"
                            )
                        },
                        keyboardActions = KeyboardActions(
                            onNext = {
                                if (stateLoginOrRegister) {
                                    loginButton.requestFocus()
                                } else {
                                    fRCPassword.requestFocus()
                                }
                            })
                    )
                    if (!stateLoginOrRegister) {
                        OutlinedTextField(
                            value = txtCPassword,
                            onValueChange = { txtCPassword = it },
                            label = { Text(text = "Confirm Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .focusRequester(fRCPassword),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            maxLines = 1,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Confirm Password Icon"
                                )
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardActions = KeyboardActions(
                                onNext = {
                                    signUpButton.requestFocus()
                                })
                        )
                    }
                    if (stateLoginOrRegister) {
                        Text(
                            text = "Forgot Password", modifier = Modifier.clickable {
                                showDialog.value = true
                            }, color = Color.Black,
                            textAlign = TextAlign.Right
                        )
                        Button(
                            onClick = {
                                authViewModel.signInUser(txtEmail, txtPassword)

                            },
                            colors = ButtonDefaults.buttonColors(Bluecolor),
                            modifier = Modifier
                                .focusable()
                                .focusRequester(loginButton)
                        ) {
                            if (isLoggingIn) {
                                CircularProgressIndicator() // Show the progress bar
                            } else {
                                Text(
                                    text = "Login In",
                                    modifier = Modifier.padding(horizontal = 30.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                authViewModel.signUpUser(
                                    txtEmail,
                                    txtName,
                                    txtRollNo,
                                    txtbranch,
                                    txtPassword,
                                    txtCPassword
                                )
                            },
                            colors = ButtonDefaults.buttonColors(Bluecolor),
                            modifier = Modifier
                                .focusable()
                                .focusRequester(signUpButton)
                        ) {
                            if (isLoggingIn) {
                                CircularProgressIndicator() // Show the progress bar
                            } else {
                                Text(
                                    text = "Sign Up",
                                    modifier = Modifier.padding(horizontal = 30.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(value: String, setShowDialog: (Boolean) -> Unit, setValue: (String) -> Unit) {

    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf(value) }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(20.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Enter Your Email",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = "",
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(20.dp)
                            )
                        },
                        placeholder = { Text(text = "Enter Email") },
                        value = txtField.value,
                        onValueChange = {
                            txtField.value = it
                        })

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                        Button(
                            onClick = {
                                if (txtField.value.isEmpty()) {
                                    txtFieldError.value = "Field can not be empty"
                                    return@Button
                                }
                                setValue(txtField.value)
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demo_ExposedDropdownMenuBox(
    results:(String)->Unit
) {
    val context = LocalContext.current
    val branches = arrayOf("Others", "CSE", "ECE", "ME", "Civil", "EE", "Chemical")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(branches[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                branches.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            results(item)
                        }
                    )
                }
            }
        }
    }
}
