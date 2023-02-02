package com.bryan.androidexam.ui.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryan.androidexam.databinding.FragmentSecondBinding
import kotlinx.coroutines.*

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SecondViewModel by activityViewModels()

    private var columnNumber = 0
    private var rowNumber = 0

    private lateinit var job: Job
    private lateinit var adapter : ColumnAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        columnNumber = arguments?.getInt("column") ?: 5
        rowNumber = arguments?.getInt("row") ?: 5
        viewModel.createList(columnNumber, rowNumber)
        adapter = ColumnAdapter(object : ColumnClickCallback {
            override fun onClick(columnIndex: Int) {
                viewModel.clearHighlightWithColumnIndex(columnIndex)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // disable scrolling
        binding.secondRv.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun canScrollHorizontally(): Boolean = false
            override fun canScrollVertically(): Boolean = false
        }
        binding.secondRv.itemAnimator = null
        binding.secondRv.adapter = adapter
        viewModel.columnDataListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        // get random value every 10 second
        job = CoroutineScope(Dispatchers.Main).launch {
            while (currentCoroutineContext().isActive) {
                delay(1000 * 10)
                viewModel.clearHighlight()
                viewModel.randomHighlight(columnNumber, rowNumber)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}