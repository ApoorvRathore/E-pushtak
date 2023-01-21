package com.apoorv.e_pushtak.activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.apoorv.e_pushtak.R
import com.apoorv.e_pushtak.database.Users
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var username: TextInputEditText
    private lateinit var useremail: TextInputEditText
    private lateinit var userpassword: TextInputEditText
    private lateinit var usercpassword: TextInputEditText
    private lateinit var register: Button
    private lateinit var signin: TextView
    private lateinit var userphone: TextInputEditText
    private lateinit var male: RadioButton
    private lateinit var female: RadioButton
    private lateinit var databaseReference: DatabaseReference
    private lateinit var gender: String
    private lateinit var myradiogroup: RadioGroup
    private lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreference: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )
        val isLogedIn = sharedPreference.getBoolean("Is Logged In", false)
        if (isLogedIn) {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_rgister)

        username = findViewById(R.id.NameET)
        useremail = findViewById(R.id.emailEt)
        userpassword = findViewById(R.id.passwordEt)
        usercpassword = findViewById(R.id.CpasswordEt)
        register = findViewById(R.id.Registerbtn)
        signin = findViewById(R.id.Signin)
        userphone = findViewById(R.id.phoneEt)
        male = findViewById(R.id.male)
        female = findViewById(R.id.female)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.GONE
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE
        myradiogroup = findViewById(R.id.radioGrp)
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")



        firebaseAuth = FirebaseAuth.getInstance()


        register.setOnClickListener {
            gender = ""
            val name: String = username.text.toString().trim()
            val phone: String = userphone.text.toString().trim()
            val email: String = useremail.text.toString().trim()
            val password: String = userpassword.text.toString().hashCode().toString().trim()
            val confirmpassword: String = usercpassword.text.toString().hashCode().toString().trim()
            gender = if (male.isChecked) {
                "Male"
            } else {
                "Female"
            }
            if (TextUtils.isEmpty(name)) {
                username.error = "Please enter your name"
            } else if (TextUtils.isEmpty(phone)) {
                userphone.error = "Plese enter you mobile no"
            } else if (TextUtils.isEmpty(email)) {
                useremail.error = "Please enter e-mail"
            } else if (TextUtils.isEmpty(password)) {
                userpassword.error = "Please enter password"
            } else if (TextUtils.isEmpty(confirmpassword)) {
                usercpassword.error = "Please enter confirm password"
            } else if (TextUtils.isEmpty(gender)) {
                Toast.makeText(this, "Plese select gender", Toast.LENGTH_SHORT)
                    .show()
            } else if ((!Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                useremail.error = "Invalid mail"
            } else if (password.length < 6) {
                Toast.makeText(this, "Password contain at-least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                progressLayout.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this
                    ) { task ->
                        if (task.isSuccessful) {

                            val information = Users(
                                name,
                                phone,
                                email,
                                password,
                                confirmpassword,
                                gender
                            )

                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .setValue(information).addOnCompleteListener {
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    progressLayout.visibility = View.GONE
                                    savedPreference()
                                    Toast.makeText(this@RegisterActivity, "Account Created", Toast.LENGTH_SHORT)
                                        .show()
                                }


                        }
                    }
            }

        }

        signin.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun savedPreference() {
        sharedPreference.edit().putBoolean("Is Logged In", true).apply()

    }
}




