package kz.tilsimsozder.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kz.tilsimsozder.auth.model.AuthType
import kz.tilsimsozder.auth.model.CurrentUser
import timber.log.Timber

class FacebookAuth(private val fragment: Fragment) : SocialNetworkAuthorization {

    companion object {
        private const val FACEBOOK_SIGN_IN = 64206
    }

    private var firebaseAuth: FirebaseAuth? = null
    private val callbackManager = CallbackManager.Factory.create()

    override fun requestCode() = FACEBOOK_SIGN_IN

    override fun signIn(
        onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
        onError: (message: String, type: AuthType) -> Unit
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
        LoginManager.getInstance().logInWithReadPermissions(fragment, mutableListOf("public_profile", "email"))

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    val credential = FacebookAuthProvider.getCredential(loginResult?.accessToken?.token ?: "")
                    firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener { authResult ->
                        if (authResult.isSuccessful) {
                            firebaseAuth?.currentUser?.let { user ->
                                user.getIdToken(true).addOnCompleteListener { result ->
                                    onComplete(CurrentUser(
                                        id = result.result?.token ?: "",
                                        displayName = user.displayName,
                                        email = user.email,
                                        photoUrl = "${user.photoUrl}?type=large"),
                                        AuthType.FACEBOOK
                                    )
                                }
                            }
                        } else {
                            onError(
                                "${authResult.exception} Failure signInWithCredential",
                                AuthType.FACEBOOK
                            )
                        }
                    }
                }

                override fun onCancel() {
                    Timber.d("Facebook sign in cancel")
                }

                override fun onError(exception: FacebookException) {
                    Timber.e(exception, "Facebook sign in error")
                }
            })
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?,
                          onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
                          onError: (message: String, type: AuthType) -> Unit
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}