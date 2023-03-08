package com.example.firebase.screens.userList

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.R
import com.example.firebase.databinding.FragmentUsersListBinding

private const val TAG = "UsersListFragment"

class UsersListFragment : Fragment(R.layout.fragment_users_list) {
    private var binding: FragmentUsersListBinding? = null
    private lateinit var viewModel: UsersListViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        binding = FragmentUsersListBinding.bind(view)

        // ViewModel Setup
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[UsersListViewModel::class.java]
        binding?.lifecycleOwner = viewLifecycleOwner

        // Recyclerview Setup
        val adapter = UsersListAdapter(viewModel.userList.value ?: mutableListOf())
        viewModel.userList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        // Swipe Effect
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteUser(viewHolder.adapterPosition)
            }

        }).attachToRecyclerView(binding?.rvUserList)

        // recyclerView
        binding?.rvUserList?.adapter = adapter
        binding?.rvUserList?.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )

        // inflate Option menu
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.users_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delteAll -> showDialogMessage()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // fab clickListener
        binding?.fab?.setOnClickListener {
            it.findNavController().navigate(R.id.action_rvUserList_to_rvAddUser)
        }
    }

    private fun showDialogMessage() {
        val dialogMessage = AlertDialog.Builder(requireContext())
            .setTitle("Delete All Users ?")
            .setMessage("If Click Yes, All Users Will be Deleted")
        dialogMessage.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        dialogMessage.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteAllUsers()

        }
        dialogMessage.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}