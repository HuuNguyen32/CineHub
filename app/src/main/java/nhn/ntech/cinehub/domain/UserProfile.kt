package nhn.ntech.cinehub.domain

data class UserProfile(
    val uid: String,
    val email: String,
    val birthDay: String,
    val profileName: String,
    val photoUrl: String?
)