package com.rejeq.sws.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rejeq.sws.R

@Composable
fun Double.toLocalizedPriceString(): String =
    stringResource(R.string.price_rub, this)
