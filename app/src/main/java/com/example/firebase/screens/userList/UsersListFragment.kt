package com.example.firebase.screens.userList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.R
import com.example.firebase.databinding.FragmentUsersListBinding

class UsersListFragment : Fragment(R.layout.fragment_users_list) {
    private var binding: FragmentUsersListBinding? = null
    private lateinit var viewModel: UsersListViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUsersListBinding.bind(view)

        // ViewModel Setup
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[UsersListViewModel::class.java]

        // Recyclerview Setup
        val adapter = UsersListAdapter(viewModel.userList.value ?: mutableListOf())
        viewModel.userList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding?.rvUserList?.adapter = adapter
        binding?.rvUserList?.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        // fab clickListener
        binding?.fab?.setOnClickListener {
            it.findNavController().navigate(R.id.action_rvUserList_to_rvAddUser)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}