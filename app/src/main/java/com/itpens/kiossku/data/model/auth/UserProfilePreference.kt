package com.itpens.kiossku.data.model.auth

data class UserProfilePreference(
    val token: TokenData,
    /**
     * Waktu (epoch millis) saat profil/token disimpan.
     * Dipakai sebagai fallback expiry bila exp pada JWT tidak ditemukan.
     * Field lama (JSON tanpa field ini) akan terbaca sebagai 0L.
     */
    val savedAtMillis: Long = 0L,
)
