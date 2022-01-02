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
import com.example.documentflow.databinding.FragmentAgreementsBinding
import com.example.documentflow.databinding.FragmentDetailAgreementsBinding
import com.example.documentflow.viewmodel.MyViewModel
import com.example.documentflow.viewmodel.factory.ViewModelFactory

class DetailAgreementsFragment : Fragment() {

    private companion object{
        const val TAG = "Detail Agreements Fragment"
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }
    private var _binding: FragmentDetailAgreementsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel
    private var agreementId: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("onCreateView")
        setUpViewModel()
        agreementId = arguments?.getInt("agreementId")!!
        _binding = FragmentDetailAgreementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentDetailAgreementsProgressBar.visibility = View.VISIBLE
        binding.fragmentDetailAgreementsMainLayout.visibility =View.GONE
        myViewModel.getAgreement(agreementId)
        getAgreementResponse()
        log("$agreementId")
    }

    private fun getAgreementResponse(){
        with(myViewModel){
            getAgreementResponse.observe(viewLifecycleOwner,{
                binding.fragmentDetailAgreementsProgressBar.visibility = View.GONE
                binding.fragmentDetailAgreementsMainLayout.visibility =View.VISIBLE
                if (it.code ==200 && it.payload!=null){
                    binding.fragmentDetailAgreementsIdText.text = it.payload!!.agreementId.toString()
                    binding.fragmentDetailAgreementsDepartmentIdText.text = it.payload!!.departmentId.toString()
                    if (it.payload!!.viewed==null){
                        binding.fragmentDetailAgreementsViewedText.text = "Нет"
                    }else{
                        binding.fragmentDetailAgreementsViewedText.text = "Да"
                    }
                    binding.fragmentDetailAgreementsAgreedAtText.text = it.payload!!.agreedAt
                    binding.fragmentDetailAgreementsDocumentsIdText.text = it.payload!!.letter.letterId.toString()
                    binding.fragmentDetailAgreementsNameText.text = it.payload!!.letter.name
                    binding.fragmentDetailAgreementsEntryDateText.text = it.payload!!.letter.entryDate
                    binding.fragmentDetailAgreementsDistributionDateText.text = it.payload!!.letter.distributionDate

                }else{
                    Toast.makeText(requireContext(),"код ${it.code}", Toast.LENGTH_SHORT).show()
                }
                log("$it")
            })
            errorGetAgreement.observe(viewLifecycleOwner,{
                binding.fragmentDetailAgreementsProgressBar.visibility = View.GONE
                binding.fragmentDetailAgreementsMainLayout.visibility =View.VISIBLE
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

}