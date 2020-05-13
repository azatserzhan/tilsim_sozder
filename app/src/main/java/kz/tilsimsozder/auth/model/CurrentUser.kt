package kz.tilsimsozder.auth.model

data class CurrentUser(
    val id: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?
)