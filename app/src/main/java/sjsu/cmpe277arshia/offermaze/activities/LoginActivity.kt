package sjsu.cmpe277arshia.offermaze.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import sjsu.cmpe277arshia.offermaze.R
import sjsu.cmpe277arshia.offermaze.database.FireStoreClass
import sjsu.cmpe277arshia.offermaze.models.User
import sjsu.cmpe277arshia.offermaze.utils.Constants

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        btn_login.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }

    fun userLoggedInSuccess(user: User) {

        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.USER_DETAILS,user)
            startActivity(intent)
        } else {

            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }
        finish()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.btn_login -> {
                    logInRegisteredUser()
                }

                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_email_login.text.toString().trim { it <= ' ' }) -> {
                showValidationError(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password_login.text.toString().trim { it <= ' ' }) -> {
                showValidationError(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            // Get the text from editText and trim the space
            val email = et_email_login.text.toString().trim { it <= ' ' }
            val password = et_password_login.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                      FireStoreClass().getUserDetails(this@LoginActivity)
                    } else {
                        showValidationError(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
}