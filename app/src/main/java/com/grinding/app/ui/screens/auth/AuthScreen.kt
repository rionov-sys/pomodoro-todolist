package com.grinding.app.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grinding.app.R
import com.grinding.app.ui.theme.*

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    var isRegisterMode by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onAuthSuccess()
        }
    }

    Scaffold(
        containerColor = Background,
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header: Brand & Mascot
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(SecondaryContainer.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassTop,
                            contentDescription = stringResource(R.string.app_name),
                            tint = Primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Surface(
                        shape = PillShape,
                        color = SurfaceContainerHigh
                    ) {
                        Text(
                            text = stringResource(R.string.app_tagline),
                            style = MaterialTheme.typography.labelSmall,
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.auth_welcome_title),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(R.string.auth_welcome_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Auth Mode Switcher Pill
            item {
                Surface(
                    shape = PillShape,
                    color = SurfaceContainerHigh,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = PillShape,
                            color = if (!isRegisterMode) InverseSurface else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clip(PillShape)
                                .clickable {
                                    isRegisterMode = false
                                    viewModel.clearMessages()
                                }
                        ) {
                            Text(
                                text = stringResource(R.string.auth_tab_masuk),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (!isRegisterMode) InverseOnSurface else OnSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }

                        Surface(
                            shape = PillShape,
                            color = if (isRegisterMode) InverseSurface else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clip(PillShape)
                                .clickable {
                                    isRegisterMode = true
                                    viewModel.clearMessages()
                                }
                        ) {
                            Text(
                                text = stringResource(R.string.auth_tab_daftar),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isRegisterMode) InverseOnSurface else OnSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // Error or Success Banner
            item {
                AnimatedVisibility(visible = uiState.errorMessage != null) {
                    uiState.errorMessage?.let { errorMsg ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = ErrorContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = ErrorColor)
                                Text(
                                    text = errorMsg,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnErrorContainer
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = uiState.successMessage != null) {
                    uiState.successMessage?.let { successMsg ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFDCFCE7),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF166534))
                                Text(
                                    text = successMsg,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF166534)
                                )
                            }
                        }
                    }
                }
            }

            // Form Card
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = SurfaceContainerLowest,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (isRegisterMode) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text(stringResource(R.string.auth_label_nama)) },
                                placeholder = { Text(stringResource(R.string.auth_hint_nama)) },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Outline) },
                                shape = PillShape,
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text(stringResource(R.string.auth_label_email)) },
                            placeholder = { Text(stringResource(R.string.auth_hint_email)) },
                            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null, tint = Outline) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = PillShape,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(stringResource(R.string.auth_label_password)) },
                            placeholder = { Text(stringResource(R.string.auth_hint_password)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Outline) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = Outline
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            shape = PillShape,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // Remember Me Checkbox
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = { rememberMe = it },
                                    colors = CheckboxDefaults.colors(checkedColor = Primary)
                                )
                                Text(
                                    text = stringResource(R.string.auth_remember_me),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = OnSurfaceVariant
                                )
                            }
                        }

                        // Submit Button
                        Button(
                            onClick = {
                                if (isRegisterMode) {
                                    viewModel.signUp(email, password, name)
                                } else {
                                    viewModel.signIn(email, password)
                                }
                            },
                            enabled = !uiState.isLoading,
                            shape = PillShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = InverseSurface,
                                contentColor = InverseOnSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    color = InverseOnSurface,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    text = if (isRegisterMode) stringResource(R.string.auth_btn_register) else stringResource(R.string.auth_btn_login),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            // Benefit Toast
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SecondaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = OnPrimaryFixed, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.auth_benefit_title),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                text = stringResource(R.string.auth_benefit_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Terms Footer
            item {
                Text(
                    text = stringResource(R.string.auth_terms_privacy),
                    style = MaterialTheme.typography.bodySmall,
                    color = Outline,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
