package com.example.minimoneybox.screens

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.minimoneybox.*
import java.util.regex.Pattern
import com.example.minimoneybox.transferInfo.LoginInfo
import com.example.minimoneybox.transferInfo.NetworkInfo
import com.example.minimoneybox.transferInfo.RequestMethod
import com.example.minimoneybox.transferInfo.TransactionData
import com.example.minimoneybox.utilities.NetworkTask
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {

    lateinit var btn_sign_in : Button
    lateinit var til_email : TextInputLayout
    lateinit var et_email : EditText
    lateinit var til_password : TextInputLayout
    lateinit var et_password : EditText
    lateinit var til_name : TextInputLayout
    lateinit var et_name : EditText
    lateinit var pigAnimation : LottieAnimationView
    lateinit var bearer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupViews()

    }

    override fun onStart() {
        super.onStart()
        setupAnimation()
    }

    private fun setupViews() {
        btn_sign_in = findViewById(R.id.btn_sign_in)
        til_email = findViewById(R.id.til_email)
        et_email = findViewById(R.id.et_email)
        til_password = findViewById(R.id.til_password)
        et_password = findViewById(R.id.et_password)
        til_name = findViewById(R.id.til_name)
        et_name = findViewById(R.id.et_name)
        pigAnimation = findViewById(R.id.animation)


        btn_sign_in.setOnClickListener {
            if (validation()) {
                var networkInfo = NetworkInfo(
                    "users/login",
                    "Email=androidtest%40moneyboxapp.com&Password=P455word12", null,
                    RequestMethod.POST
                )

                runNetwork(networkInfo)

                if(bearer.isNotEmpty()) {
                    val intent = Intent(this, UserAccounts::class.java)
                    intent.putExtra("data", setupWrapperClass())
                    startActivity(intent)
                }
            }
            else {
                Toast.makeText(this, R.string.input_invalid, Toast.LENGTH_LONG).show()
            }
        }
    }

    //calls another method to create a HTTP request and then parses the data
    private fun runNetwork(networkInfo: NetworkInfo) = runBlocking {
        val networkTask = NetworkTask()
        val result = withContext(Dispatchers.Default) { networkTask.runNetworkTask(networkInfo)}
        val gson = GsonBuilder().create()
        val info = gson.fromJson(result, Info::class.java)
        bearer = info.Session.BearerToken
    }

    //collate data together to be passed through as an extra
    private fun setupWrapperClass() : TransactionData
    {
        lateinit var loginInfo: LoginInfo

        if(et_name != null) //fix this
        {
            loginInfo =
                LoginInfo(
                    et_name.text.toString(),
                    et_password.text.toString(),
                    et_email.text.toString()
                )
        }
        else
        {
            loginInfo =
                LoginInfo(
                    "",
                    et_password.text.toString(),
                    et_email.text.toString()
                )
        }
        return TransactionData(
            accountInfo = ArrayList(),
            loginInfo = loginInfo,
            networkInfo = NetworkInfo(null, null, bearer, null),
            activeAccount = null
        )
    }


    private fun validation() : Boolean {
        var emailValid = false
        var passwordValid = false

        if (Pattern.matches(EMAIL_REGEX, et_email.text.toString())) {
            emailValid = true
        } else {
            til_email.error = getString(R.string.email_address_error)
        }

        if (Pattern.matches(PASSWORD_REGEX, et_password.text.toString())) {
            passwordValid = true
        } else {
            til_password.error = getString(R.string.password_error)
        }

        if (et_name.text.toString() != "")
        {
            if(!Pattern.matches(NAME_REGEX, et_name.text.toString())) {
                til_name.error = getString(R.string.full_name_error)
                return false
            }
        }
        return emailValid && passwordValid
    }

    private fun setupAnimation() {

        pigAnimation.setMinFrame(firstAnim.first)
        pigAnimation.setMaxFrame(firstAnim.second)
        pigAnimation.playAnimation()

        pigAnimation.addAnimatorUpdateListener { animation ->

            if(animation.animatedFraction == 1f)
            {
                pigAnimation.setMinFrame(secondAnim.first)
                pigAnimation.setMaxFrame(secondAnim.second)
                pigAnimation.repeatCount = LottieDrawable.INFINITE
            }
        }
    }

    companion object {
        val EMAIL_REGEX = "[^@]+@[^.]+\\..+"
        val NAME_REGEX = "[a-zA-Z]{6,30}"
        val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[A-Z]).{10,50}$"
        val firstAnim = 0 to 109
        val secondAnim = 131 to 158
    }

    //used to parse gson
    class Info (val Session : Session)
    class Session (val BearerToken : String)
}
