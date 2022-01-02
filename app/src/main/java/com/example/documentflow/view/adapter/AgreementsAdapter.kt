package com.example.documentflow.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.documentflow.R
import com.example.documentflow.data.model.Agreements
import com.example.documentflow.databinding.ItemAgreementsBinding

class AgreementsAdapter(private val callbackInterface: CallBackInterface) :
    RecyclerView.Adapter<AgreementsAdapter.AgreementsViewHolder>() {

    private var agreements = listOf<Agreements.AgreementsPayload>()

    interface CallBackInterface{
        fun agreementClicked(id: Int?)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setAgreements(agreements:List<Agreements.AgreementsPayload>){
        this.agreements= agreements
        notifyDataSetChanged()
    }
    class AgreementsViewHolder(view: View): RecyclerView.ViewHolder(view)  {
        val binding = ItemAgreementsBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgreementsViewHolder {
        return AgreementsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_agreements,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AgreementsViewHolder, position: Int) {
        val agreement = agreements[position]
        with(holder){
            itemView.setOnClickListener {
                callbackInterface.agreementClicked(agreement.agreementId)
            }
            binding.agreementsItemIdText.text = agreement.agreementId.toString()
            binding.agreementsItemDepartmentIdText.text = agreement.departmentId.toString()
            if (agreement.viewed==null){
                binding.agreementsItemViewedText.text = "Нет"
            }else{
                binding.agreementsItemViewedText.text = "Да"
            }
            binding.agreementsItemAgreedAtText.text = agreement.agreedAt
        }
    }

    override fun getItemCount(): Int {
        return agreements.size
    }
}