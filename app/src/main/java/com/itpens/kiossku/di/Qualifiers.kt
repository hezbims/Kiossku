package com.itpens.kiossku.di

import javax.inject.Qualifier

/**
 * Menandai [com.itpens.kiossku.data.api.AuthApi] yang dibangun tanpa
 * AuthHeaderInterceptor / TokenAuthenticator, khusus untuk endpoint
 * refresh token. Mencegah infinite loop dan circular dependency.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RefreshTokenClient
