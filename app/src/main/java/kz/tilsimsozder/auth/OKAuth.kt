package kz.tilsimsozder.auth

import android.content.Intent
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.tilsimsozder.auth.model.AuthType
import kz.tilsimsozder.auth.model.CurrentUser
import org.json.JSONObject
import ru.ok.android.sdk.Odnoklassniki
import ru.ok.android.sdk.OkListener
import ru.ok.android.sdk.util.OkAuthType
import ru.ok.android.sdk.util.OkScope

private const val REDIRECT_URI = "ok512000258409://authorize"

class OKAuth(private val fragment: Fragment) : SocialNetworkAuthorization {
    companion object {
        const val OK_SIGN_IN = 22890
    }

    private val odnoklassniki = Odnoklassniki.getInstance()

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun requestCode() = OK_SIGN_IN

    override fun signIn(
        onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
        onError: (message: String, type: AuthType) -> Unit
    ) {
        fragment.activity?.let {
            odnoklassniki.requestAuthorization(
                it,
                REDIRECT_URI,
                OkAuthType.ANY,
                OkScope.VALUABLE_ACCESS,
                OkScope.LONG_ACCESS_TOKEN
            )
        }
    }

    override fun onResult(requestCode: Int, resultCode: Int, data: Intent?,
                          onComplete: (currentUser: CurrentUser, type: AuthType) -> Unit,
                          onError: (message: String, type: AuthType) -> Unit
    ) {
        val authListener = AuthListener(
            onSuccess = { token ->
                scope.launch {
                    withContext(IO) {
                        val params = mapOf(
                            Pair("format", "json"),
                            Pair("scope", "GET_EMAIL")
                        )

                        odnoklassniki.request(
                            "users.getCurrentUser",
                            params,
                            "get",
                            object : OkListener {
                                override fun onSuccess(json: JSONObject) {
                                    onComplete(
                                        CurrentUser(
                                            token,
                                            "${json.getString("first_name")} ${json.getString("last_name")}",
                                            json.getString("email"),
                                            json.getString("pic_3")
                                        ),
                                        AuthType.OK)
                                }

                                override fun onError(error: String) {
                                    onError(error, AuthType.OK)
                                }
                            })
                    }
                }
            },
            onErrorAuth = { error ->
                onError(error, AuthType.OK)
            }
        )

        odnoklassniki.onAuthActivityResult(requestCode, resultCode, data, authListener)
    }
}

private class AuthListener(
    val onSuccess: (String) -> Unit,
    val onErrorAuth: (String) -> Unit
) : OkListener {
    override fun onSuccess(json: JSONObject?) {
        onSuccess(json?.getString("access_token") ?: "")
    }

    override fun onError(error: String) {
        onErrorAuth(error)
    }
}