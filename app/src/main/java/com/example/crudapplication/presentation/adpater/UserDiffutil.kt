package com.example.crudapplication.presentation.adpater

import androidx.recyclerview.widget.DiffUtil
import com.example.crudapplication.data.model.UserProfile

class UserDiffutil(
    private val oldList: List<UserProfile>,
    private val newList: List<UserProfile>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].uuid == newList[newItemPosition].uuid

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].uuid == newList[newItemPosition].uuid
} 