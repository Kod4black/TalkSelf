package com.github.odaridavid.talkself.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun executeOnMainThread(work: suspend (() -> Unit)) =
    CoroutineScope(Dispatchers.Main).launch {
        work()
    }
