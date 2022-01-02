package com.example.documentflow.view.fragment.documents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.documentflow.data.model.Agreements
import com.example.documentflow.data.retrofit.RetrofitBuilder
import com.example.documentflow.databinding.FragmentDocumentsDialogBinding
import com.example.documentflow.view.adapter.DepartmentsAdapter
import com.example.documentflow.viewmodel.MyViewModel
import com.example.documentflow.viewmodel.factory.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DocumentsDialogFragment :  BottomSheetDialogFragment(), DepartmentsAdapter.CallbackInterface {

    private companion object{
        const val TAG = "Documents Dialog Fragment"
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }
    private var _binding: FragmentDocumentsDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel
    private lateinit var departmentsAdapter: DepartmentsAdapter

    private var letterId = 0
    private var departmentId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("onCreateView")
        setUpViewModel()
        letterId = arguments?.getInt("letterIdForDoc")!!
        _binding = FragmentDocumentsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        departmentsAdapter = DepartmentsAdapter(this)
        binding.fragmentDocumentsDialogRecycler.adapter = departmentsAdapter

        binding.fragmentDocumentsDialogProgress.visibility = View.VISIBLE
        myViewModel.getDepartments()
        getDepartmentsResponse()
        postAgreementsResponse()

        binding.fragmentDocumentsDialogSendToAgreement.setOnClickListener {
            if (departmentId ==0){
                Toast.makeText(requireContext(),"Выберите департамент", Toast.LENGTH_SHORT).show()
            }else{
                binding.fragmentDocumentsDialogProgress.visibility = View.VISIBLE
                myViewModel.createAgreements(Agreements.CreateAgreementsRequest(
                    department_id = departmentId,
                    letter_id = letterId
                ))
            }
        }
    }

    private fun getDepartmentsResponse(){
        with(myViewModel){
            departmentsResponse.observe(viewLifecycleOwner,{
                binding.fragmentDocumentsDialogProgress.visibility = View.GONE
                if (it.code==200 && it.payload!=null){
                    departmentsAdapter.setDepartments(it.payload!!)
                }else{
                    Toast.makeText(requireContext(),"код ${it.code}", Toast.LENGTH_SHORT).show()
                }
            })
            errorDepartments.observe(viewLifecycleOwner,{
                binding.fragmentDocumentsDialogProgress.visibility = View.GONE
                Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
                log("$it")
            })
        }
    }

    private fun postAgreementsResponse(){
        with(myViewModel){
            createAgreementsResponse.observe(viewLifecycleOwner,{
                binding.fragmentDocumentsDialogProgress.visibility = View.GONE
                if (it.code==200 ){
                    Toast.makeText(requireContext(),"Запрос отправлен на согласование!",Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    dismiss()
                }else{
                    Toast.makeText(requireContext(),"код ${it.code}", Toast.LENGTH_SHORT).show()
                }
                log("$it")
            })
            errorCreateAgreements.observe(viewLifecycleOwner,{
                binding.fragmentDocumentsDialogProgress.visibility = View.GONE
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

    override fun departmentId(id: Int) {
        departmentId = id
    }

}