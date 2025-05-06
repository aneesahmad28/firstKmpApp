package viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Platform-agnostic ViewModel for the login screen
 */
class LoginViewModel {
    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> = _loginError

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        _isLoading.value = true
        _loginError.value = null

        // This would be replaced with your actual authentication logic
        // For example, API call to your backend
        // For demonstration, using a delay to simulate network request
        MainScope().launch {
            try {
                delay(1500) // Simulate network delay

                // Example authentication logic
                if (isValidCredentials(username, password)) {
                    onSuccess()
                } else {
                    _loginError.value = "Invalid username or password"
                }
            } catch (e: Exception) {
                _loginError.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // This would be replaced with your actual credential validation logic
    private fun isValidCredentials(username: String, password: String): Boolean {
        // Implement your actual validation logic here
        return true // For demonstration purposes
    }

    fun clearError() {
        _loginError.value = null
    }
}