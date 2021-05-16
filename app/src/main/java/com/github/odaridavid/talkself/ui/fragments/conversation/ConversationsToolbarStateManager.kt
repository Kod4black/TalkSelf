package com.github.odaridavid.talkself.ui.fragments.conversation

import androidx.lifecycle.MutableLiveData
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.utils.ToolbarState

class ConversationsToolbarStateManager {

    private val _toolbarState: MutableLiveData<ToolbarState> =
        MutableLiveData(ToolbarState.NormalViewState)
    private val _isMultiSelection: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _selectedConversations: MutableLiveData<ArrayList<Conversation>> = MutableLiveData()

    val toolbarState
        get() = _toolbarState

    val isMultiSelection
        get() = _isMultiSelection

    val selectedConversations
        get() = _selectedConversations

    fun setToolbarState(state: ToolbarState) =
        run { _toolbarState.value = state }

    fun isMultiSelectionStateActive(): Boolean =
        ToolbarState.MultiselectionState == _toolbarState.value

    fun addOrRemoveConversationFromSelectedList(conversation: Conversation) {
        var list = _selectedConversations.value
        if (list == null) {
            list = ArrayList()
        } else {
            if (list.contains(conversation)) {
                list.remove(conversation)
            } else {
                list.add(conversation)
            }
            _selectedConversations.value = list
        }
    }

    fun addAllConversationsToSelectedList(list: ArrayList<Conversation>) {
        _selectedConversations.value = list
    }

    fun clearSelectedList() {
        _selectedConversations.value = ArrayList()
    }

}