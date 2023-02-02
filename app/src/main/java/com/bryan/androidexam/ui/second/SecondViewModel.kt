package com.bryan.androidexam.ui.second

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecondViewModel : ViewModel() {

    val columnDataListLiveData: MutableLiveData<MutableList<ColumnData>?> = MutableLiveData()

    /**
     * create ColumnDataList by columnNumber and rowNumber
     */
    fun createList(columnNumber: Int, rowNumber: Int) {
        val columnList = mutableListOf<ColumnData>()
        for (i in 0 until columnNumber) {
            val rowList = mutableListOf<RowData>()
            for (j in 0 until rowNumber) {
                rowList.add(RowData())
            }
            rowList.add(RowData(isButton = true))
            columnList.add(ColumnData(rowDataList = rowList))
        }
        columnDataListLiveData.value = columnList
    }

    var columnHighlightIndex = -1
    var rowHighlightIndex = -1

    /**
     * get random highlight index of column and row
     */
    fun randomHighlight(columnNumber: Int, rowNumber: Int) {
        columnHighlightIndex = (0 until columnNumber).random()
        rowHighlightIndex = (0 until rowNumber).random()
        val columnList = columnDataListLiveData.value
        if (columnList != null) {
            val rowList = columnList[columnHighlightIndex].rowDataList
            rowList[rowHighlightIndex] = RowData(isHighlighted = true)
            rowList[rowList.lastIndex] = RowData(isHighlighted = true, true) // Button
            columnList[columnHighlightIndex] = ColumnData(true, rowList)
            columnDataListLiveData.value = columnList
        }
    }

    fun clearHighlight() {
        val columnList = columnDataListLiveData.value
        if (columnList != null && columnHighlightIndex > -1 && rowHighlightIndex > -1) {
            val rowDataList = columnList[columnHighlightIndex].rowDataList
            rowDataList[rowHighlightIndex] = RowData()
            rowDataList[rowDataList.lastIndex] = RowData(isButton = true)
            columnList[columnHighlightIndex] = ColumnData(rowDataList = rowDataList)
            columnHighlightIndex = -1
            rowHighlightIndex = -1
            columnDataListLiveData.value = columnList
        }
    }

    fun clearHighlightWithColumnIndex(columnIndex: Int) {
        if (columnIndex == columnHighlightIndex) {
            clearHighlight()
        }
    }

}