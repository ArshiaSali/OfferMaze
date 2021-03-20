package sjsu.cmpe277arshia.offermaze.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_products.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.database.FireStoreClass
import sjsu.cmpe277arshia.offermaze.models.Product
import sjsu.cmpe277arshia.offermaze.ui.activities.AddProductActivity
import sjsu.cmpe277arshia.offermaze.ui.adapters.MyProductsListAdapter

class ProductsFragment : BaseFragment() {

    //private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
       if(productsList.size > 0){
           rv_my_product_items.visibility = View.VISIBLE
           tv_no_products_found.visibility = View.GONE
           rv_my_product_items.layoutManager = LinearLayoutManager(activity)
           rv_my_product_items.setHasFixedSize(true)
           val adapterProducts = MyProductsListAdapter(requireActivity(),productsList,this@ProductsFragment)
           rv_my_product_items.adapter = adapterProducts
       }
        else{
           rv_my_product_items.visibility = View.GONE
           tv_no_products_found.visibility = View.VISIBLE
       }
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    private fun getProductListFromFireStore() {
        // Call the function of Firestore class.
        FireStoreClass().getProductsList(this@ProductsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_products, container, false)
        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}