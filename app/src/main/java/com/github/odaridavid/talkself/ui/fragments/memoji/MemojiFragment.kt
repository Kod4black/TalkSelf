package com.github.odaridavid.talkself.ui.fragments.memoji

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.FragmentMemojiBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemojiFragment : Fragment() {

    private lateinit var binding : FragmentMemojiBinding
    private lateinit var memojiAdapter: MemojiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentMemojiBinding.inflate(inflater,container,false)

        binding.apply {
            recyclerviewMemoji.apply {
                layoutManager = GridLayoutManager(requireContext(),3)
                memojiAdapter = MemojiAdapter{image -> passResult(image)}
                adapter = memojiAdapter
                memojiAdapter.submitList(memojis)
            }

            //navigate up
            imageViewback.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }

        return binding.root
    }

    companion object {
        val memojis = listOf(
            R.drawable.avatar1,
            R.drawable.avatar2,
            R.drawable.avatar3,
            R.drawable.avatar4,
            R.drawable.avatar5,
            R.drawable.avatar6,
            R.drawable.avatar7,
            R.drawable.avatar8,
            R.drawable.avatar9,
            R.drawable.avatar10,
            R.drawable.avatar11,
            R.drawable.avatar12,
            R.drawable.avatar13,
            R.drawable.avatar14,
            R.drawable.avatar15,
            R.drawable.avatar16,
            R.drawable.avatar17,
            R.drawable.avatar18,
            R.drawable.avatar19,
            R.drawable.avatar20,
            R.drawable.avatar21,
            R.drawable.avatar22,
            R.drawable.avatar23,
            R.drawable.avatar24,
            R.drawable.avatar25,
            R.drawable.avatar26,
            R.drawable.avatar27,
            R.drawable.avatar28,
            R.drawable.avatar29,
            R.drawable.avatar30,
            R.drawable.avatar31,
            R.drawable.avatar32,
            R.drawable.avatar33,
            R.drawable.avatar34,
            R.drawable.avatar35,
            R.drawable.avatar36,
            R.drawable.avatar37,
            R.drawable.avatar38,
            R.drawable.avatar39,
            R.drawable.avatar40,
            R.drawable.avatar41,
            R.drawable.avatar42,
        )
    }

    private fun passResult(drawable: Int){
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResult("requestKey", bundleOf("bundleKey" to drawable))
        findNavController().navigateUp()
    }

}
