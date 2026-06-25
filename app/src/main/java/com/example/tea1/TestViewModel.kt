package com.example.tea1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.IOException

class TestViewModel: ViewModel() {
    sealed interface TeaUiState {
        data class Success(val hash: String) : TeaUiState
        object Error : TeaUiState
        object Loading : TeaUiState
    }

    var teaUiState: TeaUiState by mutableStateOf(TeaUiState.Loading)
        private set

    suspend fun authentication(user : String, mdp : String) : String {
            return try {
                val apihash = TeaApi.retrofitService.authenticate(user, mdp)
                teaUiState = TeaUiState.Success(apihash)
                apihash
            } catch (e: IOException) {
                teaUiState = TeaUiState.Error
                "false"
            }
    }

}