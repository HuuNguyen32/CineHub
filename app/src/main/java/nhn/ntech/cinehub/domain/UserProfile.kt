package nhn.ntech.cinehub.domain

import com.google.firebase.Timestamp

data class UserProfile(
    var uid: String? = null,
    var email: String? = null,
    var birthDay: String? = null,
    var profileName: String? = null,
    var photoUrl: String? = null,
    var createAt: Long? = null
)