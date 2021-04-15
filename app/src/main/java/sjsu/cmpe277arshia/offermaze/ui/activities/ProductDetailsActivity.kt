package sjsu.cmpe277arshia.offermaze.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_settings.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.utils.Constants

class ProductDetailsActivity : BaseActivity() {

    private var globalProductId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionBar()
        Log.i("Products_id",globalProductId)
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            globalProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product_id",globalProductId)
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_product_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_button)
        }

        toolbar_product_details_activity.setNavigationOnClickListener { onBackPressed() }
    }
}