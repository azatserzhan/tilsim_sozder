package kz.tilsimsozder.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import com.vk.sdk.api.model.VKApiUser
import com.vk.sdk.api.model.VKList
import kz.tilsimsozder.auth.model.AuthType
import kz.tilsimsozder.auth.model.CurrentUser

class VKAuth(private val fragment: Fragment) : SocialNetworkAuthorization {

    companion object {
        const val VK_SIGN_IN = 10485
    }

    override fun requestCode() = VK_SIGN_IN

    override fun signIn(
        onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
        onError: (message: String, type: AuthType) -> Unit
    ) {
        VKSdk.login(fragment.requireActivity(), "email")
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?,
                          onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
                          onErrorVK: (message: String, type: AuthType) -> Unit
    ) {
        VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken?> {
            override fun onResult(res: VKAccessToken?) {
                VKApi.users().get().executeWithListener(object : VKRequest.VKRequestListener() {
                    override fun onComplete(response: VKResponse) {
                        val user = (response.parsedModel as VKList<VKApiUser>).first()
                        onComplete(CurrentUser(
                            res?.accessToken ?: "",
                            "${user.first_name} ${user.last_name}",
                            res?.email,
                            user.photo_200_orig
                        ), AuthType.VK)
                    }
                })
            }

            override fun onError(error: VKError) {
                onErrorVK("Error to sign in with vk: ${error.errorMessage}", AuthType.VK)
            }
        })
    }
}