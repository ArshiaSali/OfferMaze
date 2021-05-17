package sjsu.cmpe277arshia.offermaze.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_checkout.*
import org.json.JSONObject
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.database.FireStoreClass
import sjsu.cmpe277arshia.offermaze.models.Address
import sjsu.cmpe277arshia.offermaze.models.CartItem
import sjsu.cmpe277arshia.offermaze.models.Order
import sjsu.cmpe277arshia.offermaze.models.Product
import sjsu.cmpe277arshia.offermaze.ui.adapters.CartItemsListAdapter
import sjsu.cmpe277arshia.offermaze.utils.Constants

class CheckoutActivity : BaseActivity(), PaymentResultListener {

    private var globalAddressDetails : Address? = null
    private lateinit var globalProductList: ArrayList<Product>
    private lateinit var globalCartItemsList: ArrayList<CartItem>
    private var globalSubTotal: Double = 0.0
    private var globalTotalAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setupActionBar()
        Checkout.preload(applicationContext)
        btn_place_order.setOnClickListener { startPayment() }
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

        getProductList()
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        globalProductList = productsList
        getCartItemsList()

    }
    private fun getCartItemsList() {

        FireStoreClass().getCartList(this@CheckoutActivity)
    }
    private fun getProductList() {

        FireStoreClass().getAllProductsList(this@CheckoutActivity)
    }

    fun allDetailsUpdatedSuccessfully() {

        Toast.makeText(this@CheckoutActivity, "Your order placed successfully.", Toast.LENGTH_SHORT)
                .show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        for (product in globalProductList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity
                }
            }
        }
        globalCartItemsList = cartList

        rv_cart_list_items.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, globalCartItemsList, false)
        rv_cart_list_items.adapter = cartListAdapter

        for (item in globalCartItemsList) {

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity > 0) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()

                globalSubTotal += (price * quantity)
            }
        }

        tv_checkout_sub_total.text = "$$globalSubTotal"
        // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
        tv_checkout_shipping_charge.text = "$5.0"

        if (globalSubTotal > 0) {
            ll_checkout_place_order.visibility = View.VISIBLE

            globalTotalAmount = globalSubTotal + 5.0
            tv_checkout_total_amount.text = "$$globalTotalAmount"
        } else {
            ll_checkout_place_order.visibility = View.GONE
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


    private fun placeAnOrder() {

        if(globalAddressDetails != null){
            val order = Order(
                    FireStoreClass().getCurrentUserID(),
                    globalCartItemsList,
                    globalAddressDetails!!,
                    "My order ${System.currentTimeMillis()}",
                    globalCartItemsList[0].image,
                    globalSubTotal.toString(),
                    "5.0", // The Shipping Charge is fixed as $10 for now in our case.
                    globalTotalAmount.toString()
            )
            FireStoreClass().placeOrder(this@CheckoutActivity, order)
        }


    }

    fun orderPlacedSuccess() {

       FireStoreClass().updateAllDetails(this,globalCartItemsList)
    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()
        val totalInt = globalTotalAmount * 100
        val totalString = totalInt.toString()

        try {
            val options = JSONObject()
            options.put("name","Razorpay Corp")
            //You can omit the image option to fetch the image from dashboard
            options.put("currency","USD");
            options.put("amount",totalString)//pass amount in currency subunits

            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this@CheckoutActivity, "Error", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentSuccess(p0: String?) {
        placeAnOrder()
    }
}