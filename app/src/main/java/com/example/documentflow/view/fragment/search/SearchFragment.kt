package com.example.documentflow.view.fragment.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.documentflow.R
import com.example.documentflow.data.model.Letters
import com.example.documentflow.data.retrofit.RetrofitBuilder
import com.example.documentflow.databinding.FragmentSearchBinding
import com.example.documentflow.databinding.FragmentUserDataBinding
import com.example.documentflow.view.adapter.LettersAdapter
import com.example.documentflow.viewmodel.MyViewModel
import com.example.documentflow.viewmodel.factory.ViewModelFactory


class SearchFragment : Fragment(), LettersAdapter.CallBackInterface {
    private companion object{
        const val TAG = "Search Data Fragment"
    }
    private fun log(message: String) {
        Log.d(TAG, message)
    }
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var myViewModel: MyViewModel
    private lateinit var lettersAdapter: LettersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("onCreateView")
        setUpViewModel()
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchFragmentTextView.text = "Введите хотя бы 3 символа..."

        lettersAdapter = LettersAdapter(this)
        binding.searchFragmentRecycler.adapter = lettersAdapter
        getLetterListResponse()

        binding.searchFragmentEdit.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText?.length!!>=3){
                    binding.searchFragmentProgressBar.visibility = View.VISIBLE
                    binding.searchFragmentTextView.visibility =View.GONE
                    myViewModel.getLetterList(Letters.LettersRequest(
                        rows_offset = 0,
                        sender = "",
                        name = newText,
                        rows_limit = 10
                    ))

                }else if (newText.length<3){
                    lettersAdapter.setLetters(emptyList())
                    binding.searchFragmentProgressBar.visibility = View.GONE
                    binding.searchFragmentTextView.visibility = View.VISIBLE
                    binding.searchFragmentTextView.text = "Введите хотя бы 3 символа..."
                }
                return false
            }
        })
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

    private fun getLetterListResponse(){
        with(myViewModel){
            lettersListResponse.observe(viewLifecycleOwner,{
                binding.searchFragmentProgressBar.visibility = View.GONE
                if (it.code == 200){
                    if (it.payload !=null){
                        lettersAdapter.setLetters(it.payload!!)
                    }else{
                        lettersAdapter.setLetters(emptyList())
                        binding.searchFragmentTextView.visibility = View.VISIBLE
                        binding.searchFragmentTextView.text = "По данному запросу ничего не найдено"
                    }
                }else{
                    Toast.makeText(requireContext(),"код ${it.code}", Toast.LENGTH_SHORT).show()
                }
                log("$it lettersListResponse")
            })
            errorLetterList.observe(viewLifecycleOwner,{
                binding.searchFragmentProgressBar.visibility = View.GONE
                Toast.makeText(requireContext(),"$it", Toast.LENGTH_SHORT).show()
                log("$it errorLetterList")
            })
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        log("onDestroyView")
    }

    override fun letterClicked(id: Int?) {
        val bundle = Bundle()
        if (id!=null){
            bundle.putInt("letterId",id)
            if(findNavController().currentDestination?.id == R.id.searchFragment){
                findNavController().navigate(R.id.action_searchFragment_to_detailDocumentsFragment,bundle)
            }
        }
    }
}