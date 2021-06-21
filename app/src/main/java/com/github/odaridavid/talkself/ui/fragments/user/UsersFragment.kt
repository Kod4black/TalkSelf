package com.github.odaridavid.talkself.ui.fragments.user

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.graphics.alpha
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import codes.side.andcolorpicker.converter.getBInt
import codes.side.andcolorpicker.converter.getGInt
import codes.side.andcolorpicker.converter.getRInt
import codes.side.andcolorpicker.converter.setFromColorInt
import codes.side.andcolorpicker.group.PickerGroup
import codes.side.andcolorpicker.group.registerPickers
import codes.side.andcolorpicker.hsl.HSLColorPickerSeekBar
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.picker.ColorSeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.FragmentUsersBinding
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.getColor
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.toast
import com.github.odaridavid.talkself.utils.interfaces.ImageClick
import com.skydoves.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class UsersFragment : Fragment(), ImageClick {


    private val viewmodel by viewModels<UserFragmentViewModel>()
    private lateinit var binding: FragmentUsersBinding
    private var conversation: Conversation? = null
    private lateinit var userAdapter: UsersAdapter
    private var transformationLayoutGlobal : TransformationLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)

        conversation = arguments?.getParcelable("conversation")

        bindUI()
        return binding.root
    }

    private fun bindUI() {

        binding.apply {

            users.apply {
                userAdapter = UsersAdapter(binding, callback, this@UsersFragment)
                adapter = userAdapter
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            }

            imageViewback.setOnClickListener {
                it.findNavController().navigateUp()
            }

        }

        viewmodel.users(conversation?.id!!).observe(viewLifecycleOwner, {
            userAdapter.submitList(it)
        })
    }



    override fun onImageClick(transformationLayout: TransformationLayout, user: User) {
        transformationLayout.apply {
            transformationLayoutGlobal = this
            bindDialogView(user)
            bindTargetView(binding.myCardView)
            startTransform()
            callback.isEnabled = true
        }


    }

    private fun bindDialogView(user: User) {

        binding.dialog.apply {

            Glide.with(imageViewUserProfile)
                .load(user.imageUri)
                .circleCrop()
                .placeholder(R.drawable.ic_koala)
                .error(R.drawable.ic_koala)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewUserProfile)


            textInputEditText.apply {
                addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        textGchatUserOther.text = s.toString()
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })

                setText(user.name)
                textGchatUserOther.text = user.name

            }


            val pickerGroup = PickerGroup<IntegerHSLColor>().also {
                it.registerPickers(
                    hueColorPickerSeekBar,
                    saturationColorPickerSeekBar,
                    lightnessColorPickerSeekBar,
                    alphaColorPickerSeekBar
                )
            }

            // Listen individual pickers or groups for changes
            pickerGroup.addListener(
                object : HSLColorPickerSeekBar.DefaultOnColorPickListener() {
                    override fun onColorChanged(
                        picker: ColorSeekBar<IntegerHSLColor>,
                        color: IntegerHSLColor,
                        value: Int
                    ) {

                        try {
                            val hexColor = getColor(color.getRInt(),color.getGInt(),color.getBInt(),color.alpha.toDouble())
                            layoutGchatContainerOther.setBackgroundColor(Color.parseColor(hexColor))
                            requireContext().toast(hexColor)
                        }catch (e : Exception){
                            requireContext().toast(e.message!!)
                        }

                    }
                }
            )
            // Set desired color programmatically
            pickerGroup.setColor(
                IntegerHSLColor().also {
                    it.setFromColorInt(
                        Color.rgb(
                            28,
                            84,
                            187
                        )
                    )
                }
            )



        }


    }

    val callback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            transformationLayoutGlobal?.finishTransform()
            isEnabled = false
        }
    }


}