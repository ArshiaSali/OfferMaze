package sjsu.cmpe277arshia.offermaze.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_edit_address.*
import kotlinx.android.synthetic.main.activity_address_list.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.database.FireStoreClass
import sjsu.cmpe277arshia.offermaze.models.Address
import sjsu.cmpe277arshia.offermaze.utils.Constants

class AddEditAddressActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)
        setupActionBar()

        button_submit_address.setOnClickListener { saveAddressToFirestore() }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_edit_address_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_back_button)
        }

        toolbar_add_edit_address_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateData(): Boolean {
        return when {

            TextUtils.isEmpty(edit_address_full_name.text.toString().trim { it <= ' ' }) -> {
                showValidationError(
                    resources.getString(R.string.err_msg_please_enter_full_name),
                    true
                )
                false
            }

            TextUtils.isEmpty(edit_address_phone_number.text.toString().trim { it <= ' ' }) -> {
                showValidationError(
                    resources.getString(R.string.err_msg_please_enter_phone_number),
                    true
                )
                false
            }

            TextUtils.isEmpty(edit_address_address.text.toString().trim { it <= ' ' }) -> {
                showValidationError(resources.getString(R.string.err_msg_please_enter_address), true)
                false
            }

            TextUtils.isEmpty(edit_address_zip_code.text.toString().trim { it <= ' ' }) -> {
                showValidationError(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }

            else -> {
                true
            }
        }
    }

    fun addUpdateAddressSuccess() {


        Toast.makeText(
            this@AddEditAddressActivity,
            resources.getString(R.string.err_your_address_added_successfully),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }

    private fun saveAddressToFirestore() {

        // Here we get the text from editText and trim the space
        val fullName: String = edit_address_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = edit_address_phone_number.text.toString().trim { it <= ' ' }
        val address: String = edit_address_address.text.toString().trim { it <= ' ' }
        val zipCode: String = edit_address_zip_code.text.toString().trim { it <= ' ' }
        val additionalNote: String = edit_address_additional_note.text.toString().trim { it <= ' ' }

        if (validateData()) {


            val addressType: String = when {
                rb_home_address.isChecked -> {
                    Constants.HOME
                }
                else -> {
                    Constants.OFFICE
                }
            }

            val addressModel = Address(
                FireStoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType
            )

            FireStoreClass().addAddress(this, addressModel)

        }
    }
}