package com.example.crudapplication.presentation.adpater

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.crudapplication.R
import com.example.crudapplication.data.model.UserProfile
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val users: MutableList<UserProfile> = mutableListOf()
    private var clickListener: OnItemClickListener? = null
    private var longClickListener: OnItemLongClickListener? = null

    fun setUsers(newUsers: List<UserProfile>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffutil(this.users, newUsers))
        this.users.clear()
        this.users.addAll(newUsers)
        diffResult.dispatchUpdatesTo(this)
    }

    fun interface OnItemClickListener {
        fun onItemClick(user: UserProfile)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

    fun setOnItemClickListener(block: (UserProfile) -> Unit) {
        this.clickListener = OnItemClickListener { user -> block(user) }
    }

    fun interface OnItemLongClickListener {
        fun onItemLongClick(user: UserProfile)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.longClickListener = listener
    }

    fun setOnItemLongClickListener(block: (UserProfile) -> Unit) {
        this.longClickListener = OnItemLongClickListener { user -> block(user) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.name.text = user.name
        holder.phone.text = user.phone

        val imageBase64 = user.profileImage
        if (!imageBase64.isNullOrEmpty()) {
            try {
                val decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.profileImage.setImageBitmap(bitmap)
            } catch (e: IllegalArgumentException) {
                Log.e("UserAdapter", "Base64 디코딩 실패: ${e.message}")
                holder.profileImage.setImageResource(R.drawable.profile_img)
            }
        } else {
            holder.profileImage.setImageResource(R.drawable.profile_img)
        }

        holder.itemView.setOnClickListener {
            clickListener?.onItemClick(user)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener?.onItemLongClick(user)
            true
        }
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val phone: TextView = itemView.findViewById(R.id.tv_phone)
        val profileImage: CircleImageView = itemView.findViewById(R.id.iv_profile)
    }
} 