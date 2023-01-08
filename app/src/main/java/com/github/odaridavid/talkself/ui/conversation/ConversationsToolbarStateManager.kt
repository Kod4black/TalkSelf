package com.github.odaridavid.talkself.ui.conversation

import androidx.lifecycle.MutableLiveData
import com.github.odaridavid.talkself.common.ToolbarState
import com.github.odaridavid.talkself.ui.models.ConversationUiModel

internal class ConversationsToolbarStateManager {

    private val _toolbarState: MutableLiveData<ToolbarState> =
        MutableLiveData(ToolbarState.NormalViewState)
    private val _isMultiSelection: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _selectedConversations: MutableLiveData<MutableList<ConversationUiModel>> =
        MutableLiveData()

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

    fun addOrRemoveConversationFromSelectedList(conversationUiModel: ConversationUiModel) {
        val list = _selectedConversations.value ?: mutableListOf()
        if (list.contains(conversationUiModel)) {
            list.remove(conversationUiModel)
        } else {
            list.add(conversationUiModel)
        }
        _selectedConversations.value = list
    }

    fun addAllConversationsToSelectedList(list: List<ConversationUiModel>) {
        _selectedConversations.value = list as MutableList<ConversationUiModel>
    }

    fun clearSelectedList() {
        _selectedConversations.value = ArrayList()
    }
}
