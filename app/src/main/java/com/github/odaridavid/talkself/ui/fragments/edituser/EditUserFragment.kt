package com.github.odaridavid.talkself.ui.fragments.edituser

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
import codes.side.andcolorpicker.hsl.HSLColorPickerSeekBar
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.picker.ColorSeekBar
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.FragmentEditUserBinding
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.bindImage
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.getColor
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditUserFragment : Fragment() {

    private val viewmodel by viewModels<EditUserViewModel>()
    private lateinit var user: User
    private lateinit var binding: FragmentEditUserBinding
    private val args: EditUserFragmentArgs by navArgs()
    private lateinit var memojiAdapter: MemojiAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentEditUserBinding.inflate(inflater, container, false)

        user = args.user

        binding.imageViewback.setOnClickListener {
            findNavController().navigateUp()
        }

        bindDialogView(user)

        viewmodel.updateName(user.name!!)
        viewmodel.updateImage(user.imageUri!!)
        viewmodel.updateColor(user.color!!)


        viewmodel.name.observe(viewLifecycleOwner, {
            binding.textGchatUserOther.text = it
        })


        viewmodel.image.observe(viewLifecycleOwner, {

            requireContext().bindImage(it, binding.imageViewUserProfile)
            requireContext().bindImage(it, binding.imageGchatProfileOther)

        })

        viewmodel.color.observe(viewLifecycleOwner, {

            binding.layoutGchatContainerOther.setBackgroundColor(Color.parseColor(it))

        })

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

    private fun bindDialogView(user: User) {

        binding.apply {


            imageViewUserProfile.setOnClickListener {

                transformationLayout.bindTargetView(myCardView)
                transformationLayout.startTransform()
                bindMemojiDialog()

            }

            textInputEditText.apply {

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

                            val hexColor = getColor(
                                color.getRInt(),
                                color.getGInt(),
                                color.getBInt(),
                                color.alpha.toDouble()
                            )

                            viewmodel.updateColor(hexColor)

                        } catch (e: Exception) {
                            requireContext().toast(e.message!!)
                        }

                    }
                }
            )

            // Set desired color programmatically
            pickerGroup.setColor(
                IntegerHSLColor().also {
                    it.setFromColorInt(
                        Color.parseColor(user.color)
                    )
                }
            )

            binding.buttonSave.setOnClickListener {
                user.name = viewmodel.name.value
                user.imageUri = viewmodel.image.value
                user.color = viewmodel.color.value!!
                viewmodel.updateUser(user)
                findNavController().navigateUp()
            }

        }

    }


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

    companion object {

        val memojis = listOf(
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar12.png?alt=media&token=e404c051-cb3a-4352-becb-ab3b7d7d40c6",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar13.png?alt=media&token=26cefe77-ab3c-4560-b510-ae1a7db02b12",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar1.png?alt=media&token=8866ea00-b959-4d01-a880-fa5fb258460b",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar10.png?alt=media&token=bae846da-82a5-4747-a78a-4dbe5b46bcc9",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar11.png?alt=media&token=bdf02178-84b2-47ed-84c3-2a1cc5ff8df7",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar14.png?alt=media&token=c1f57e19-7668-45ee-b913-c0650d84db00",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar16.png?alt=media&token=7f0ad6f6-cdff-480f-a25a-c352ca8479fd",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar15.png?alt=media&token=0b37801b-661d-42da-a680-6fb1b9b399ed",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar17.png?alt=media&token=973eec93-9965-4d50-be70-0174a58f0454",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar18.png?alt=media&token=739fa80d-ce65-4bb5-89cb-60de97f60595",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar19.png?alt=media&token=d49a8b5f-0ec7-4168-abc7-4bae3defed26",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar20.png?alt=media&token=6941755c-711b-499c-8523-0d5c48adad1f",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar21.png?alt=media&token=ffa103c8-33d0-46d7-a8bd-b83ac1c17be0",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar2.png?alt=media&token=221cc46b-1e1a-421e-a185-7c3aecdd18af",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar22.png?alt=media&token=f5742090-dd88-43f8-861a-31294d8387f0",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar23.png?alt=media&token=9d96a889-505e-4f48-a829-16e326230c09",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar24.png?alt=media&token=4ce6c37a-49cf-4967-80f8-aaa65bb6304d",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar25.png?alt=media&token=0852f77f-9cd6-4bbe-97db-b6b3aab68a1c",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar26.png?alt=media&token=88e45d64-dfc7-4180-aa6c-cb5d19404c50",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar27.png?alt=media&token=73b61640-5c58-4e98-a752-17dc22d216b5",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar28.png?alt=media&token=1939a554-1767-420d-9661-a91bbe4ba064",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar29.png?alt=media&token=35c3a0a7-f152-4b2d-9a40-065b7ca8b3bd",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar3.png?alt=media&token=b8e4b5ad-7a0c-425c-ba88-3366b489b03a",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar30.png?alt=media&token=d82b041f-15cb-4eba-b692-2f8a1d726a05",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar31.png?alt=media&token=3ee8257b-5df8-4491-9433-27c2ea248f48",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar32.png?alt=media&token=6e6216b8-2f29-4aad-8bcd-f6792954b706",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar36.png?alt=media&token=14ab1faf-c08e-4729-ab2c-3976782a71ea",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar34.png?alt=media&token=57ef3634-7a88-458c-81da-bb22574c7f0a",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar33.png?alt=media&token=d103e4d2-9316-4427-9cdc-c061aa135f7b",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar35.png?alt=media&token=f6886d93-5d68-43d5-b3b0-2990a65a83a3",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar37.png?alt=media&token=f26aeac6-dd79-49c3-a36b-e2f5d467a154",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar38.png?alt=media&token=2c184a9a-04fc-4db2-be39-209f4d9d9a29",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar4.png?alt=media&token=0d7b7249-fa35-430f-b8f4-70c5129e3da2",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar39.png?alt=media&token=1effb757-434f-486f-b4d7-e64a81032ffd",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar40.png?alt=media&token=8823f8e6-4390-419a-bd4c-01ee798e1e4a",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar41.png?alt=media&token=b045e993-5c30-47d7-8368-aab64f30e278",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar6.png?alt=media&token=42335d55-eb40-4fa4-9217-eba897cd6253",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar5.png?alt=media&token=433ae24c-6e46-4273-b68e-95d3434f8836",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar42.png?alt=media&token=4fbb8750-c076-4863-821e-345b8bc8a333",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar7.png?alt=media&token=e5d5be9a-3e20-48e0-8288-295ad79820dd",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar8.png?alt=media&token=85f7e9ce-824a-4c88-ad98-240de3bc486b",
            "https://firebasestorage.googleapis.com/v0/b/talk-self.appspot.com/o/avatar9.png?alt=media&token=da217a09-0f4e-4a1e-8a38-aa4aa9d11a2d"
        )

    }

}
