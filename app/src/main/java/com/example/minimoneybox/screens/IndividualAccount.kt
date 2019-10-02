package com.example.minimoneybox.screens

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.minimoneybox.R
import com.example.minimoneybox.transferInfo.*
import com.example.minimoneybox.utilities.NetworkTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class IndividualAccount : AppCompatActivity() {

    var bearer : String? = null
    lateinit var transactionData: TransactionData
    lateinit var accountInfo: ArrayList<AccountInfo?>
    var activeAccount : AccountInfo? = null
    var loginInfo: LoginInfo? = null
    lateinit var networkInfo: NetworkInfo
    lateinit var multilineText : TextView
    lateinit var accountTitle : TextView
    lateinit var button : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_account)

        retrieveExtras()
        networkInfo = NetworkInfo(
            "oneoffpayments",
            "Amount=20&InvestorProductId=" + activeAccount?.id,
            bearer,
            RequestMethod.POST
        )
        setupViews()
    }

    //calls another method to create a HTTP request and then parses the data
    private fun runNetwork(networkInfo: NetworkInfo) = runBlocking {
        val networkTask = NetworkTask()
        withContext(Dispatchers.Default) { networkTask.runNetworkTask(networkInfo)}
    }

    private fun setupViews()
    {
        multilineText = findViewById(R.id.multilineText)
        accountTitle = findViewById(R.id.accountTitle)

        multilineText.text = getString(R.string.button_multiline_individual, "%.2f".format(activeAccount?.planValue), "%.2f".format(activeAccount?.moneybox))
        accountTitle.text = activeAccount?.name
        button = findViewById(R.id.button)

        button.setOnClickListener {
            runNetwork(networkInfo)
            val intent = Intent(this, UserAccounts::class.java)
            intent.putExtra("data", setupWrapperClass())
            startActivity(intent)
        }
    }

    //collate data together to be passed through as an extra
    private fun setupWrapperClass() : TransactionData
    {
        return TransactionData(loginInfo = loginInfo,
            accountInfo = ArrayList(),
            activeAccount = null,
            networkInfo = NetworkInfo(null, null, bearer, null)
        )
    }

    //retrieve data passed as an extra
    private fun retrieveExtras()
    {
        transactionData = intent.getParcelableExtra("data")
        accountInfo = transactionData.accountInfo

        if(transactionData.networkInfo != null)
        {
            bearer = transactionData.networkInfo?.bearer
        }
        if(transactionData.loginInfo != null)
        {
            loginInfo = transactionData.loginInfo
        }
        if(transactionData.activeAccount != null)
        {
            activeAccount = transactionData.activeAccount
        }
    }
}
