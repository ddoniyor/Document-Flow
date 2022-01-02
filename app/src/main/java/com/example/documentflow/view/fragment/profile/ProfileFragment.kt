package com.example.documentflow.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.documentflow.App
import com.example.documentflow.LoginActivity
import com.example.documentflow.R
import com.example.documentflow.databinding.FragmentProfileBinding
import com.example.documentflow.utils.SharedPreferencesHelper
import com.example.documentflow.view.adapter.ProfileAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment(), ProfileAdapter.CallBackInterface {
    private companion object{
        const val TAG = "Profile Fragment"
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var profileAdapter: ProfileAdapter
    private var icons = intArrayOf( R.drawable.ic_profile_documents,R.drawable.ic_my_agreements, R.drawable.ic_profile_data, R.drawable.ic_profile_exit)
    private var titles = arrayListOf<String>(*App.resourses.getStringArray(R.array.titles))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("onCreateView")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        profileAdapter = ProfileAdapter(titles,icons,requireContext(),this)
        binding.profileFragmentRecycler.adapter = profileAdapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        log("onViewCreated")
        super.onViewCreated(view, savedInstanceState)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        log("onDestroyView")
    }

    override fun profileItemClicked(position: Int) {
        when(position){
            0->{
                log("Создание документа")
            }
            1->{
                if(findNavController().currentDestination?.id == R.id.profileFragment){
                    findNavController().navigate(R.id.action_profileFragment_to_agreementsFragment)
                }
            }
            2->{
                if (findNavController().currentDestination?.id == R.id.profileFragment) {
                    findNavController().navigate(R.id.action_profileFragment_to_userDataFragment)
                }
            }
            3->{
                logOutFromProfile()
            }
        }
    }
    private fun logOutFromProfile(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.exit_message))
            .setMessage(resources.getString(R.string.exit_description_message))
            .setNeutralButton(resources.getString(R.string.cancel_message)) { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton(resources.getString(R.string.no_message)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes_message)) { dialog, _ ->
                sharedPreferencesHelper.clearAccessToken()
                sharedPreferencesHelper.clearEnterState()

                activity?.let {
                    val intent = Intent(it, LoginActivity::class.java)
                    it.startActivity(intent)
                    it.finishAffinity()
                }
                dialog.dismiss()
            }
            .show()
    }



}