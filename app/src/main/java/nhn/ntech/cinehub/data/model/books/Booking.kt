package nhn.ntech.cinehub.data.model.books

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Booking(
    val id: String = "",                    // ID duy nhất của booking
    val movieId: String = "",               // ID của phim
    val movieTitle: String = "",            // Tên phim (cho hiển thị)
    val totalPrice: Int = 0,               // Tổng tiền
    val ticketCount: Int = 0,              // Số lượng vé
    val bookingDate: String = "",          // Ngày đặt vé (dd/MM/yyyy HH:mm:ss)
    val status: String = "pending",        // Trạng thái: pending, confirmed, cancelled
    val seats: String = "",                // Danh sách ghế (A1,A2,B3...)
    val theater: String = "",              // Rạp chiếu
    val showTime: String = ""              // Suất chiếu
) {
    // Constructor mặc định cho Firebase
    constructor() : this("", "", "", 0, 0, "", "pending", "", "", "")

    companion object {
        const val STATUS_PENDING = "pending"
        const val STATUS_CONFIRMED = "confirmed"
        const val STATUS_CANCELLED = "cancelled"
    }
}