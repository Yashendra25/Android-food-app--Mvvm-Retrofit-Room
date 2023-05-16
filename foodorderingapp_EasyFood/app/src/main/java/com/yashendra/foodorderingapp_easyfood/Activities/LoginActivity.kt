package com.yashendra.foodorderingapp_easyfood.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yashendra.foodorderingapp_easyfood.R
import com.yashendra.foodorderingapp_easyfood.ViewModel.HeaderViewModel
import com.yashendra.foodorderingapp_easyfood.ViewModel.HeaderViwModelFactory
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomeViewModel
import com.yashendra.foodorderingapp_easyfood.ViewModel.HomelViewModelFactory
import com.yashendra.foodorderingapp_easyfood.dataclasses.User
import com.yashendra.foodorderingapp_easyfood.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header_main.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbreference: DatabaseReference
//    private lateinit var viewModel: HeaderViewModel
    object UserSession {
        var Uname: String = ""
        var Uuserid: String = ""
        var Uemail: String = ""
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
//        viewModel=androidx.lifecycle.ViewModelProviders.of(this)[HeaderViewModel::class.java]
//       viewModel = ViewModelProvider(this, HeaderViwModelFactory()).get(HeaderViewModel::class.java)

        auth=FirebaseAuth.getInstance()

        val sharedPrefs = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val email_saved = sharedPrefs.getString("email", null)
        val password_saved = sharedPrefs.getString("password", null)

        if (email_saved != null && password_saved != null) {
            val intent= Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }



        btn_sign_in.setOnClickListener {
                val email=ed_email.text.toString()
                val password=ed_password.text.toString()
                login(email,password)
        }
        btn_sign_up.setOnClickListener {
            val intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
        loadingdetails_In_nav_Header()

    }


    fun savingcredentials(email: String,password: String) {
        val sharedPrefs = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun login(email: String, password: String) {
        println("email $email"+"password $password")
        //logic for log in
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user=auth.currentUser
                    savingcredentials(email, password)


                    //jump to the main activity
                    val intent= Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    print("error is:$task")
                    Toast.makeText(this@LoginActivity, "User Does Not exist", Toast.LENGTH_SHORT).show()

                }
            }

    }

    private fun loadingdetails_In_nav_Header() {
        print("control aya")
        val uid=auth.currentUser?.uid!!
        dbreference= FirebaseDatabase.getInstance("https://android-food-app-12ee0-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
        dbreference.child("user")
        dbreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    for (uidvalue in childSnapshot.children){
                        val name: String = uidvalue.getValue(User::class.java)!!.name!!
                        val userid: String = uidvalue.getValue(User::class.java)!!.uid!!
                        val email: String = uidvalue.getValue(User::class.java)!!.email!!

                        if(uid.equals(userid))
                        {
                            Log.d("controlchecker",uid)
//                            viewModel.setheadername(name)
//                            viewModel.setheaderemail(email)

                            UserSession.Uname=name
                            UserSession.Uuserid = userid
                            UserSession.Uemail = email

                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })


    }


    }