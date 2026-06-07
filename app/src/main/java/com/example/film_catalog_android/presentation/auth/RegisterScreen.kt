package com.example.film_catalog_android.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.film_catalog_android.core.ui.auth.AuthModeSwitcher
import com.example.film_catalog_android.core.ui.auth.AuthTextField
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.height(56.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextField(
            value = uiState.firstName,
            onValueChange = viewModel::onFirstNameChange,
            placeholder = "Имя"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = uiState.lastName,
            onValueChange = viewModel::onLastNameChange,
            placeholder = "Фамилия"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            placeholder = "Электронная почта"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            placeholder = "Пароль",
            visualTransformation = PasswordVisualTransformation()
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = uiState.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.register(onRegisterSuccess)
            },
            enabled = !uiState.isLoading
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(text = "Зарегистрироваться")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AuthModeSwitcher(
            isRegisterSelected = true,
            onRegisterClick = {},
            onLoginClick = onLoginClick
        )
    }
}
