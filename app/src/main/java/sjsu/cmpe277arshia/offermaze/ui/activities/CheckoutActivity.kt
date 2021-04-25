package sjsu.cmpe277arshia.offermaze.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_checkout.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.models.Address
import sjsu.cmpe277arshia.offermaze.utils.Constants

class CheckoutActivity : AppCompatActivity() {

    private var globalAddressDetails : Address? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setupActionBar()
        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            globalAddressDetails = intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if(globalAddressDetails != null){
            tv_checkout_address_type.text = globalAddressDetails?.type
            tv_checkout_full_name.text = globalAddressDetails?.name
            tv_checkout_address.text = "${globalAddressDetails!!.address}, ${globalAddressDetails!!.zipCode}"
            tv_checkout_additional_note.text = globalAddressDetails?.additionalNote
            tv_checkout_mobile_number.text = globalAddressDetails?.mobileNumber
        }

    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_checkout_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_button)
        }

        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }
    }
}