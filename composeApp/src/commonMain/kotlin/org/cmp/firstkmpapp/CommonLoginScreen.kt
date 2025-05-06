package org.cmp.firstkmpapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import viewmodel.LoginViewModel


@Composable
fun CommonLoginScreen(
    onLoginClicked: (username: String, password: String) -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    isLoading: Boolean = false
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        // Title
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Error message if present
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.body2,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        // Username field
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                errorMessage = null
            },
            label = { Text("Username or Email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            ),
            isError = errorMessage != null
        )

        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .focusRequester(passwordFocusRequester)
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp &&
                        keyEvent.key == Key.Enter &&
                        username.isNotBlank() && password.isNotBlank()
                    ) {
                        focusManager.clearFocus()
                        validateAndLogin(username, password, onLoginClicked, errorMessage = { error ->
                            errorMessage = error
                        })
                        true
                    } else {
                        false
                    }
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    validateAndLogin(username, password, onLoginClicked, errorMessage = { error ->
                        errorMessage = error
                    })
                }
            ),
            visualTransformation = if (showPassword)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            trailingIcon = {
            },
            isError = errorMessage != null
        )

        // Forgot Password
        TextButton(
            onClick = onForgotPasswordClicked,
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 16.dp)
        ) {
            Text("Forgot Password?")
        }

        // Login Button
        Button(
            onClick = {
                validateAndLogin(username, password, onLoginClicked, errorMessage = { error ->
                    errorMessage = error
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading && username.isNotBlank() && password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colors.onPrimary,
                )
            } else {
                Text("Login")
            }
        }

        // Sign Up option
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an account?")
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = onSignUpClicked) {
                Text("Sign Up")
            }
        }
    }
}

/**
 * Validates credentials and triggers login if valid
 */
private fun validateAndLogin(
    username: String,
    password: String,
    onLoginClicked: (username: String, password: String) -> Unit,
    errorMessage: (String) -> Unit
) {
    when {
        username.isBlank() -> errorMessage("Username cannot be empty")
        password.isBlank() -> errorMessage("Password cannot be empty")
        password.length < 6 -> errorMessage("Password must be at least 6 characters")
        else -> onLoginClicked(username, password)
    }
}



/**
 * Example usage in your app
 */
@Composable
fun LoginScreenImplementation() {
    val viewModel = remember { LoginViewModel() }
    val isLoading by viewModel.isLoading
    val error by viewModel.loginError

    // Set up navigation based on your app's architecture
    val navigateToHome = {
        // Navigate to home screen
        println("Navigate to home screen")
    }

    val navigateToForgotPassword = {
        // Navigate to forgot password screen
        println("Navigate to forgot password screen")
    }

    val navigateToSignUp = {
        // Navigate to sign up screen
        println("Navigate to sign up screen")
    }

    CommonLoginScreen(
        onLoginClicked = { username, password ->
            viewModel.login(username, password, onSuccess = navigateToHome)
        },
        onForgotPasswordClicked = navigateToForgotPassword,
        onSignUpClicked = navigateToSignUp,
        isLoading = isLoading
    )

    // Show snackbar for errors
    LaunchedEffect(error) {
        error?.let {
            // Show error in snackbar or other UI component
            viewModel.clearError()
        }
    }
}