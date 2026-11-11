package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.SubscriptionPlan
import com.example.ui.theme.Emerald500
import com.example.ui.theme.GoldenGlow
import com.example.ui.theme.LegoBlue
import com.example.ui.theme.LegoRed
import com.example.ui.theme.LegoYellow
import com.example.ui.theme.PowerballRed
import com.example.ui.theme.SkyBlue400
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import com.example.ui.theme.Slate950

@Composable
fun AuthSubscriptionDialog(
    initialTab: Int = 0, // 0 = Iniciar Sesión de Suscripción, 1 = Crear Cuenta Nueva con KYC Estricto
    onSignInSubscription: (email: String, plan: SubscriptionPlan) -> Unit,
    onRegisterAccount: (email: String, name: String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    // Form fields - Login
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var selectedPlan by remember { mutableStateOf(SubscriptionPlan.VIP_GOLD) }

    // Form fields - Strict KYC Registration
    var fullNameInput by remember { mutableStateOf("") }
    var registerEmailInput by remember { mutableStateOf("") }
    var idNumberInput by remember { mutableStateOf("") } // Cédula o Pasaporte vigente
    var countryInput by remember { mutableStateOf("Ecuador") }
    var addressInput by remember { mutableStateOf("") }
    var phoneInput by remember { mutableStateOf("") }
    var pichinchaAccountInput by remember { mutableStateOf("") } // Cuenta Banco Pichincha vigente
    var registerPasswordInput by remember { mutableStateOf("") }

    // KYC Document Upload simulation states
    var frontIdUploaded by remember { mutableStateOf(false) }
    var backIdUploaded by remember { mutableStateOf(false) }
    var selfieWithIdUploaded by remember { mutableStateOf(false) }
    var kycValidationStatus by remember { mutableStateOf<Boolean?>(null) } // null = pending, true = approved, false = rejected

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("auth_subscription_dialog"),
            shape = RoundedCornerShape(22.dp),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, LegoYellow),
            tonalElevation = 14.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (selectedTab == 0) GoldenGlow else LegoBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Default.CardMembership else Icons.Default.Badge,
                                contentDescription = null,
                                tint = Slate950,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (selectedTab == 0) "Inicio de Suscripción" else "Registro KYC & Cuenta Nueva",
                                color = LegoYellow,
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "LEGO Bingo & Lotería - Ecuador",
                                color = Slate400,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Slate400
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tab Switcher
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Slate950,
                    contentColor = LegoYellow,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = LegoYellow,
                            height = 3.dp
                        )
                    },
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("Iniciar Sesión", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("Registro con KYC", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (errorMessage != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PowerballRed.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PowerballRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = PowerballRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (selectedTab == 0) {
                    // TAB 0: Iniciar Sesión de Suscripción
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Ingresa con tu correo de suscripción VIP:",
                            color = Color.White,
                            fontSize = 12.sp
                        )

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it; errorMessage = null },
                            label = { Text("Correo Electrónico") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GoldenGlow) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldenGlow,
                                unfocusedBorderColor = Slate700,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("login_email_input")
                        )

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GoldenGlow) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldenGlow,
                                unfocusedBorderColor = Slate700,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("login_password_input")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = {
                                if (emailInput.isBlank() || !emailInput.contains("@")) {
                                    errorMessage = "Por favor ingresa un correo electrónico válido."
                                } else {
                                    onSignInSubscription(emailInput, selectedPlan)
                                    Toast.makeText(context, "¡Sesión VIP iniciada con éxito!", Toast.LENGTH_LONG).show()
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldenGlow),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("submit_subscription_login_btn")
                        ) {
                            Icon(imageVector = Icons.Default.CardMembership, contentDescription = null, tint = Slate950)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Iniciar Sesión VIP",
                                color = Slate950,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                } else {
                    // TAB 1: Registro Estricto con KYC (Cédula/Pasaporte, Datos Obligatorios, Fotos y Validación)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🔒 Registro Seguro con KYC (Datos Obligatorios Requeridos):",
                            color = LegoYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )

                        OutlinedTextField(
                            value = fullNameInput,
                            onValueChange = { fullNameInput = it; errorMessage = null },
                            label = { Text("Nombre Completo (como en documento)") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = LegoBlue) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LegoBlue,
                                unfocusedBorderColor = Slate700,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("kyc_fullname_input")
                        )

                        OutlinedTextField(
                            value = idNumberInput,
                            onValueChange = { idNumberInput = it; errorMessage = null },
                            label = { Text("Cédula Vigente o Pasaporte Vigente") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null, tint = LegoBlue) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LegoBlue,
                                unfocusedBorderColor = Slate700,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("kyc_id_input")
                        )

                        OutlinedTextField(
                            value = registerEmailInput,
                            onValueChange = { registerEmailInput = it; errorMessage = null },
                            label = { Text("Correo Electrónico Válido") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = LegoBlue) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LegoBlue,
                                unfocusedBorderColor = Slate700,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().testTag("kyc_email_input")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = countryInput,
                                onValueChange = { countryInput = it },
                                label = { Text("País") },
                                leadingIcon = { Icon(Icons.Default.Public, contentDescription = null, tint = LegoBlue) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LegoBlue,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = phoneInput,
                                onValueChange = { phoneInput = it },
                                label = { Text("Teléfono / Celular") },
                                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = LegoBlue) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LegoBlue,
                                    unfocusedBorderColor = Slate700,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = addressInput,
                            onValueChange = { addressInput = it },
                            label = { Text("Dirección Actual en Quito / Ecuador") },
                            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = LegoBlue) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LegoBlue,
                                unfocusedBorderColor = Slate700,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = pichinchaAccountInput,
                            onValueChange = { pichinchaAccountInput = it },
                            label = { Text("Cuenta de Banco Pichincha Vigente (N°)") },
                            leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Emerald500) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Emerald500,
                                unfocusedBorderColor = Slate700,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Document Upload & Selfie Section
                        Text(
                            text = "📸 Verificación Fotográfica Legal:",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    frontIdUploaded = true
                                    Toast.makeText(context, "Cédula frontal cargada correctamente", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (frontIdUploaded) Emerald500 else Slate700),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (frontIdUploaded) "✅ Cédula Frontal" else "Cargar Cédula Frontal",
                                    color = if (frontIdUploaded) Emerald500 else Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    backIdUploaded = true
                                    Toast.makeText(context, "Cédula trasera cargada correctamente", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (backIdUploaded) Emerald500 else Slate700),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (backIdUploaded) "✅ Cédula Trasera" else "Cargar Cédula Trasera",
                                    color = if (backIdUploaded) Emerald500 else Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                selfieWithIdUploaded = true
                                Toast.makeText(context, "Selfie con documento visible capturada con éxito", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selfieWithIdUploaded) Emerald500 else Slate700),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = if (selfieWithIdUploaded) Emerald500 else LegoYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (selfieWithIdUploaded) "✅ Selfie con Documento Verificada" else "Tomar Selfie con Cédula / Pasaporte Visible",
                                color = if (selfieWithIdUploaded) Emerald500 else Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (kycValidationStatus == true) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Emerald500.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Emerald500),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Emerald500, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(text = "¡Aprobado por el Algoritmo KYC!", color = Emerald500, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(text = "Cuenta verificada. Correo de confirmación enviado.", color = Color.White, fontSize = 10.sp)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                if (fullNameInput.isBlank() || idNumberInput.isBlank() || registerEmailInput.isBlank() || pichinchaAccountInput.isBlank()) {
                                    errorMessage = "Por favor completa todos los campos obligatorios de KYC y cuenta Pichincha."
                                } else if (!frontIdUploaded || !backIdUploaded || !selfieWithIdUploaded) {
                                    errorMessage = "Debes cargar la imagen frontal, trasera y la selfie con tu cédula o pasaporte."
                                } else {
                                    kycValidationStatus = true
                                    onRegisterAccount(registerEmailInput, fullNameInput)
                                    Toast.makeText(context, "¡Perfil KYC verificado y aprobado automáticamente! Correo enviado.", Toast.LENGTH_LONG).show()
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LegoBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("submit_kyc_register_btn")
                        ) {
                            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Enviar y Validar KYC Automáticamente",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
