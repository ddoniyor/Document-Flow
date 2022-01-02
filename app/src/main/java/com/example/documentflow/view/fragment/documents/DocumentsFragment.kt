package com.example.documentflow.view.fragment.documents

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
import com.example.documentflow.data.model.Letters
import com.example.documentflow.data.retrofit.RetrofitBuilder
import com.example.documentflow.databinding.FragmentDocumentsBinding
import com.example.documentflow.view.adapter.LettersAdapter
import com.example.documentflow.viewmodel.MyViewModel
import com.example.documentflow.viewmodel.factory.ViewModelFactory

class DocumentsFragment : Fragment(), LettersAdapter.CallBackInterface {
    private companion object{
        const val TAG = "Documents Fragment"
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }

    private var _binding: FragmentDocumentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel
    private lateinit var lettersAdapter: LettersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("onCreateView")
        setUpViewModel()
        _binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("onViewCreated")
        lettersAdapter = LettersAdapter(this)
        binding.documentsFragmentRecycler.adapter = lettersAdapter

        binding.documentsFragmentProgressBar.visibility = View.VISIBLE
        myViewModel.getLetterList(
            Letters.LettersRequest(
                rows_offset = 0,
                sender = "",
                name = "",
                rows_limit = 10
            )
        )
        getLetterListResponse()
    }

    private fun getLetterListResponse(){
        with(myViewModel){
            lettersListResponse.observe(viewLifecycleOwner,{
                binding.documentsFragmentProgressBar.visibility = View.GONE
                if (it.code == 200 && it.payload!=null){
                    lettersAdapter.setLetters(it.payload!!)
                }else{
                    Toast.makeText(requireContext(),"код ${it.code}", Toast.LENGTH_SHORT).show()
                }
                log("$it lettersListResponse")
            })
            errorLetterList.observe(viewLifecycleOwner,{
                binding.documentsFragmentProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(),"$it",Toast.LENGTH_SHORT).show()
                log("$it errorLetterList")
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

    override fun letterClicked(id: Int?) {
        val bundle = Bundle()
        if (id!=null){
            bundle.putInt("letterId",id)
            if(findNavController().currentDestination?.id == R.id.documentsFragment){
                findNavController().navigate(R.id.action_documentsFragment_to_detailDocumentsFragment,bundle)
            }
        }
    }
}