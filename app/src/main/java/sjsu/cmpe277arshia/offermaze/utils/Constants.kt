package sjsu.cmpe277arshia.offermaze.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val USERS: String = "users"
    const val OFFER_MAZE_PREFERENCES: String = "OfferMazePref"
    const val LOGGED_IN_USERNAME : String = "LoggedInUser"
    const val USER_DETAILS :String = "UserDetails"
    const val CHOOSE_IMAGE = 1

    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, CHOOSE_IMAGE)
    }
}