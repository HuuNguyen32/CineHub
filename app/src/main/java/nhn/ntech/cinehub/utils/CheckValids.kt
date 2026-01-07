package nhn.ntech.cinehub.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object CheckValids {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(pass: String): Boolean {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"
        return pass.matches(passwordPattern.toRegex())
    }

    fun isValidBirthday(date: String): Boolean {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.isLenient = false
        return try {
            val parsedDate = sdf.parse(date)
            val today = Calendar.getInstance().time
            parsedDate?.before(today) ?: false
        } catch (e: Exception) {
            false
        }
    }
}