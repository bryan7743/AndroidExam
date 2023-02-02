package com.bryan.androidexam.ui.second

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bryan.androidexam.databinding.RvColumnBinding
import com.bryan.androidexam.databinding.RvItemButtonBinding
import com.bryan.androidexam.databinding.RvItemTextBinding

data class ColumnData(val isHighlighted: Boolean = false, var rowDataList: MutableList<RowData>)
data class RowData(val isHighlighted: Boolean = false, val isButton: Boolean = false)

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

interface ColumnClickCallback {
    fun onClick(columnIndex: Int)
}

interface RowClickCallback {
    fun onClick()
}

class ColumnAdapter(val callback: ColumnClickCallback) : ListAdapter<ColumnData, ColumnAdapter.ColumnViewHolder>(DiffCallback()) {

    private val highlightForeground: GradientDrawable = GradientDrawable()
    private val normalForeground: GradientDrawable = GradientDrawable()

    init {
        highlightForeground.cornerRadius = 10f
        highlightForeground.setStroke(6, Color.parseColor("#1BD3D5"))
    }

    inner class ColumnViewHolder(private val binding: RvColumnBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(columnData: ColumnData, position: Int) {
            // disable scrolling
            binding.rvColumn.layoutManager = object : LinearLayoutManager(binding.root.context, VERTICAL, false) {
                override fun canScrollHorizontally(): Boolean = false
                override fun canScrollVertically(): Boolean = false
            }
            val adapter = RowAdapter(object : RowClickCallback {
                override fun onClick() {
                    callback.onClick(position)
                }
            })
            binding.rvColumn.adapter = adapter
            adapter.submitList(columnData.rowDataList)
            if (columnData.isHighlighted) {
                binding.rvColumn.foreground = highlightForeground
            } else {
                binding.rvColumn.foreground = normalForeground
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnViewHolder {
        val binding = RvColumnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.layoutParams.width = getScreenWidth() / itemCount // average distribution items width
        return ColumnViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColumnViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    class DiffCallback : DiffUtil.ItemCallback<ColumnData>() {
        override fun areItemsTheSame(oldItem: ColumnData, newItem: ColumnData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ColumnData, newItem: ColumnData): Boolean {
            return oldItem == newItem
        }
    }

    override fun submitList(list: MutableList<ColumnData>?) {
        super.submitList(if (list != null) ArrayList(list) else null)
    }
}

class RowAdapter(val callback: RowClickCallback) : ListAdapter<RowData, RecyclerView.ViewHolder>(DiffCallback()) {

    private val normalDrawable = GradientDrawable()
    private val highlightDrawable = GradientDrawable()

    init {
        normalDrawable.cornerRadius = 10f
        normalDrawable.setStroke(2, Color.parseColor("#9C9C9C"))
        highlightDrawable.cornerRadius = 10f
        highlightDrawable.setStroke(2, Color.parseColor("#9C9C9C"))
        highlightDrawable.setColor(Color.parseColor("#1BD3D5"))
    }

    class TextViewHolder(private val binding: RvItemTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rowData: RowData, position: Int) {
            val pair = when (position % 3) {
                0 -> Pair("#f7e9e9", "#ba2828")
                1 -> Pair("#e7f4e5", "#139f0d")
                else -> Pair("#faedea", "#cf532f")
            }
            binding.itemTextTv.setBackgroundColor(Color.parseColor(pair.first))
            binding.itemTextView.setBackgroundColor(Color.parseColor(pair.second))
            if (rowData.isHighlighted) {
                binding.itemTextTv.text = "random"
            } else {
                binding.itemTextTv.text = ""
            }
        }
    }

    inner class ButtonViewHolder(private val binding: RvItemButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rowData: RowData) {
            if (rowData.isHighlighted) {
                binding.itemButtonTv.background = highlightDrawable
                binding.itemButtonTv.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                binding.itemButtonTv.background = normalDrawable
                binding.itemButtonTv.setTextColor(Color.parseColor("#9C9C9C"))
            }
            binding.itemButtonTv.setOnClickListener {
                callback.onClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isButton) {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = RvItemTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.root.layoutParams.height = getScreenHeight() / itemCount
            TextViewHolder(
                binding
            )
        } else {
            val binding = RvItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.root.layoutParams.height = getScreenHeight() / itemCount
            ButtonViewHolder(
                binding
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TextViewHolder -> holder.bind(getItem(position), position)
            is ButtonViewHolder -> holder.bind(getItem(position))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<RowData>() {
        override fun areItemsTheSame(oldItem: RowData, newItem: RowData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RowData, newItem: RowData): Boolean {
            return oldItem == newItem
        }
    }

    override fun submitList(list: MutableList<RowData>?) {
        super.submitList(if (list != null) ArrayList(list) else null)
    }
}