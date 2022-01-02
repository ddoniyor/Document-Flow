package com.example.documentflow.view.fragment.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.documentflow.R
import com.example.documentflow.data.retrofit.RetrofitBuilder
import com.example.documentflow.databinding.FragmentAgreementsBinding
import com.example.documentflow.databinding.FragmentDocumentsBinding
import com.example.documentflow.view.adapter.AgreementsAdapter
import com.example.documentflow.viewmodel.MyViewModel
import com.example.documentflow.viewmodel.factory.ViewModelFactory


class AgreementsFragment : Fragment(), AgreementsAdapter.CallBackInterface {
    private companion object{
        const val TAG = "Agreements Fragment"
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }
    private var _binding: FragmentAgreementsBinding? = null
    private val binding get() = _binding!!
    private lateinit var agreementsAdapter: AgreementsAdapter
    private lateinit var myViewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("onCreateView")
        setUpViewModel()
        _binding = FragmentAgreementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        agreementsAdapter = AgreementsAdapter(this)
        binding.agreementsFragmentRecycler.adapter = agreementsAdapter

        binding.agreementsFragmentProgressBar.visibility = View.VISIBLE
        myViewModel.getAgreements()
        getAgreementsResponse()
    }

    private fun getAgreementsResponse(){
        with(myViewModel){
            getAgreementsResponse.observe(viewLifecycleOwner,{
                binding.agreementsFragmentProgressBar.visibility = View.GONE
                if (it.code==200 && it.payload!=null){
                    agreementsAdapter.setAgreements(it.payload!!)
                }else{
                    Toast.makeText(requireContext(),"код ${it.code}", Toast.LENGTH_SHORT).show()
                }
                log("$it")
            })
            errorGetAgreements.observe(viewLifecycleOwner,{
                binding.agreementsFragmentProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                log("$it")
            })
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        log("onDestroyView")
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

    override fun agreementClicked(id: Int?) {
        log("$id")
        val bundle = Bundle()
        if (id!=null){
            bundle.putInt("agreementId",id)
            if(findNavController().currentDestination?.id == R.id.agreementsFragment){
                findNavController().navigate(R.id.action_agreementsFragment_to_detailAgreementsFragment,bundle)
            }
        }
    }
}