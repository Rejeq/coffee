package com.rejeq.sws.ui.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.rejeq.sws.ui.theme.SwsTheme

@Composable
fun SwsTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start),
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = visualTransformation,
            placeholder = placeholder?.let {
                { Text(it) }
            },
            singleLine = singleLine,
            isError = isError,
            supportingText = supportingText?.let {
                { Text(it) }
            },
            shape = RoundedCornerShape(32.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
        )
    }
}

@Composable
fun SwsPasswordTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    singleLine: Boolean = false,
    isError: Boolean = false,
    supportingText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    SwsTextField(
        value = value,
        onValueChange = onValueChange,
        title = title,
        modifier = modifier,
        placeholder = placeholder,
        singleLine = singleLine,
        isError = isError,
        supportingText = supportingText,
        visualTransformation = PasswordVisualTransformation(),
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
    )
}

fun TextFieldValue.selectAll(): TextFieldValue = this.copy(
    selection = TextRange(0, text.length),
)

@Composable
@Preview
@PreviewLightDark
@PreviewDynamicColors
private fun PreviewSwsTextField() {
    SwsTheme {
        Surface {
            Column {
                SwsTextField(
                    value = TextFieldValue(""),
                    onValueChange = {},
                    title = "Title",
                    placeholder = "example@example.ru",
                )

                SwsPasswordTextField(
                    value = TextFieldValue("Something"),
                    onValueChange = {},
                    title = "Password",
                )
            }
        }
    }
}
