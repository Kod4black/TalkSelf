package com.github.odaridavid.talkself.domain

data class Chat(
    var chatId: Int? = null,
    var userId: Int? = null,
    var username: String? = null,
    var message: String? = null,
    var timesent: Long? = null,
    var conservationId: Int? = null,
)
