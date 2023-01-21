package com.apoorv.e_pushtak.activity



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.apoorv.e_pushtak.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit



class ForgetActivity : AppCompatActivity() {
    private lateinit var mobilenumber:EditText
    private lateinit var getotp:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)
        mobilenumber = findViewById(R.id.mobile)
        getotp = findViewById(R.id.getotp)
        val progressbar:ProgressBar = findViewById(R.id.progress_otp)


        getotp.setOnClickListener {
            if (!mobilenumber.text.toString().trim().isEmpty()){
                if ((mobilenumber.text.toString().trim()).length==10){
                    progressbar.visibility = View.VISIBLE
                    getotp.visibility = View.INVISIBLE

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91 ${mobilenumber.text}",
                        60,
                        TimeUnit.SECONDS,
                        this@ForgetActivity,
                        object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                                progressbar.visibility = View.GONE
                                getotp.visibility = View.VISIBLE
                            }

                            override fun onVerificationFailed(p0: FirebaseException) {
                                progressbar.visibility = View.GONE
                                getotp.visibility = View.VISIBLE
                                Toast.makeText(this@ForgetActivity,"Failed due to ${p0.message}",Toast.LENGTH_SHORT).show()
                            }

                            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                                super.onCodeSent(p0, p1)
                                progressbar.visibility = View.GONE
                                getotp.visibility = View.VISIBLE
                                val intent = Intent(this@ForgetActivity, OtpVerification::class.java)
                                intent.putExtra("Mobile No",mobilenumber.text.toString())
                                intent.putExtra("BackOtp",p0)
                                startActivity(intent)
                            }

                        })

                }else{
                    Toast.makeText(this,"Please enter correct number",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
            }

            //        val intent = Intent(this@ForgetActivity,OtpVerification::class.java)
//                        intent.putExtra("Mobile No",mobilenumber.text.toString())
//                        startActivity(intent)

        }
    }
}





