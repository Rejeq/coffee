package com.rejeq.sws.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rejeq.sws.R
import com.rejeq.sws.ui.register.RegisterViewModel.NavEvent
import com.rejeq.sws.ui.theme.SwsTheme
import com.rejeq.sws.ui.utils.FailedContent
import com.rejeq.sws.ui.utils.NavEventSideEffect
import com.rejeq.sws.ui.utils.SwsButton
import com.rejeq.sws.ui.utils.SwsPasswordTextField
import com.rejeq.sws.ui.utils.SwsTextField
import com.rejeq.sws.ui.utils.SwsTopBar
import com.rejeq.sws.ui.utils.selectAll

@Composable
fun RegisterContent(
    vm: RegisterViewModel,
    modifier: Modifier = Modifier,
    onOpenShops: () -> Unit = {},
) {
    NavEventSideEffect(vm.navigation) { event ->
        when (event) {
            NavEvent.OpenShops -> onOpenShops()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SwsTopBar(
                title = stringResource(R.string.register_top_bar_title),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                32.dp,
                Alignment.CenterVertically,
            ),
        ) {
            val focusManager = LocalFocusManager.current
            val keyboardController = LocalSoftwareKeyboardController.current

            val email by vm.emailTextField
            val password by vm.passwordTextField
            val repeatPassword by vm.repeatPasswordTextField

            SwsTextField(
                value = email,
                onValueChange = vm::onEmailChange,
                title = stringResource(R.string.email_field_title),
                placeholder = "example@example.ru",
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)

                    vm.onPasswordChange(password.selectAll())
                },
            )

            SwsPasswordTextField(
                value = password,
                onValueChange = vm::onPasswordChange,
                title = stringResource(R.string.password_field_title),
                placeholder = "********",
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)

                    vm.onRepeatPasswordChange(repeatPassword.selectAll())
                },
            )

            SwsPasswordTextField(
                value = repeatPassword,
                onValueChange = vm::onRepeatPasswordChange,
                title = stringResource(R.string.repeat_password_field_title),
                placeholder = "********",
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions {
                    keyboardController?.hide()
                    vm.onRegisterClick()
                },
            )

            SwsButton(
                onClick = vm::onRegisterClick,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.register_btn_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            val error = vm.error.collectAsStateWithLifecycle().value
            if (error != null) {
                FailedContent(
                    error = error,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
@PreviewScreenSizes
private fun RegisterContentPreview() {
    SwsTheme {
        RegisterContent(
            vm = remember { PreviewRegisterViewModel() },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
