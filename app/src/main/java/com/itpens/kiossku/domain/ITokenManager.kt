package com.itpens.kiossku.domain

import com.itpens.kiossku.data.model.auth.TokenData

interface ITokenManager {
    fun getUserId() : Int

    @Deprecated(message = "", replaceWith = ReplaceWith("getProfileToken()"))
    fun getToken() : String?

    /**
     * Mengembalikan profil token yang tersimpan di USER_PROFILE_KEY apa adanya
     * (tanpa cek kedaluwarsa), atau null bila belum pernah tersimpan.
     */
    fun getProfileToken() : TokenData?

    /**
     * Mengembalikan access token dari USER_PROFILE_KEY bila masih berlaku
     * (berdasarkan exp pada JWT, fallback 1 jam sejak disimpan),
     * atau null bila tidak ada / sudah kedaluwarsa.
     */
    fun getValidProfileToken() : String?

    @Deprecated(message = "", replaceWith = ReplaceWith("setUserProfile(token)"))
    fun setTokenSync(token : String?)

    fun setUserId(userId : Int)

    fun setUserProfile(tokenData: TokenData)
}
