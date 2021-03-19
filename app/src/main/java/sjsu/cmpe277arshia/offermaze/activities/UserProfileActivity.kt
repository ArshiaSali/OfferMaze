package sjsu.cmpe277arshia.offermaze.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_user_profile.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.models.User
import sjsu.cmpe277arshia.offermaze.utils.Constants
import sjsu.cmpe277arshia.offermaze.utils.GlideLoader
import java.io.IOException

class UserProfileActivity : BaseActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var userDetails: User = User()
        if(intent.hasExtra(Constants.USER_DETAILS)){
            userDetails = intent.getParcelableExtra(Constants.USER_DETAILS)!!
        }
        et_first_name.isEnabled = false
        et_first_name.setText(userDetails.firstName)
        et_last_name.isEnabled = false
        et_last_name.setText(userDetails.lastName)
        et_email.isEnabled = false
        et_email.setText(userDetails.email)

        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_submit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_user_photo -> {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                       //showValidationError("You have Storage Permission",false)
                        Constants.showImageChooser(this)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {
                    if(validateUserProfileDetails()){
                        showValidationError("valid Details",false)
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               // showValidationError("Storage Permission granted",false)
                Constants.showImageChooser(this)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Permission Granted",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.CHOOSE_IMAGE) {
                if (data != null) {
                    try {
                        val selectedImageFileUri = data.data!!

                       // iv_user_photo.setImageURI(selectedImageFileUri)
                       GlideLoader(this@UserProfileActivity).loadUserPicture(
                            selectedImageFileUri,
                            iv_user_photo
                        )

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                           "Image Selection Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {

            TextUtils.isEmpty(et_mobile_number.text.toString().trim { it <= ' ' }) -> {
                showValidationError(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }
}