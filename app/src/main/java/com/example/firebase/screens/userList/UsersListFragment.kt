package com.example.firebase.screens.userList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.firebase.R
import com.example.firebase.databinding.FragmentUpdateUserBinding
import com.example.firebase.databinding.FragmentUsersListBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UsersListFragment : Fragment(R.layout.fragment_users_list) {
    private var binding: FragmentUsersListBinding? = null
    private lateinit var userListViewmodel: UsersListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsersListBinding.bind(view)
        userListViewmodel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[UsersListViewModel::class.java]

        binding?.fab?.setOnClickListener {
            it.findNavController().navigate(R.id.action_rvUserList_to_rvAddUser)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}