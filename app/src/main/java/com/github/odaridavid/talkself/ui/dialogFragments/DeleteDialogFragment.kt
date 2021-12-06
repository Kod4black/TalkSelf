package com.github.odaridavid.talkself.ui.dialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.odaridavid.talkself.ui.models.ConversationUiModel

class DeleteDialogFragment : DialogFragment() {

    private lateinit var conversationUiModel: ConversationUiModel
    private var action: ((String, ConversationUiModel) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(TITLE)
            .setMessage(MESSAGE)
            .setPositiveButton(POSSITIVE_MESSAGE_BUTTON) { _, _ ->
                action?.invoke(POSSITIVE_MESSAGE_BUTTON, conversationUiModel)

            }
            .setNegativeButton(NEGATIVE_MESSAGE_BUTTON) { dialog, _ ->
                action?.invoke(NEGATIVE_MESSAGE_BUTTON, conversationUiModel)

                dialog.cancel()
            }
            .create()


    companion object {
        // TODO Move these to string resource for translation
        const val TITLE = "Confirm Delete..."
        const val MESSAGE = "Are you sure you wanna delete this?"
        const val POSSITIVE_MESSAGE_BUTTON = "Delete Conversation"
        const val NEGATIVE_MESSAGE_BUTTON = "No"
        const val TAG = "DeleteDialogFragment"

        @JvmStatic
        fun newInstance(
            conversationUiModel: ConversationUiModel,
            action: (String, ConversationUiModel) -> Unit
        ): DeleteDialogFragment =
            DeleteDialogFragment().apply {
                this.conversationUiModel = conversationUiModel
                this.action = action
            }
    }
}
