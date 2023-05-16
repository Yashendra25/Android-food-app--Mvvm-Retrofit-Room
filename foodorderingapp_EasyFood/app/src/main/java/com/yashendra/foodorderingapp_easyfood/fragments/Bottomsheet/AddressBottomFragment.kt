package com.yashendra.foodorderingapp_easyfood.fragments.Bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yashendra.foodorderingapp_easyfood.Activities.MainActivity
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_address_bottom.*

class AddressBottomFragment : BottomSheetDialogFragment() {
    private lateinit var pincode:String
    private lateinit var address:String
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        chenageaddressbtn.setOnClickListener {
            pincode=ed_pincode.text.toString()
            address=ed_address.text.toString()
            val data=pincode+" "+address
            if (pincode.isNotEmpty() and address.isNotEmpty())
            {
                val newText = data
                 viewModel.setaddressdata(data)
                dismiss()

            }


        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AddressBottomFragment().apply {

            }
    }
}