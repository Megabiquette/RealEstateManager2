package com.albanfontaine.realestatemanager2.controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.albanfontaine.realestatemanager2.R
import com.albanfontaine.realestatemanager2.utils.Constants
import kotlinx.android.synthetic.main.activity_loan.*
import kotlinx.android.synthetic.main.toolbar.*

class LoanActivity : AppCompatActivity() {

	private lateinit var mPrice: TextView
	private lateinit var mContribution: EditText
	private lateinit var mRate: EditText
	private lateinit var mDuration: EditText
	private lateinit var mPayment: TextView
	private lateinit var mPaymentLayout: LinearLayout
	private lateinit var mCalculateButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_loan)

		configureToolbar()
		configureViews()

		val extras = intent.extras
		if(extras != null){
			val price = extras.getInt(Constants.PROPERTY_PRICE)
			mPrice.text = price.toString()
		}
	}

	private fun calculate(){
		if(checkFields()){
			val price = mPrice.text.toString().toDouble()
			val contribution = mContribution.text.toString().toDouble()
			val rate = mRate.text.toString().toDouble()
			val duration = mDuration.text.toString().toDouble()

			val interests = ((price - contribution) * rate) / 100
			val payment = ((price - contribution) + interests) / duration

			mPayment.text = String.format("%.2f", payment)
			mPaymentLayout.visibility = View.VISIBLE
		}else{
			Toast.makeText(applicationContext, R.string.loan_error, Toast.LENGTH_LONG).show()
		}
	}

	private fun checkFields(): Boolean{
		if(mContribution.text.toString().trim().equals("") || mRate.text.toString().trim().equals("") || mDuration.text.toString().trim().equals("")){
			return false
		}
		return true
	}

	private fun configureToolbar(){
		setSupportActionBar(toolbar as Toolbar)
		val ab : ActionBar? = getSupportActionBar()
		ab?.setDisplayHomeAsUpEnabled(true)
	}

	private fun configureViews(){
		mPrice = loan_price
		mContribution = loan_contribution
		mRate = loan_rate
		mDuration = loan_duration
		mPayment = loan_payment
		mPaymentLayout = loan_payment_layout
		mCalculateButton = loan_calculate_button
		mCalculateButton.setOnClickListener{ calculate() }
	}
}