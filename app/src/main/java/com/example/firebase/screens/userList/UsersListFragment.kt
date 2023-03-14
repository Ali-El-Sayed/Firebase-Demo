package com.example.firebase.screens.userList

import android.os.Bundle
import android.view.*
import androidx.annotation.MenuRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.R
import com.example.firebase.databinding.FragmentUsersListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

private const val TAG = "UsersListFragment"

class UsersListFragment : Fragment(R.layout.fragment_users_list) {
    private var binding: FragmentUsersListBinding? = null
    private lateinit var viewModel: UsersListViewModel
    private lateinit var adapter: UsersListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        binding = FragmentUsersListBinding.bind(view)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        // ViewModel Setup
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[UsersListViewModel::class.java]
        binding?.lifecycleOwner = viewLifecycleOwner

        // Recyclerview Setup
        viewModel.userList.observe(viewLifecycleOwner) {
            adapter = UsersListAdapter(viewModel.userList.value ?: mutableListOf())
            adapter.submitList(it)
            binding?.rvUserList?.adapter = adapter
        }

        // Hide Fab with Scrolling
        binding?.rvUserList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding?.fab?.visibility == View.VISIBLE) binding?.fab?.hide()
                else if (dy < 0 && binding?.fab?.visibility != View.VISIBLE) binding?.fab?.show()
            }
        })

        // Swipe Effect
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteUser(viewHolder.adapterPosition)
            }

        }).attachToRecyclerView(binding?.rvUserList)

        // recyclerView
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
                    R.id.menu_log_out -> logOut()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // fab clickListener
        binding?.fab?.setOnClickListener {
            it.findNavController().navigate(R.id.action_rvUserList_to_rvAddUser)
        }
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.rvUserList, true).build()
        findNavController().navigate(R.id.action_rvUserList_to_fLogin, null, options)
    }

    private fun showDialogMessage() {
        val dialogMessage = AlertDialog.Builder(requireContext()).setTitle("Delete All Users ?")
            .setMessage("Click Yes to delete all users")
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