package com.example.minimoneybox.screens

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.minimoneybox.utilities.NetworkTask
import com.example.minimoneybox.R
import com.example.minimoneybox.transferInfo.*
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UserAccounts : AppCompatActivity() {

    lateinit var nameView : TextView
    lateinit var planValueView : TextView
    private var totalPlanValue : Double = 0.0
    lateinit var  transactionData: TransactionData
    lateinit var accountInfo: ArrayList<AccountInfo?>
    var activeAccount : AccountInfo? = null
    var loginInfo: LoginInfo? = null
    var bearer : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_accounts)

        retrieveExtras()
        var networkInfo = NetworkInfo(
            "investorproducts",
            null, bearer, RequestMethod.GET
        )
        runNetwork(networkInfo)
        setupViews()
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
    }

    //calls another method to create a HTTP request and then parses the data
    private fun runNetwork(networkInfo: NetworkInfo) = runBlocking {
        val networkTask = NetworkTask()
        val result = withContext(Dispatchers.Default) { networkTask.runNetworkTask(networkInfo)}
        val gson = GsonBuilder().create()
        val accounts = gson.fromJson(result, Info::class.java)

        totalPlanValue = accounts.TotalPlanValue
        for(i in accounts.ProductResponses)
        {
            var individualAcc = AccountInfo(i.Id, i.PlanValue, i.Moneybox, i.Product.FriendlyName)
            accountInfo.add(individualAcc)
        }
    }

    private fun setupViews() {
        nameView = findViewById(R.id.name)
        planValueView = findViewById(R.id.planValue)

        if(loginInfo?.name != "")
        {
            nameView.visibility = View.VISIBLE
            nameView.text = getString(R.string.name, loginInfo?.name)
        }

        planValueView.text = getString(R.string.plan_value, totalPlanValue.toString())

        val layout = findViewById<LinearLayout>(R.id.linearLayout)

        var buttons = ArrayList<Button>()
        for (i in accountInfo) {

            if(i != null) {
                val btnTag = Button(this)
                btnTag.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                btnTag.text = getString(R.string.button_multiline, i.name, "%.2f".format(i.planValue), "%.2f".format(i.moneybox))
                btnTag.background = getDrawable(R.drawable.background_button_colored_rounded_medium)
                btnTag.setTextColor(ContextCompat.getColor(this, R.color.white))
                btnTag.id = i.id
                buttons.add(btnTag)
                layout.addView(btnTag)
            }
        }
        for(i in buttons)
        {
            i.setOnClickListener{
                for(j in accountInfo)
                {
                    if(i.id == j?.id)
                    {
                        activeAccount = j
                    }
                }
                val intent = Intent(this, IndividualAccount::class.java)
                intent.putExtra("data", setupWrapperClass())
                startActivity(intent)
            }
        }
    }

    //collate data together to be passed through as an extra
    private fun setupWrapperClass() : TransactionData
    {
        return TransactionData(loginInfo = loginInfo,
            accountInfo = accountInfo,
            activeAccount = activeAccount,
            networkInfo = NetworkInfo(null, null, bearer, null)
            )
    }

    //used to parse gson
    class Info (val ProductResponses : List<Accounts>, val TotalPlanValue : Double)
    class Accounts (val Id: Int, val PlanValue : Double, val Moneybox : Double, val Product: Product)
    class Product(val FriendlyName : String)
}
