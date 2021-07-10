package com.github.odaridavid.talkself.ui.fragments.conversation

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.FragmentConversationsBinding
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.ui.fragments.dialogFragments.DeleteDialogFragment
import com.github.odaridavid.talkself.utils.Coroutines
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.action
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.snack
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.toast
import com.github.odaridavid.talkself.utils.ToolbarState
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*


@AndroidEntryPoint
class ConversationFragment : Fragment() {

    private val viewmodel by viewModels<ConversationsViewModel>()
    private lateinit var conversationadapter: ConversationAdapter

    private lateinit var binding: FragmentConversationsBinding

    private val callback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (isMultiSelected()) {
                viewmodel.stateManager.setToolbarState(ToolbarState.NormalViewState)
            } else {
                finishCardTransform()
            }
        }
    }

    var mScrollY = 0F


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentConversationsBinding.inflate(inflater, container, false)

        //Set up recyclerAdapter
        setUpAdapter()

        //setup the recyclerview's itemtouchHelper
        setUpCustomItemTouchHelper()

        //Handle onbackpresses
        addOnBackPressCallback()

        bindUI()


        return binding.root
    }

    private fun createConversation(it: View) {
        //Create a new conversation using a randrom id
        val conversation = Conversation(conversationId = Random().nextInt(), System.currentTimeMillis())

        //Get username strings from the InputEditTextviews
        val usernameOne = getUserOneName()
        val usernameTwo = getUserTwoName()

        //If either of the usernames are null halt this function
        if (usernameOne == null || usernameTwo == null) {
            return
        }

        //Create user objects from the two usernames and associate them with the created conversation Id through th constructor
        val user1 = User(name = usernameOne,conversationId = conversation.conversationId,color = "#774df2",imageUri = "")
        val user2 = User(name = usernameTwo, conversationId = conversation.conversationId, color = "#ff2e2e2e",imageUri = "")

        //Call the function to add these objects to the database
        makeConversation(user1, user2, conversation)

        //Clean the UI
        finishCardTransform()
        clearInputEditText()


        val actions = ConversationFragmentDirections.conversationToChat(conversation)
        it.findNavController().navigate(actions)

    }

    //clear the text from the input TextViews
    private fun clearInputEditText() {
        binding.dialog.textInputEditTextFirstUsernameView.text?.clear()
        binding.dialog.textInputEditTextFirstUsernameView.text?.clear()
    }

    //Get the first username from the textInputEditText
    private fun getUserOneName(): String? {

        return if (binding.dialog.textInputEditTextFirstUsernameView.text.toString().isEmpty()) {
            binding.dialog.textInputEditTextFirstUsernameView.error =
                "Invalid Username.Should Contain Characters."
            null
        } else {
            binding.dialog.textInputEditTextFirstUsernameView.text.toString()
        }

    }

    //Get the first username from the textInputEditText
    private fun getUserTwoName(): String? {

        return if (binding.dialog.textInputEditTextSecondUsernameView.text.toString().isEmpty()) {
            binding.dialog.textInputEditTextSecondUsernameView.error =
                "Invalid Username.Should Contain Characters."
            null
        } else {
            binding.dialog.textInputEditTextSecondUsernameView.text.toString()
        }

    }

    //Start the morph animation of the fab to a cardview
    private fun startCardTransform() {
        binding.transformationLayout.startTransform()
        binding.backgroundView.apply {
            isVisible = true
        }
        callback.isEnabled = true
    }

    //Stop the morph animation of the fab to a cardview
    private fun finishCardTransform() {
        binding.transformationLayout.finishTransform()
        binding.backgroundView.apply {
            isVisible = false
        }
        callback.isEnabled = false
    }


    //Sets up the recyclerview
    private fun setUpAdapter() {
        binding.conversationsRecyclerView.apply {

            //Initialize the adapter
            conversationadapter = ConversationAdapter(
                viewLifecycleOwner,
                viewmodel.stateManager,
                onclick = { conversation -> onClick(conversation) },
                onLongClick = { conversation -> onLongClick(conversation) })

            //set it to the recyclerview
            adapter = conversationadapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rcv: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(rcv, dx, dy)
                    mScrollY += dy.toFloat()
                    mScrollY = mScrollY.coerceAtLeast(0F)
//                    layoutHeader.translationY = min(-mScrollY, 0F)
                }
            })
        }
    }


    fun showDeleteDialog(actualConversationObject: Conversation) {
        //Initialize the delete dialog fragment and show it
        /**
         * @param action is a sort of constant which determines the type of action to be perfomed on
         * @param actualConversationObject which is passed in here either to just delete or refresh the recyclerview
         *
         * @see actOnConversation() function
         *
         */
        DeleteDialogFragment(actualConversationObject) { action: String, conversation: Conversation ->

            actOnConversation(action, conversation)

        }.show(childFragmentManager, DeleteDialogFragment.TAG)
    }


    //Either delete or refresh the recyclerview based on the action passed
    private fun actOnConversation(action: String, conversation: Conversation) {

        when (action) {

            DeleteDialogFragment.POSSITIVE_MESSAGE_BUTTON -> {
                deleteConversation(conversation)
                showSnackBar("Conversation deleted", conversation)
            }

            DeleteDialogFragment.NEGATIVE_MESSAGE_BUTTON -> {
                conversationadapter.notifyDataSetChanged()
            }
        }

    }

    //Show a snackbar
    private fun showSnackBar(message: String, conversation: Conversation) {

        //If the undo action is pressed the conversation is added back
        requireView().snack(message) {
            action("Undo") { addConversation(conversation) }
        }

    }

    private fun deleteConversation(conversation: Conversation) {
        viewmodel.deleteConversation(conversation)
    }

    private fun addConversation(conversation: Conversation) {
        viewmodel.makeConversation(conversation)
    }

    private fun addUser(user: User) {
        viewmodel.addUser(user)
    }

    private fun makeConversation(user1: User, user2: User, conversation: Conversation) {
        addConversation(conversation)
        addUser(user1)
        addUser(user2)
    }


    private fun addOnBackPressCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

    private fun setUpCustomItemTouchHelper() {
        //A custom itemtouchhelper to add a background to a viewholder
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val conversationAndUser = conversationadapter.getItemAt(viewHolder.adapterPosition)
                    showDeleteDialog(conversationAndUser.conversation!!)
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .addBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorAccent
                            )
                        )
                        .addActionIcon(R.drawable.ic_swipe_delete)
                        .create()
                        .decorate()

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.conversationsRecyclerView)
    }

    /**
     * Toolbar State management
     */

    @SuppressLint("SetTextI18n")
    private fun bindUI() = Coroutines.main {
        //Launch our custom dialog Card to collect user input
        binding.fab.setOnClickListener {
            startCardTransform()
        }

        //Close the dialog when a user clicks on the dialog card
        binding.myCardView.setOnClickListener {
            finishCardTransform()
        }

        //Call the function to create a Conversation and launch the next Fragment
        binding.dialog.buttonCreate.setOnClickListener {
            createConversation(it)
        }

        //Close the dialog fragment when user clicks on the dismiss button
        binding.dialog.buttonDismiss.setOnClickListener {
            finishCardTransform()
        }

            fireUpObservers()


        toolBarCommonStuff()

    }

    private  fun fireUpObservers() {
        //Observe toolbar state changes and react accordingly
        viewmodel.stateManager.toolbarState.observe(viewLifecycleOwner, { state ->
            when (state) {

                ToolbarState.NormalViewState -> {
                    setNormalToolbar()
                    viewmodel.stateManager.clearSelectedList()
                    callback.isEnabled = false
                }

                ToolbarState.MultiselectionState -> {
                    setSelectedToolbar()
                    callback.isEnabled = true
                }

                else -> {
                    // do nothing
                }

            }
        })

        //Observe selected conversations and react to the toolbartitle accordingly
        viewmodel.stateManager.selectedConversations.observe(viewLifecycleOwner, {
            if (isMultiSelected()) {
                binding.activityConvesationsTitle.text = "${it.size} selected"
                if (it.size == 0) {
                    viewmodel.stateManager.setToolbarState(ToolbarState.NormalViewState)
                }
            } else {
                binding.activityConvesationsTitle.text = "Your Conversations"
            }
        })

        //Observe changes on the conversations from the viewModel
        viewmodel.conversationAndUser.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()){
                conversationadapter.submitList(it)
            }
        })

        //Observe the state of the isMultiselection state Variable
        viewmodel.stateManager.isMultiSelection.observe(viewLifecycleOwner, {
            if (!it) {
                conversationadapter.notifyDataSetChanged()
            }
        })
    }

    private fun setSelectedToolbar() {

        binding.toolbar.apply {
            menu.clear()
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorGrey))
            inflateMenu(R.menu.conversation_selected_menu)
            navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_cancel)
            setNavigationOnClickListener {
                viewmodel.stateManager.setToolbarState(ToolbarState.NormalViewState)
            }
//            binding.activityConvesationsTitle.visibility = View.GONE
            binding.activityConvesationsTitle.apply {
                visibility = View.VISIBLE
                setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
                textSize = 14F
            }

        }

    }

    private fun setNormalToolbar() {

        binding.toolbar.apply {
            navigationIcon = null
            setNavigationOnClickListener(null)
            menu.clear()
//            inflateMenu(R.menu.conversation_normal_fragment)
            binding.activityConvesationsTitle.apply {
                visibility = View.VISIBLE
                textSize = 24F
            }
        }

    }

    private fun onClick(conversation: Conversation) {
        if (isMultiSelected()) {
            viewmodel.stateManager.addOrRemoveConversationFromSelectedList(conversation)
        }
    }

    private fun onLongClick(conversation: Conversation) {
        viewmodel.stateManager.setToolbarState(ToolbarState.MultiselectionState)
        viewmodel.stateManager.addOrRemoveConversationFromSelectedList(conversation)
    }

    private fun isMultiSelected() = viewmodel.stateManager.isMultiSelectionStateActive()

    private fun toolBarCommonStuff() {

        binding.toolbar.apply {


            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {

                    R.id.action_deleteAll -> {
                        activity?.toast("Feature is under Development")
                    }

                    R.id.action_selectAll -> {
                        viewmodel.stateManager.addAllConversationsToSelectedList(viewmodel.conversation.value!!)
                    }

                    R.id.action_deselect_all -> {
                        viewmodel.stateManager.clearSelectedList()
                    }

                    R.id.settings -> {
                        findNavController().navigate(R.id.conversation_to_settings)
                    }
                }

                true
            }
        }
    }


}

