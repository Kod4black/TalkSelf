package com.github.odaridavid.talkself.ui.conversation

import androidx.lifecycle.MutableLiveData
import com.github.odaridavid.talkself.data.local.models.ConversationEntity
import com.github.odaridavid.talkself.common.ToolbarState

class ConversationsToolbarStateManager {

    private val _toolbarState: MutableLiveData<ToolbarState> =
        MutableLiveData(ToolbarState.NormalViewState)
    private val _isMultiSelection: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _selectedConversations: MutableLiveData<ArrayList<ConversationEntity>> = MutableLiveData()

    val toolbarState
        get() = _toolbarState

    val isMultiSelection
        get() = _isMultiSelection

    val selectedConversations
        get() = _selectedConversations

    fun setToolbarState(state: ToolbarState) =
        run {
            _toolbarState.value = state
            _isMultiSelection.value = isMultiSelectionStateActive()
        }

    fun isMultiSelectionStateActive(): Boolean =
        ToolbarState.MultiselectionState == _toolbarState.value

    fun addOrRemoveConversationFromSelectedList(conversationEntity: ConversationEntity) {
        var list = _selectedConversations.value
        if (list == null) {
            list = ArrayList()
        } else {
            if (list.contains(conversationEntity)) {
                list.remove(conversationEntity)
            } else {
                list.add(conversationEntity)
            }
            _selectedConversations.value = list
        }
    }

    fun addAllConversationsToSelectedList(list: List<ConversationEntity>) {
        _selectedConversations.value = list as ArrayList<ConversationEntity>
    }

    fun clearSelectedList() {
        _selectedConversations.value = ArrayList()
    }

}
