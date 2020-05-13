package kz.tilsimsozder.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import kz.tilsimsozder.auth.model.AuthType
import kz.tilsimsozder.auth.model.CurrentUser

class AuthManager(fragment: Fragment) {

    private var googleAuth = GoogleAuth(fragment)

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
        onError: (message: String, type: AuthType) -> Unit
    ) {
        when (requestCode) {
            googleAuth.requestCode() ->
                googleAuth.onResult(requestCode, resultCode, data, onComplete, onError)
        }
    }

    fun signIn(
        type: AuthType,
        onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit
    ) {
        when (type) {
            AuthType.GOOGLE -> googleAuth.signIn(onComplete)
        }
    }
}