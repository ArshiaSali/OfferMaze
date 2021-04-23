package sjsu.cmpe277arshia.offermaze.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_settings.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.database.FireStoreClass
import sjsu.cmpe277arshia.offermaze.models.Product
import sjsu.cmpe277arshia.offermaze.utils.Constants
import sjsu.cmpe277arshia.offermaze.utils.GlideLoader

class ProductDetailsActivity : BaseActivity() {

    private var globalProductId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        setupActionBar()
        Log.i("Products_id",globalProductId)
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            globalProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        var productOwnerId : String = ""

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            productOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if(FireStoreClass().getCurrentUserID() == productOwnerId){
            btn_add_to_cart.visibility = View.GONE
        }else{
            btn_add_to_cart.visibility = View.VISIBLE
        }
        getProductDetails()
    }

    private fun getProductDetails() {

        FireStoreClass().getProductDetails(this@ProductDetailsActivity, globalProductId)
    }

    fun productDetailsSuccess(product: Product){

        // Populate the product details in the UI.
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )

        tv_product_details_title.text = product.title
        tv_product_details_price.text = "$${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_stock_quantity.text = product.stock_quantity
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