package sjsu.cmpe277arshia.offermaze.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.database.FireStoreClass
import sjsu.cmpe277arshia.offermaze.models.CartItem
import sjsu.cmpe277arshia.offermaze.ui.adapters.CartItemsListAdapter

class CartListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)
        setupActionBar()
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
        getCartItemsList()
    }
    fun successCartItemsList(cartList: ArrayList<CartItem>) {

       if(cartList.size > 0){
           rv_cart_items_list.visibility = View.VISIBLE
           ll_checkout.visibility = View.VISIBLE
           tv_no_cart_item_found.visibility = View.GONE

           rv_cart_items_list.layoutManager =  LinearLayoutManager(this@CartListActivity)
           rv_cart_items_list.setHasFixedSize(true)
           val cartListAdapter = CartItemsListAdapter(this@CartListActivity,cartList)
           rv_cart_items_list.adapter = cartListAdapter
           var subTotal: Double = 0.0

           for(item in cartList){
               val price = item.price.toDouble()
               val quantity = item.cart_quantity.toInt()
               subTotal += (price * quantity)
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
}