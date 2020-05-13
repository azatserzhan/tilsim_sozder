package kz.tilsimsozder.auth

import android.content.Intent
import kz.tilsimsozder.auth.model.AuthType
import kz.tilsimsozder.auth.model.CurrentUser

interface SocialNetworkAuthorization {
    fun requestCode(): Int
    fun signIn(
        onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit
    )

    fun onResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
        onError: (message: String, type: AuthType) -> Unit
    )
}