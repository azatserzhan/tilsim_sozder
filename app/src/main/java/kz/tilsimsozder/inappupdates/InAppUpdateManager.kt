package kz.tilsimsozder.inappupdates

import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class InAppUpdateManager(private val context: Context) {

    companion object {
        const val REQUEST_CODE = 9999
    }

    private lateinit var updateManager: AppUpdateManager

    private var updateInfo: AppUpdateInfo? = null

    private val installStateListener = { state: InstallState ->
        handleInstallStatusChanged(state.installStatus())
    }

    private var state: UpdateState = UpdateState.IDLE
        set(value) {
            if (field != value) {
                field = value
                stateSubject.onNext(value)
            }
        }

    private var stateSubject = BehaviorSubject.createDefault(state)

    fun start() {
        updateManager = AppUpdateManagerFactory.create(context)
        updateManager.appUpdateInfo.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.v(task.exception, "Error updating")
                return@addOnCompleteListener
            }

            val updateInfo = task.result
            this.updateInfo = updateInfo

            when (updateInfo.updateAvailability()) {
                UpdateAvailability.UPDATE_AVAILABLE -> {
                    val hasUpdate = updateInfo.availableVersionCode() != 0
                    state = if (!hasUpdate) {
                        UpdateState.FINISHED
                    } else {
                        UpdateState.READY_FOR_DOWNLOAD
                    }
                }
                UpdateAvailability.UPDATE_NOT_AVAILABLE -> state = UpdateState.FINISHED
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> state = UpdateState.DOWNLOADING
                UpdateAvailability.UNKNOWN -> Unit
            }

            handleInstallStatusChanged(updateInfo.installStatus())
        }
    }

    /**
     * Start download
     */
    fun startUpdate(activity: Activity) {
        if (!::updateManager.isInitialized) {
            Timber.d("startUpdate(): updateManager isn't initialized")
            return
        }

        val updateInfo = updateInfo ?: run {
            Timber.d("updateInfo isn't initialized")
            return
        }
        if (state == UpdateState.READY_FOR_DOWNLOAD) {
            state = UpdateState.DOWNLOADING
            try {
                updateManager.startUpdateFlowForResult(
                    updateInfo,
                    AppUpdateType.FLEXIBLE,
                    activity,
                    REQUEST_CODE
                )
            } catch (e: Exception) {
                Timber.e(e, "Error starting update intent")
                state = UpdateState.FINISHED
            }
        }
    }

    fun observeState(): Observable<UpdateState> = stateSubject.hide()

    fun onActivityResult(resultCode: Int) {
        if (!::updateManager.isInitialized) {
            Timber.d("updateManager isn't initialized")
            return
        }

        if (resultCode == Activity.RESULT_OK) {
            updateManager.registerListener(installStateListener)
        } else if (resultCode == Activity.RESULT_CANCELED) {
            state = UpdateState.FINISHED
        }
    }

    private fun handleInstallStatusChanged(installStatus: Int) {
        if (installStatus == InstallStatus.DOWNLOADED) {
            state = UpdateState.READY_FOR_INSTALL
            if (!::updateManager.isInitialized) {
                Timber.d("updateManager isn't initialized")
                return
            }
            updateManager.unregisterListener(installStateListener)
            updateManager.completeUpdate()
        }
    }
}

enum class UpdateState {
    IDLE,
    FINISHED,
    READY_FOR_DOWNLOAD,
    DOWNLOADING,
    READY_FOR_INSTALL
}

