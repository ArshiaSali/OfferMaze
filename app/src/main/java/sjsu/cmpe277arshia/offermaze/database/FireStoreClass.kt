package sjsu.cmpe277arshia.offermaze.database

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import sjsu.cmpe277arshia.offermaze.models.Product
import sjsu.cmpe277arshia.offermaze.models.User
import sjsu.cmpe277arshia.offermaze.ui.activities.*
import sjsu.cmpe277arshia.offermaze.ui.fragments.DashboardFragment
import sjsu.cmpe277arshia.offermaze.ui.fragments.ProductsFragment
import sjsu.cmpe277arshia.offermaze.utils.Constants


class FireStoreClass {
    private val fireStoreInstance = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        fireStoreInstance.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
               // activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        fireStoreInstance.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.OFFER_MAZE_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        fireStoreInstance.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddProductActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }
            }
            .addOnFailureListener { exception ->


                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product){
        fireStoreInstance.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge()).addOnSuccessListener {
                activity.productUploadSuccess()
            }.addOnFailureListener{
                e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error",
                    e
                )
            }
    }
    fun getProductsList(fragment: Fragment) {

        fireStoreInstance.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)
                }
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }

    fun deleteProduct(fragment: ProductsFragment,productId: String){
        fireStoreInstance.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener{e ->
                Log.e("Delete Product", "Error while deleting product", e)
            }
    }
    fun getDashboardItemsList(fragment: DashboardFragment){
        fireStoreInstance.collection(Constants.PRODUCTS).get().addOnSuccessListener {
            document -> Log.i(fragment.javaClass.simpleName, document.documents.toString())
            val productsList: ArrayList<Product> = ArrayList()
            for (i in document.documents) {
                val product = i.toObject(Product::class.java)
                product!!.product_id = i.id
                productsList.add(product)
            }
            fragment.successDashboardItemsList(productsList)
        }.addOnFailureListener{e ->
            Log.e("Get Product List", "Error while getting dashboard list.", e)
        }
    }
}