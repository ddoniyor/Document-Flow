package com.example.documentflow.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.documentflow.R
import com.example.documentflow.data.model.Letters
import com.example.documentflow.databinding.ItemLettersBinding
import com.example.documentflow.databinding.ItemProfileBinding
import java.util.zip.Inflater

class LettersAdapter( private val callbackInterface: CallBackInterface): RecyclerView.Adapter<LettersAdapter.LettersViewHolder>() {

    private var letters = listOf<Letters.LettersList>()

    @SuppressLint("NotifyDataSetChanged")
    fun setLetters(letters:List<Letters.LettersList>){
        this.letters= letters
        notifyDataSetChanged()
    }

    class LettersViewHolder (view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemLettersBinding.bind(view)
    }

    interface CallBackInterface{
        fun letterClicked(id: Int?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LettersViewHolder {
        return LettersViewHolder(
            LayoutInflater.from(parent.context).inflate(
            R.layout.item_letters,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: LettersViewHolder, position: Int) {
        val letter = letters[position]
        with(holder){
            itemView.setOnClickListener {
                callbackInterface.letterClicked(letter.id)
            }
            binding.lettersItemIdText.text = letter.id.toString()
            binding.lettersItemNameText.text = letter.name
            binding.lettersItemSenderText.text = letter.sender
            /*binding.lettersItemEntryDateText.text = letter.entryDate
            binding.lettersItemDistributionDateText.text = letter.distributionDate*/
        }
    }

    override fun getItemCount(): Int {
        return letters.size
    }
}