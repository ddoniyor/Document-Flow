package com.example.documentflow.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.documentflow.R
import com.example.documentflow.databinding.ItemProfileBinding
import com.example.documentflow.utils.SafeClickListener


class ProfileAdapter (private var titles:ArrayList<String>,
                      private var icons:IntArray,
                      private val context: Context,
                      private val callbackInterface: CallBackInterface
): RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    interface CallBackInterface{
        fun profileItemClicked(position: Int)
    }

    class ProfileViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = ItemProfileBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_profile,
            parent,
            false
        )
        return ProfileViewHolder(view)
    }


    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        with(holder){
            binding.profileItemText.text = titles[position]
            binding.profileItemIcon.setImageResource(icons[position])
            if (position == titles.size-1){
                binding.profileItemText.setTextColor(context.resources.getColor(R.color.red))
            }
            itemView.setOnClickListener(object : SafeClickListener(){
                override fun onSafeCLick(v: View?) {
                    callbackInterface.profileItemClicked(position)
                }
            })
        }


    }

    override fun getItemCount(): Int {
        return titles.size
    }
}