package kz.tilsimsozder.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kz.tilsimsozder.R
import kz.tilsimsozder.auth.model.AuthType
import kz.tilsimsozder.auth.model.CurrentUser

class GoogleAuth(private val fragment: Fragment) : SocialNetworkAuthorization {

    companion object {
        const val GOOGLE_SIGN_IN = 9001
    }

    private var firebaseAuth: FirebaseAuth? = null

    override fun requestCode() = GOOGLE_SIGN_IN

    override fun signIn(
        onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(fragment.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(fragment.requireContext(), options)
        val signInIntent = googleSignInClient.signInIntent

        fragment.startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?,
                          onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
                          onError: (message: String, type: AuthType) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)

            firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    firebaseAuth?.currentUser?.let {
                        it.getIdToken(true).addOnCompleteListener { result ->
                            result.result
                        }
                        onComplete(CurrentUser(id = it.uid, displayName = it.displayName,
                                email = it.email, photoUrl = it.photoUrl.toString()),
                                AuthType.GOOGLE)
                    }
                } else {
                    onError("${authResult.exception} Failure signInWithCredential",
                            AuthType.GOOGLE)
                }
            }
        } catch (e: ApiException) {
            onError("$e Google sign in failed", AuthType.GOOGLE)
        }
    }
}