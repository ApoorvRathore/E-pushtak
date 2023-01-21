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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressLayout: RelativeLayout
    private lateinit var progressbar: ProgressBar
    private lateinit var emailEt: TextInputEditText
    private lateinit var passwordEt: TextInputEditText
    private lateinit var login: Button
    private lateinit var signup: TextView
    private lateinit var forget: TextView
    private lateinit var sharedPreference: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreference = getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )
        val isLogedIn = sharedPreference.getBoolean("Is Logged In", false)
        if (isLogedIn) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_login)


        emailEt = findViewById(R.id.emailEt)
        passwordEt = findViewById(R.id.passwordEt)
        login = findViewById(R.id.loginbtn)
        signup = findViewById(R.id.no_accountTV)
        forget = findViewById(R.id.forgotTV)
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.GONE
        progressbar = findViewById(R.id.progressBar)
        progressbar.visibility = View.GONE

        firebaseAuth = FirebaseAuth.getInstance()


        login.setOnClickListener {

            val email: String = emailEt.text.toString().trim()
            val password: String = passwordEt.text.toString().hashCode().toString().trim()


            if (!Patterns.EMAIL_ADDRESS.matcher(email)
                    .matches()
            ) {
                emailEt.error = "Invalid mail"
            } else if (TextUtils.isEmpty(email)) {
                emailEt.error = "Please enter email"
            } else if (TextUtils.isEmpty(password)) {
                passwordEt.error = "Please enter password"
            } else if (password.length < 6) {
                Toast.makeText(this, "Password contain at-least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                progressLayout.visibility = View.VISIBLE
                progressbar.visibility = View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this
                    ) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            progressLayout.visibility = View.GONE
                            Toast.makeText(applicationContext, "Logging Successfull", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                            savedPreference()
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "Log in failed ", Toast.LENGTH_SHORT).show()
                            progressLayout.visibility = View.GONE
                        }
                    }

            }
        }
        signup.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        forget.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetActivity::class.java)
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


