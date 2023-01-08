package com.github.odaridavid.talkself.ui.dialogFragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.ui.models.ConversationUiModel

internal class DeleteDialogFragment : DialogFragment() {

    private lateinit var conversationUiModel: ConversationUiModel
    private var onDeleteConversationSelected: ((ConversationUiModel) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_dialog_title))
            .setMessage(getString(R.string.delete_dialog_message))
            .setPositiveButton(getString(R.string.delete_positive_button_title)) { _, _ ->
                onDeleteConversationSelected?.invoke(conversationUiModel)
            }
            .setNegativeButton(getString(R.string.delete_negative_button_title)) { dialog, _ ->
                dialog.cancel()
            }
            .create()


    companion object {
        const val TAG = "DeleteDialogFragment"

        @JvmStatic
        fun newInstance(
            conversationUiModel: ConversationUiModel,
            onDeleteConversationSelected: (ConversationUiModel) -> Unit
        ): DeleteDialogFragment =
            DeleteDialogFragment().apply {
                this.conversationUiModel = conversationUiModel
                this.onDeleteConversationSelected = onDeleteConversationSelected
            }
    }

}
