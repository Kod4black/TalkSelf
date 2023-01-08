package com.github.odaridavid.talkself.ui.edituser

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import codes.side.andcolorpicker.converter.getBInt
import codes.side.andcolorpicker.converter.getGInt
import codes.side.andcolorpicker.converter.getRInt
import codes.side.andcolorpicker.converter.setFromColorInt
import codes.side.andcolorpicker.group.PickerGroup
import codes.side.andcolorpicker.group.registerPickers
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.picker.ColorSeekBar
import com.github.odaridavid.talkself.common.Analytics
import com.github.odaridavid.talkself.common.ColorUtils
import com.github.odaridavid.talkself.common.bindImage
import com.github.odaridavid.talkself.databinding.FragmentEditUserBinding
import com.github.odaridavid.talkself.ui.models.UserUiModel
import dagger.hilt.android.AndroidEntryPoint

// TODO Check on Fragment bindings should call onDestroyView and unbind
@AndroidEntryPoint
class EditUserFragment : Fragment() {

    private val viewmodel by viewModels<EditUserViewModel>()
    private lateinit var userUiModel: UserUiModel
    private lateinit var binding: FragmentEditUserBinding
    private val args: EditUserFragmentArgs by navArgs()
    private lateinit var memojiAdapter: MemojiAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditUserBinding.inflate(inflater, container, false)

        userUiModel = args.user

        binding.imageViewback.setOnClickListener {
            findNavController().navigateUp()
        }

        bindDialogView(userUiModel)

        viewmodel.updateName(userUiModel.name!!)
        viewmodel.updateImage(userUiModel.imageUri!!)
        viewmodel.updateColor(userUiModel.color!!)

        viewmodel.name.observe(viewLifecycleOwner) {
            binding.textGchatUserOther.text = it
        }

        viewmodel.imageUrl.observe(viewLifecycleOwner) { imageUrl ->
            binding.imageViewUserProfile.bindImage(context = requireContext(), imageUrl = imageUrl)
            binding.imageGchatProfileOther.bindImage(
                context = requireContext(),
                imageUrl = imageUrl
            )
        }

        viewmodel.color.observe(viewLifecycleOwner) {
            binding.layoutGchatContainerOther.setBackgroundColor(Color.parseColor(it))
        }

        addOnBackPressCallback()

        return binding.root
    }

    private fun bindMemojiDialog() {

        binding.dialog.apply {
            recyclerviewMemoji.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                memojiAdapter = MemojiAdapter { image -> passResult(image) }
                adapter = memojiAdapter
                memojiAdapter.submitList(memojis)
            }
        }

        callback.isEnabled = true
    }

    private fun bindDialogView(userUiModel: UserUiModel) {

        binding.apply {
            imageViewUserProfile.setOnClickListener {
                transformationLayout.bindTargetView(myCardView)
                transformationLayout.startTransform()
                bindMemojiDialog()
            }

            textInputEditText.apply {
                // TODO Create delegate for this
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        //Nothing to do here
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        viewmodel.updateName(s.toString())
                    }

                    override fun afterTextChanged(s: Editable?) {
                        //Nothing to do here
                    }

                })
                setText(userUiModel.name)
                textGchatUserOther.text = userUiModel.name
            }

            val pickerGroup = PickerGroup<IntegerHSLColor>().also {
                it.registerPickers(
                    hueColorPickerSeekBar,
                    saturationColorPickerSeekBar,
                    lightnessColorPickerSeekBar
                )
            }

            // TODO Abstract this third party logic somewhere else
            pickerGroup.addListener(
                object :
                    ColorSeekBar.OnColorPickListener<ColorSeekBar<IntegerHSLColor>, IntegerHSLColor> {
                    override fun onColorChanged(
                        picker: ColorSeekBar<IntegerHSLColor>,
                        color: IntegerHSLColor,
                        value: Int
                    ) {

                        try {
                            val hexColor = ColorUtils.getColor(
                                red = color.getRInt(),
                                green = color.getGInt(),
                                blue = color.getBInt(),
                                alpha = color.alpha.toDouble()
                            )

                            viewmodel.updateColor(hexColor)
                        } catch (e: Exception) {
                            val params = Bundle().apply {
                                putString(
                                    "class",
                                    "${this@EditUserFragment.requireActivity().packageName}.${this@EditUserFragment.javaClass}"
                                )
                                putString("exception", e.message)
                            }
                            Analytics.instance().logEvent("ColorSelectionException", params)
                        }
                    }

                    override fun onColorPicked(
                        picker: ColorSeekBar<IntegerHSLColor>,
                        color: IntegerHSLColor,
                        value: Int,
                        fromUser: Boolean
                    ) {
                        // Do Nothing
                    }

                    override fun onColorPicking(
                        picker: ColorSeekBar<IntegerHSLColor>,
                        color: IntegerHSLColor,
                        value: Int,
                        fromUser: Boolean
                    ) {
                        // Do Nothing
                    }
                }
            )

            // Set desired color programmatically
            pickerGroup.setColor(
                IntegerHSLColor().also {
                    it.setFromColorInt(
                        Color.parseColor(userUiModel.color)
                    )
                }
            )

            binding.buttonSave.setOnClickListener {
                userUiModel.name = viewmodel.name.value
                userUiModel.imageUri = viewmodel.imageUrl.value
                userUiModel.color = viewmodel.color.value!!
                viewmodel.updateUser(userUiModel)
                findNavController().navigateUp()
            }
        }
    }

    // TODO Rename this function or refactor it
    private fun passResult(imageUrl: String) {
        binding.apply {
            viewmodel.updateImage(imageUrl)
            binding.transformationLayout.finishTransform()
            callback.isEnabled = true
        }
    }

    private val callback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            binding.transformationLayout.finishTransform()
            isEnabled = false
        }
    }

    private fun addOnBackPressCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }

}
