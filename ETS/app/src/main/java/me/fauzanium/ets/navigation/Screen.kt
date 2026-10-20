package me.fauzanium.ets.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Dashboard")
    object Register : Screen("register?memberId={memberId}", "Pendaftaran") {
        fun createRoute(memberId: Int? = null): String {
            return if (memberId != null) "register?memberId=$memberId" else "register"
        }
    }
    object MemberList : Screen("member_list", "Daftar Anggota")
    object Search : Screen("search", "Pencarian Data")
    object ManageData : Screen("manage_data", "Kelola Data")
    object MemberDetail : Screen("member_detail/{memberId}", "Detail Anggota") {
        fun createRoute(memberId: Int): String = "member_detail/$memberId"
    }
}
