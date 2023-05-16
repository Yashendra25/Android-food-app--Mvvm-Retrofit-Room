package com.yashendra.foodorderingapp_easyfood.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.dataclasses.User
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbreference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth=FirebaseAuth.getInstance()
        btn_sign_up.setOnClickListener {
            val name=ed_name.text.toString()
            val email=ed_email.text.toString()
            val password=ed_password.text.toString()

            signup(name,email,password)
        }
    }

    private fun signup(name: String, email: String, password: String) {
        //logic of creating user
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //jump to the main activity
                    addusertoTheDatabase(name,email, auth.currentUser?.uid!!)

                    //saving user to shared prefernces
                    val sharedPrefs = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    
                    editor.putString("email", email)
                    editor.putString("password", password)
                    editor.apply()

                    val intent= Intent(this@SignUpActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    print(task.result)
                    Toast.makeText(this, "Unable to sign up:some error occured", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addusertoTheDatabase(name: String, email: String, uid: String) {
        dbreference= FirebaseDatabase.getInstance("https://android-food-app-12ee0-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
        dbreference.child("user").child(uid).setValue(User(name,email, uid)).addOnSuccessListener {
            println("control aya")

        }
    }
}