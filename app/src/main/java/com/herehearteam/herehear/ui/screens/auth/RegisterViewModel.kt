import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.herehearteam.herehear.data.model.UserRequestDto
import com.herehearteam.herehear.data.remote.api.ApiService
import com.herehearteam.herehear.domain.model.LoginState
import com.herehearteam.herehear.domain.model.RegisterState
import com.herehearteam.herehear.domain.model.SignInResult
import com.herehearteam.herehear.domain.model.User
import com.herehearteam.herehear.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterViewModel(
    private val apiService: ApiService,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState.asStateFlow()

    private val firebaseAuth = Firebase.auth

    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun registerWithEmailPassword(
        name: String,
        email: String,
        password: String
    ) {
        _state.update { it.copy(isLoading = true, registrationError = null) }

        viewModelScope.launch {
           // _registrationState.value = RegistrationState.Loading
            try {
                val result = withContext(Dispatchers.IO) {
                    try {
                        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                        val currentUser = authResult.user
                        val existingUsers = apiService.getAllUsers()
                        val userExists = existingUsers.data.any {
                            it.email == email
                        }


                        if (currentUser != null) {
                            val hashedPassword = hashPassword(password)
                            val userRequest = UserRequestDto(
                                user_id = currentUser.uid,
                                username = name,
                                email = email,
                                password = hashedPassword
                            )
                           apiService.createUser(userRequest)

                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()

                            currentUser.updateProfile(profileUpdates).await()

                            val userData = User(
                                userId = currentUser.uid,
                                displayName = name,
                                email = currentUser.email ?: ""
                            )
                            userRepository.saveUser(userData)

                            SignInResult(userData, null)
                        } else {
                            SignInResult(null, "Registrasi gagal. Silakan coba lagi.")
                        }
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        SignInResult(null, "Password terlalu lemah. Gunakan password yang lebih kuat.")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        SignInResult(null, "Format email tidak valid.")
                    } catch (e: FirebaseAuthUserCollisionException) {
                        SignInResult(null, "Email sudah digunakan. Gunakan email lain.")
                    } catch (e: Exception) {
                        SignInResult(
                            null,
                            e.localizedMessage ?: "Registrasi gagal. Pastikan data yang Anda masukkan benar."
                        )
                    }
                }

                _state.update {
                    it.copy(
                        isRegistrationSuccessful = result.data != null,
                        registrationError = result.errorMessage,
                        currentUser = result.data,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isRegistrationSuccessful = false,
                        registrationError = "Terjadi kesalahan. Silakan coba lagi.",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onSignInResult(result: SignInResult) {
        viewModelScope.launch {
            if (result.data != null) {
                userRepository.saveUser(result.data)
                _state.update {
                    it.copy(
                        isRegistrationSuccessful = true,
                        registrationError = null,
                        currentUser = result.data
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        isRegistrationSuccessful = false,
                        registrationError = result.errorMessage,
                        currentUser = null
                    )
                }
            }
        }
    }

    fun onSignInError(errorMessage: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isRegistrationSuccessful = false,
                    registrationError = errorMessage,
                    currentUser = null
                )
            }
        }
    }

    fun resetRegistrationState() {
        _state.update {
            val newState = it.copy(
                isRegistrationSuccessful = false,
                registrationError = null,
                currentUser = null
            )
            Log.d("RegisterViewModel", "resetRegistrationState: $newState")
            newState
        }
    }
}
sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    object Success : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}