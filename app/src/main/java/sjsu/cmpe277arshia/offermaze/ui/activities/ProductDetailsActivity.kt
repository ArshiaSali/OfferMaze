package sjsu.cmpe277arshia.offermaze.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_settings.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.database.FireStoreClass
import sjsu.cmpe277arshia.offermaze.models.CartItem
import sjsu.cmpe277arshia.offermaze.models.Product
import sjsu.cmpe277arshia.offermaze.utils.Constants
import sjsu.cmpe277arshia.offermaze.utils.GlideLoader

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var globalProductId: String = ""
    private lateinit var globalProductDetails : Product

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

        btn_add_to_cart.setOnClickListener(this)
    }

    private fun getProductDetails() {

        FireStoreClass().getProductDetails(this@ProductDetailsActivity, globalProductId)
    }

    fun productDetailsSuccess(product: Product){

        globalProductDetails = product
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            iv_product_detail_image
        )

        tv_product_details_title.text = product.title
        tv_product_details_price.text = "$${product.price}"
        tv_product_details_description.text = product.description
        tv_product_details_stock_quantity.text = product.stock_quantity
    }

    fun addToCartSuccess() {
        Toast.makeText(
            this@ProductDetailsActivity,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT
        ).show()

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

    private fun addToCart(){
        val addToCart = CartItem(
            FireStoreClass().getCurrentUserID(),
            globalProductId,
            globalProductDetails.title,
            globalProductDetails.price,
            globalProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        FireStoreClass().addCartItems(this@ProductDetailsActivity, addToCart)
    }
    override fun onClick(v: View?) {

        if(v != null){
            when(v.id){
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
            }
        }
    }
}