package com.github.odaridavid.talkself.utils.interfaces

import com.github.odaridavid.talkself.models.User
import com.skydoves.transformationlayout.TransformationLayout

interface ImageClick {
    fun onImageClick(transformationLayout: TransformationLayout, user: User)
}