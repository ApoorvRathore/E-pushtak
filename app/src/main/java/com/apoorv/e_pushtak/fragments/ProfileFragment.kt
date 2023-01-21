package com.apoorv.e_pushtak.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.apoorv.e_pushtak.R
import com.apoorv.e_pushtak.database.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class ProfileFragment : Fragment() {
    private lateinit var imgProfile: ImageSwitcher
    private lateinit var txtUserName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPhone: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var user: Users
    private lateinit var databaseReference: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?):
            View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        imgProfile = view.findViewById(R.id.imgProfile)
        txtUserName = view.findViewById(R.id.txtUserName)
        txtEmail = view.findViewById(R.id.txtUserEmail)
        txtPhone = view.findViewById(R.id.txtPhoneNumber)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        user = Users()
        getData()

        return view

    }


    private fun getData() {
        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(Users::class.java)!!
                    txtUserName.text = user.name
                    txtEmail.text = user.email
                    txtPhone.text = user.phoneno
                }

                override fun onCancelled(error: DatabaseError) {
                    (
                            Toast.makeText(
                                context, "Failed to load data due to ${error.message}", Toast
                                    .LENGTH_SHORT
                            ).show()
                            )
                }

            })

    }


}