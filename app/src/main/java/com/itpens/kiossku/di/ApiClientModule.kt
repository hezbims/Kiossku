package com.itpens.kiossku.di

import com.itpens.kiossku.BuildConfig
import com.itpens.kiossku.data.api.*
import com.itpens.kiossku.data.api.SharedPrefsCookieJar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiClientModule {

    private const val AUTH_BASE_URL = "${BuildConfig.KIOSSSKU_BASE_URL}auth/"
    private const val PRODUCT_BASE_URL = "${BuildConfig.KIOSSSKU_BASE_URL}product/"

    @Singleton
    @Provides
    fun provideSharedPrefsCookieJar(
        sharedPrefsCookieJar : SharedPrefsCookieJar
    ) : okhttp3.CookieJar = sharedPrefsCookieJar

    /**
     * OkHttpClient ber-autentikasi: interceptor Bearer token, authenticator
     * 401, dan shared cookie jar. Dipakai semua client Kiossku.
     */
    @Singleton
    @Provides
    fun provideAuthenticatedOkHttpClient(
        authHeaderInterceptor : AuthHeaderInterceptor,
        tokenAuthenticator : TokenAuthenticator,
        cookieJar : okhttp3.CookieJar
    ) : OkHttpClient =
        OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(authHeaderInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    /**
     * OkHttpClient polos untuk endpoint refresh token — tanpa interceptor /
     * authenticator supaya refresh tidak recursive.
     */
    @Singleton
    @Provides
    @RefreshTokenClient
    fun provideRefreshOkHttpClient(
        cookieJar : okhttp3.CookieJar
    ) : OkHttpClient =
        OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .connectTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()

    /**
     * Retrofit utama (base url Kiossku) yang dipakai semua api Kiossku
     * kecuali refresh token.
     */
    @Singleton
    @Provides
    fun provideKiosskuRetrofit(
        authenticatedOkHttpClient : OkHttpClient
    ) : Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.KIOSSSKU_BASE_URL)
            .client(authenticatedOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * Retrofit terpisah khusus refresh token, memakai client polos.
     */
    @Singleton
    @Provides
    @RefreshTokenClient
    fun provideRefreshTokenRetrofit(
        @RefreshTokenClient refreshOkHttpClient : OkHttpClient
    ) : Retrofit =
        Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .client(refreshOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideAuthApiClient(
        kiosskuRetrofit : Retrofit
    ) : AuthApi =
        kiosskuRetrofit.newBuilder()
            .baseUrl(AUTH_BASE_URL)
            .build()
            .create(AuthApi::class.java)

    /**
     * AuthApi khusus refresh token, dibangun dari retrofit polos
     * (tanpa interceptor) untuk dipakai TokenRefresher.
     */
    @Singleton
    @Provides
    @RefreshTokenClient
    fun provideRefreshTokenAuthApi(
        @RefreshTokenClient refreshTokenRetrofit : Retrofit
    ) : AuthApi =
        refreshTokenRetrofit.create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideDaerahApiClient() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.INDONESIA_REGION_BASE_URL)
            .build()
            .create<DaerahApi>()

    @Singleton
    @Provides
    fun provideGetProductApiClient(
        kiosskuRetrofit : Retrofit
    ) : GetProductApi =
        kiosskuRetrofit.newBuilder()
            .baseUrl(PRODUCT_BASE_URL)
            .build()
            .create(GetProductApi::class.java)

    @Singleton
    @Provides
    fun providePostProductApiClient(
        kiosskuRetrofit : Retrofit
    ) : PostProductApi =
        kiosskuRetrofit.newBuilder()
            .baseUrl(PRODUCT_BASE_URL)
            .build()
            .create(PostProductApi::class.java)

    @Singleton
    @Provides
    fun provideUserApiClient(
        kiosskuRetrofit : Retrofit
    ) : UserApi =
        kiosskuRetrofit.newBuilder()
            .baseUrl(AUTH_BASE_URL)
            .build()
            .create(UserApi::class.java)

    private const val DEFAULT_TIMEOUT_SECONDS = 30L
}
