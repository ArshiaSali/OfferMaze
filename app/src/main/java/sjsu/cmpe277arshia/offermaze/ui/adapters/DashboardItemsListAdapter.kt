package sjsu.cmpe277arshia.offermaze.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*
import kotlinx.android.synthetic.main.item_list_layout.view.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.models.Product
import sjsu.cmpe277arshia.offermaze.ui.fragments.ProductsFragment
import sjsu.cmpe277arshia.offermaze.utils.GlideLoader



open class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_dashboard_layout,
                parent,
                false
            )
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {

            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_dashboard_item_image)

            holder.itemView.tv_dashboard_item_title.text = model.title
            holder.itemView.tv_dashboard_item_price.text = "$${model.price}"

            holder.itemView.setOnClickListener{
                if(onClickListener != null){
                    onClickListener!!.onClick(position, model)
                }
            }



            /*holder.itemView.ib_delete_product.setOnClickListener {


                fragment.deleteProduct(model.product_id)

            }*/

        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnClickListener{
        fun onClick(position: Int, product: Product)
    }
}