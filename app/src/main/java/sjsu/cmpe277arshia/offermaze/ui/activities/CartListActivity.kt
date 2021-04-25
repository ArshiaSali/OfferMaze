package sjsu.cmpe277arshia.offermaze.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.database.FireStoreClass
import sjsu.cmpe277arshia.offermaze.models.CartItem
import sjsu.cmpe277arshia.offermaze.models.Product
import sjsu.cmpe277arshia.offermaze.ui.adapters.CartItemsListAdapter
import sjsu.cmpe277arshia.offermaze.utils.Constants

class CartListActivity : BaseActivity() {

    private lateinit var globalProductsList : ArrayList<Product>
    private lateinit var globalCartListItems: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()

        btn_checkout.setOnClickListener {
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_button)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getCartItemsList() {

        FireStoreClass().getCartList(this@CartListActivity)
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductList()
    }

    fun itemRemovedSuccess(){
        Toast.makeText(this@CartListActivity, resources.getString(R.string.msg_item_removed_successfully),
        Toast.LENGTH_SHORT).show()

        getCartItemsList()
    }
    fun successCartItemsList(cartList: ArrayList<CartItem>) {

        for (product in globalProductsList) {
            for (cartItem in cartList) {
                if (product.product_id == cartItem.product_id) {

                    cartItem.stock_quantity = product.stock_quantity

                    if (product.stock_quantity.toInt() == 0) {
                        cartItem.cart_quantity = product.stock_quantity
                    }
                }
            }
        }

        globalCartListItems = cartList

       if(globalCartListItems.size > 0){
           rv_cart_items_list.visibility = View.VISIBLE
           ll_checkout.visibility = View.VISIBLE
           tv_no_cart_item_found.visibility = View.GONE

           rv_cart_items_list.layoutManager =  LinearLayoutManager(this@CartListActivity)
           rv_cart_items_list.setHasFixedSize(true)
           val cartListAdapter = CartItemsListAdapter(this@CartListActivity,cartList)
           rv_cart_items_list.adapter = cartListAdapter
           var subTotal: Double = 0.0

           for(item in globalCartListItems){
               val availableQuantity = item.stock_quantity.toInt()
               if(availableQuantity > 0) {
                   val price = item.price.toDouble()
                   val quantity = item.cart_quantity.toInt()
                   subTotal += (price * quantity)
               }
           }
           tv_sub_total.text = "$$subTotal"
           tv_shipping_charge.text = "$5.0"

           if(subTotal > 0){
               ll_checkout.visibility = View.VISIBLE

               val total = subTotal + 5
               tv_total_amount.text = "$$total"
           }else {
               ll_checkout.visibility = View.GONE
           }
       }else{
           rv_cart_items_list.visibility = View.GONE
           ll_checkout.visibility = View.GONE
           tv_no_cart_item_found.visibility = View.VISIBLE
       }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        globalProductsList = productsList
        getCartItemsList()

    }

    private fun getProductList() {

        FireStoreClass().getAllProductsList(this)
    }

    fun itemUpdateSuccess() {
        getCartItemsList()
    }

}