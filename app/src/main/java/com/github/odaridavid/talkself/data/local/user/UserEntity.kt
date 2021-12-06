/**
 *
 * Copyright 2020 David Odari
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *            http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 **/
package com.github.odaridavid.talkself.data.local.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.odaridavid.talkself.domain.User

@Entity(tableName = "user")
data class UserEntity(
    var name: String? = null,
    val conversationId: Int? = null,
    var imageUri: String? = null,
    var color: String? = null,
    @PrimaryKey(autoGenerate = true)
    var userId: Int? = null,
)

fun UserEntity.toDomain(): User = User(name, conversationId, imageUri, color, userId)