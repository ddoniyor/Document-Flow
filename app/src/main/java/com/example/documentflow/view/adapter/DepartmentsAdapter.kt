package com.example.documentflow.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.documentflow.R
import com.example.documentflow.data.model.Departments
import com.example.documentflow.databinding.ItemDepartmentsBinding

class DepartmentsAdapter(private val callbackInterface: CallbackInterface) : RecyclerView.Adapter<DepartmentsAdapter.DepartmentsViewHolder>()  {
    private var departments = listOf<Departments.DepartmentsList>()
    private var selectedPosition = -1
    @SuppressLint("NotifyDataSetChanged")
    fun setDepartments(departments:List<Departments.DepartmentsList>){
        this.departments = departments
        notifyDataSetChanged()
    }


    interface CallbackInterface {
        fun departmentId(id:Int)
    }

    class DepartmentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemDepartmentsBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentsViewHolder {
        return DepartmentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_departments, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DepartmentsViewHolder, position: Int) {
        val department = departments[position]
        with(holder){
            binding.itemDepartmentsText.text = department.name

            itemView.setOnClickListener {
                if (itemCount>1){
                    selectedPosition = position
                    notifyDataSetChanged()
                    callbackInterface.departmentId(department.id!!)
                }

            }

            if (selectedPosition == position){
                binding.itemDepartmentsRadioButton.setImageResource(R.drawable.ic_selected)
            }else{
                binding.itemDepartmentsRadioButton.setImageResource(R.drawable.ic_unselected)
            }
        }

    }

    override fun getItemCount(): Int {
        return departments.size
    }
}