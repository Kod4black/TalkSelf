package com.github.odaridavid.talkself.ui.fragments.dialogFragments

import android.app.Dialog
import android.os.Bundle
import android.os.Message
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.github.odaridavid.talkself.models.Conversation

class DeleteDialogFragment(private val conversation: Conversation,private val action: (String, Conversation) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(TITLE)
            .setMessage(MESSAGE)
            .setPositiveButton(POSSITIVE_MESSAGE_BUTTON) { _, _ ->
                action.invoke(POSSITIVE_MESSAGE_BUTTON,conversation)

            }
            .setNegativeButton(NEGATIVE_MESSAGE_BUTTON) { dialog, _ ->
                action.invoke(NEGATIVE_MESSAGE_BUTTON,conversation)

                dialog.cancel() }
            .create()


    companion object{
        const val TITLE = "Confirm Delete..."
        const val MESSAGE = "Are you sure you wanna delete this?"
        const val POSSITIVE_MESSAGE_BUTTON = "Delete Conversation"
        const val NEGATIVE_MESSAGE_BUTTON = "No"
        const val TAG = "DeleteDialogFragment"
    }

}