package com.example.crudapplication.presentation.adpater;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crudapplication.R;
import com.example.crudapplication.data.model.UserProfile;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserProfile> users = new ArrayList<>();
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public void setUsers(List<UserProfile> newUsers) {
        // DiffUtil 연결
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new UserDiffutil(this.users, newUsers));
        this.users.clear();
        this.users.addAll(newUsers);
        diffResult.dispatchUpdatesTo(this); // dispatchUpdatesTo를 통해 변경사항을 리사이클러뷰에 적용
    }

    // 클릭리스너 인터페이스
    public interface OnItemClickListener{
        void onItemClick(UserProfile user);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.clickListener = listener;
    }

    // 롱클릭리스너 인터페이스
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

    // 데이터 연결
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // UserProfile데이터 가져와서 뿌려줌
        UserProfile user = users.get(position);
        holder.name.setText(user.getName());
        holder.phone.setText(user.getPhone());

        // 프로필 이미지 설정
        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(user.getProfileImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                holder.profileImage.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                Log.e("UserAdapter", "Base64 디코딩 실패: " + e.getMessage());
                // 이미지 로드 실패 시 기본 이미지 설정
                holder.profileImage.setImageResource(R.drawable.profile_img);
            }
        } else {
            // 프로필 이미지가 없을 경우 기본 이미지 설정
            holder.profileImage.setImageResource(R.drawable.profile_img);
        }

        // 클릭 이벤트
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null){
                clickListener.onItemClick(user);
            }
        });

        // 롱클링 이벤트
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(user);
            }
            return true;
        });
    }


    // 아이템 개수리턴
    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }


    // 레이아웃과 데이터 연결
    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        CircleImageView profileImage;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            phone = itemView.findViewById(R.id.tv_phone);
            profileImage = itemView.findViewById(R.id.iv_profile);
        }
    }
}

