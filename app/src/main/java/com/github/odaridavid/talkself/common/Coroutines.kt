package com.github.odaridavid.talkself.common

import kotlinx.coroutines.*

object Coroutines {

    //Working in the Main/UI thread
    fun main(work: suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }

}
