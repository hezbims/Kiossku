package com.itpens.kiossku.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sumber kebenaran status validitas sesi login, dipantau oleh UI untuk
 * redirect otomatis ke halaman login ketika sesi diketahui tidak valid
 * (refresh token gagal permanen).
 *
 * State in-memory saja:
 * - Default [isSessionValid] = true. App baru dibuka dianggap valid;
 *   kalau ternyata tidak, interceptor akan mencoba refresh dan bila gagal
 *   permanen status berubah menjadi false — user tidak "dilempar" ke login
 *   sebelum ada bukti sesi invalid.
 * - [markSessionValid] dipanggil setiap token valid tersimpan (login sukses /
 *   refresh sukses), sehingga state otomatis pulih setelah user login lagi.
 */
@Singleton
class SessionManager @Inject constructor() {

    private val _isSessionValid = MutableStateFlow(DEFAULT_SESSION_VALID)

    val isSessionValid : StateFlow<Boolean> = _isSessionValid.asStateFlow()

    /**
     * Dipanggil oleh TokenRefresher ketika refresh token gagal permanen
     * (retry habis / refresh token invalid).
     */
    fun markSessionInvalid() {
        _isSessionValid.value = false
    }

    /**
     * Dipanggil setiap token valid tersimpan: login sukses atau refresh sukses.
     * StateFlow skip nilai yang sama, jadi pemanggilan berulang aman.
     */
    fun markSessionValid() {
        _isSessionValid.value = true
    }

    private companion object {
        const val DEFAULT_SESSION_VALID = true
    }
}
