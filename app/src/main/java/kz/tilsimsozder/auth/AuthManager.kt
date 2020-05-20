package kz.tilsimsozder.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import kz.tilsimsozder.auth.model.AuthType
import kz.tilsimsozder.auth.model.CurrentUser

class AuthManager(
    fragment: Fragment,
    private val onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
    private val onError: (message: String, type: AuthType) -> Unit
) {

    private var googleAuth = GoogleAuth(fragment)
    private var facebookAuth = FacebookAuth(fragment)
    private var vkAuth = VKAuth(fragment)
    private var okAuth = OKAuth(fragment)

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        when(requestCode) {
            googleAuth.requestCode() ->
                googleAuth.onResult(requestCode, resultCode, data, onComplete, onError)
            facebookAuth.requestCode() ->
                facebookAuth.onResult(requestCode, resultCode, data, onComplete, onError)
            vkAuth.requestCode() ->
                vkAuth.onResult(requestCode, resultCode, data, onComplete, onError)
            okAuth.requestCode() ->
                okAuth.onResult(requestCode, resultCode, data, onComplete, onError)
        }
    }

    fun signIn(
        type: AuthType
    ) {
        when (type) {
            AuthType.GOOGLE -> googleAuth.signIn(onComplete, onError)
            AuthType.FACEBOOK -> facebookAuth.signIn(onComplete, onError)
            AuthType.VK -> vkAuth.signIn(onComplete, onError)
            AuthType.OK -> okAuth.signIn(onComplete, onError)
        }
    }
}