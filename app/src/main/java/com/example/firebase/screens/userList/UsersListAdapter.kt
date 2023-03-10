package com.example.firebase.screens.userList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.R
import com.example.firebase.databinding.UserItemBinding
import com.example.firebase.models.User

class UsersListAdapter(private var data: MutableList<User>) :
    ListAdapter<User, UsersListAdapter.ViewHolder>(UserListCallBack()) {

    class ViewHolder(
        private val binding: UserItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(user: User?) {
            binding.tvUserName.text = user?.name
            binding.tvUserAge.text = user?.age.toString()
            binding.tvUserEmail.text = user?.email
            binding.root.setOnClickListener {
                val bundle = bundleOf("userName" to user?.name)
                bundle.putString("userAge", user?.age.toString())
                bundle.putString("userEmail", user?.email)
                bundle.putString("userId", user?.id)
                it.findNavController().navigate(R.id.action_rvUserList_to_rvUpdateUser, bundle)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it)
        }
    }
}

class UserListCallBack : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
        oldItem.id == newItem.id
}