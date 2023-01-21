package com.apoorv.e_pushtak.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.apoorv.e_pushtak.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpVerification : AppCompatActivity() {
    lateinit var getotpback: String
    private lateinit var otp: EditText
    private lateinit var vrify: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        otp = findViewById(R.id.etOtp)
        vrify = findViewById(R.id.btnVerify)
        val textview: TextView =
            findViewById(R.id.txtMob)
        textview.setText(
            String.format(
                "+91-%s",
                intent.getStringExtra("Mobile No")
            )
        )
        val mAuth = FirebaseAuth.getInstance()
        mAuth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
        getotpback =
            intent.getStringExtra("BackOtp").toString()
        val progressVerify: ProgressBar =
            findViewById(R.id.progress_otp)
        vrify.setOnClickListener {
            if (!otp.text.toString().trim().isEmpty()) {
                val enterOtp: String =
                    otp.text.toString()
//                Toast.makeText(this,"Otp Verified",Toast.LENGTH_SHORT).show()
                progressVerify.visibility =
                    View.VISIBLE
                vrify.visibility = View.INVISIBLE
                val phoneauthentication: PhoneAuthCredential =
                    PhoneAuthProvider.getCredential(
                        getotpback,
                        enterOtp
                    )
                FirebaseAuth.getInstance()
                    .signInWithCredential(
                        phoneauthentication
                    )
                    .addOnCompleteListener { p0 ->
                        progressVerify.visibility =
                            View.GONE
                        vrify.visibility =
                            View.VISIBLE
                        if (p0.isSuccessful) {
                            val intent = Intent(
                                applicationContext,
                                MainActivity::class.java
                            )
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@OtpVerification,
                                "Please enter correct otp",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this,
                    "Please enter otp",
                    Toast.LENGTH_SHORT
                ).show()
            }
            findViewById<TextView>(R.id.resendOtp).setOnClickListener {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91 ${intent.getStringExtra("Mobile No")}",
                    60,
                    TimeUnit.SECONDS,
                    this@OtpVerification,
                    object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                        override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                        }

                        override fun onVerificationFailed(p0: FirebaseException) {

                            Toast.makeText(this@OtpVerification, "Failed due to ${p0.message}", Toast.LENGTH_SHORT)
                                .show()
                        }

                        override fun onCodeSent(backendOtp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                            getotpback = backendOtp
                            Toast.makeText(this@OtpVerification, "Otp sent sucessfully", Toast.LENGTH_SHORT).show()

                        }

                    })
            }
        }
    }
}