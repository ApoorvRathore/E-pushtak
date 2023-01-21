package com.apoorv.e_pushtak.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.apoorv.e_pushtak.R


class AboutFragment : Fragment() {
    private lateinit var txtTitle: TextView
    private lateinit var txtAbout: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        txtTitle = view.findViewById(R.id.txtdescription)
        txtAbout = view.findViewById(R.id.txtAboutApp)
        aboutApp()
        return view

    }

    private fun aboutApp() {
        txtAbout.text = "App Name: E-Pushtak \n\n" +
                "Developed by: Apoorv Rathore\n\n" +
                "From: Internshala Trainings \n\n" +
                "Language used : XML and Kotlin \n\n" +
                "This is a simple book app which is made from Kotlin.\n" +
                "You can see description of books which is present in our application" +
                "You can add books to favourite and remove from it. \n" +
                "User can also create account on this app"
    }

}
