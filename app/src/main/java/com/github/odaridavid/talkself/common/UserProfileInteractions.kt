package com.github.odaridavid.talkself.common

import com.github.odaridavid.talkself.data.local.models.UserEntity

interface UserProfileInteractions {
    fun onImageClick(userEntity: UserEntity)
}
