package com.github.odaridavid.talkself.common

import com.github.odaridavid.talkself.data.local.user.UserEntity

interface UserProfileInteractions {
    fun onImageClick(userEntity: UserEntity)
}
