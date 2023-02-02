package com.bryan.androidexam.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bryan.androidexam.R
import com.bryan.androidexam.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainBtnNext.setOnClickListener {
            if (checkInput(binding.mainEtColumn) && checkInput(binding.mainEtRow)) {
                val bundle = Bundle()
                bundle.putInt("column", binding.mainEtColumn.text.toString().toInt())
                bundle.putInt("row", binding.mainEtRow.text.toString().toInt())
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_secondFragment, bundle)
            }
        }
    }

    private fun checkInput(editText: EditText): Boolean {
        if (editText.length() > 0) {
            val num = editText.text.toString().toInt()
            if (num > 0) {
                return true
            } else {
                editText.error = "須大於 0"
            }
        } else {
            editText.error = "不可為空"
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}