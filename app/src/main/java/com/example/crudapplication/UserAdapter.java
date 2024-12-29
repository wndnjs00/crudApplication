package com.example.crudapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserProfile> users;
    private OnItemLongClickListener longClickListener;

    public void setUsers(List<UserProfile> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(UserProfile user);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserProfile user = users.get(position);
        holder.name.setText(user.getName());
        holder.phone.setText(user.getPhone());

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(user);
            }
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            phone = itemView.findViewById(R.id.tv_phone);
        }
    }
}

