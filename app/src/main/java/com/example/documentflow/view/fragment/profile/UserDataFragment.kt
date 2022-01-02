package com.example.documentflow.view.fragment.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.documentflow.R
import com.example.documentflow.data.retrofit.RetrofitBuilder
import com.example.documentflow.databinding.FragmentDocumentsBinding
import com.example.documentflow.databinding.FragmentUserDataBinding
import com.example.documentflow.viewmodel.MyViewModel
import com.example.documentflow.viewmodel.factory.ViewModelFactory


class UserDataFragment : Fragment() {
    private companion object{
        const val TAG = "User Data Fragment"
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }
    private var _binding: FragmentUserDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("onCreateView")
        setUpViewModel()
        _binding = FragmentUserDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserDataResponse()
        binding.fragmentUserDataProgressBar.visibility = View.VISIBLE
        myViewModel.getUserData(1)
    }
    private fun getUserDataResponse(){
        with(myViewModel){
            userDataResponse.observe(viewLifecycleOwner,{userdata->
                binding.fragmentUserDataProgressBar.visibility = View.GONE
                if (userdata.code==200 && userdata.payload!=null){
                    binding.fragmentUserDataName.setText(userdata.payload!!.fullName)
                    binding.fragmentUserDataRole.setText(userdata.payload!!.role!!.role)
                    binding.fragmentUserDataEmail.setText(userdata.payload!!.email)
                    binding.fragmentUserDataDepartment.setText(userdata.payload!!.department.name)
                }else{
                    Toast.makeText(requireContext(),"код ${userdata.code}", Toast.LENGTH_SHORT).show()
                }
                log("$userdata")
            })
            errorUserData.observe(viewLifecycleOwner,{
                binding.fragmentUserDataProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                log("$it")
            })
        }
    }

    private fun setUpViewModel() {
        myViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(RetrofitBuilder().getApiInterface(requireContext()))
            ).get(
                MyViewModel::class.java
            )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        log("onDestroyView")
    }
}