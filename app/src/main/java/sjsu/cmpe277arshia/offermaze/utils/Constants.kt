package sjsu.cmpe277arshia.offermaze.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val USERS: String = "users"
    const val OFFER_MAZE_PREFERENCES: String = "OfferMazePref"
    const val LOGGED_IN_USERNAME : String = "LoggedInUser"
    const val USER_DETAILS :String = "UserDetails"
    const val CHOOSE_IMAGE = 1
    const val MALE: String = "Male"
    const val FEMALE: String = "Female"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE :String = "image"
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val COMPLETE_PROFILE: String = "profileCompleted"
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, CHOOSE_IMAGE)
    }
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}