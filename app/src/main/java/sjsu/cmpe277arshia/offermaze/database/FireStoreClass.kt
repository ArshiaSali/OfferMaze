package sjsu.cmpe277arshia.offermaze.database

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import sjsu.cmpe277arshia.offermaze.activities.RegisterActivity
import sjsu.cmpe277arshia.offermaze.models.User


class FireStoreClass {
    private val fireStoreInstance = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        fireStoreInstance.collection("users")
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
}