package com.yashendra.foodorderingapp_easyfood.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.yashendra.foodorderingapp_easyfood.Activities.MainActivity
import com.yashendra.foodorderingapp_easyfood.Activities.PaymentActivity
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.adapters.CartAdapter
import com.yashendra.foodorderingapp_easyfood.Interface.cartAdapter
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import com.yashendra.foodorderingapp_easyfood.dataclasses.MealWithPrice
import com.yashendra.foodorderingapp_easyfood.db.fbdao
import com.yashendra.foodorderingapp_easyfood.fragments.Bottomsheet.AddressBottomFragment
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : Fragment(), cartAdapter {
    private lateinit var fbdao: fbdao
    private lateinit var adapter: CartAdapter
    private lateinit var new_address:String
    private lateinit var textViewAddres: TextView
    var price:Int=0
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

        return inflater.inflate(R.layout.fragment_cart, container, false)

    }

    private fun makepayment(price:Int) {
        val intent = Intent(requireContext(),PaymentActivity::class.java)
        startActivity(intent)
        intent.putExtra("price",price)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        textViewAddres = view.findViewById<TextView>(R.id.location_bar_text)
        btn_buy.setOnClickListener {
            makepayment(price)
        }
        location_ll.setOnClickListener {
            val AddressBottomFragment= AddressBottomFragment()
            AddressBottomFragment.show(childFragmentManager,"MyBottomSheetAddressDialogFragment")
        }
        Observeaddress()


    }

    private fun Observeaddress() {
        viewModel.ObserveAddressLivedata().observe(viewLifecycleOwner, Observer {
            textViewAddres.text=it
        })
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        adapter.stopListening()
    }
    fun setupRecyclerview() {
        val query = FirebaseFirestore.getInstance().collection("Cart")
        val recyclerOptions = FirestoreRecyclerOptions.Builder<MealWithPrice>()
            .setQuery(query, MealWithPrice::class.java)
            .build()

        adapter= CartAdapter(recyclerOptions,this)

        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(requireContext())
    }
    override fun ondeleteClicked(mealid: String) {
        super.ondeleteClicked(mealid)
        fbdao= fbdao()
        fbdao.deleteCartDocument(mealid)

        val snackbar = Snackbar.make(requireView(), "Item deleted from cart", Snackbar.LENGTH_LONG)

        snackbar.setAction("OK") {
            snackbar.view.setBackgroundColor(Color.GREEN) // change background color of snackbar
            snackbar.dismiss() // dismiss snackbar when action button is clicked
        }

        snackbar.show()
    }

    override fun onTotalPriceUpdated(totalPrice: Int) {

        // Update the total price view with the new value
        val tvTotalPrice =tv_total_price
        price=totalPrice

        tvTotalPrice.text = "Total:${totalPrice}"
    }


    override fun onResume() {
        super.onResume()

    }



}