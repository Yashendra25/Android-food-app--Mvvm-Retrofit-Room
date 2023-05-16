package com.yashendra.foodorderingapp_easyfood.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.yashendra.foodorderingapp_easyfood.R
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val price=intent.getIntExtra("price",500)
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","Project Android Food App")
            options.put("description","Order")
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");

            val amount = (price* 100).toInt() // convert rupees to paise and cast to int
            options.put("amount",amount) // pass amount in currency subunits
//            options.put("amount", (adapter.total.toString().toFloat() * 100).toInt())


            val prefill = JSONObject()
            prefill.put("email","")
            prefill.put("contact","")

            options.put("prefill",prefill)
            co.open(this,options)

        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"payment Successful  ${p0}", Toast.LENGTH_LONG).show()
        println(p0)

        Log.d("payment",p0.toString())
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)


//        val dialog = Dialog(requireContext())
//        dialog.setContentView(R.layout.paymentstatus)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // to make the background transparent
//
//
//        val window = dialog.window
//        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        window?.setGravity(Gravity.CENTER)
//
//        dialog.show()
//
//        dialog.dismiss_button.setOnClickListener {
//            dialog.dismiss()
//        }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Error  ${p0}", Toast.LENGTH_LONG).show()
        println(p1)
        Log.d("payment",p1.toString())
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

//        val dialog = Dialog(requireContext())
//        dialog.setContentView(R.layout.paymentstatus)
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent) // to make the background transparent
//
//
//        val window = dialog.window
//        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        window?.setGravity(Gravity.CENTER)
//        dialog.payment_img.setImageResource(R.drawable.ic_baseline_cancel_24)
//
//        dialog.show()
//
//        dialog.dismiss_button.setOnClickListener {
//            dialog.dismiss()
//        }

    }
}