package com.example.crudapplication;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

class UserDiffutil extends DiffUtil.Callback {
    private final List<UserProfile> oldList;
    private final List<UserProfile> newList;

    public UserDiffutil(List<UserProfile> oldList, List<UserProfile> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
