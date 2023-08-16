package co.mbznetwork.gamesforyou.di

import co.mbznetwork.gamesforyou.BuildConfig
import co.mbznetwork.gamesforyou.config.API_KEY_PARAMETER_NAME
import co.mbznetwork.gamesforyou.datasource.api.RAWGApi
import co.mbznetwork.gamesforyou.datasource.api.typeadapter.GsonSanitizedTypeAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackendModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor() = Interceptor {
        with(it) {
            proceed(
                request().run {
                    newBuilder().url(
                        url.newBuilder().addQueryParameter(
                            API_KEY_PARAMETER_NAME,
                            BuildConfig.API_KEY
                        ).build()
                    ).build()
                }
            )
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().setLevel(
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    )

    @Provides
    @Singleton
    fun provideHttpClient(
        authHeader: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor).addInterceptor(authHeader).build()

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().registerTypeAdapterFactory(GsonSanitizedTypeAdapterFactory())
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(
        httpClient: OkHttpClient,
        gson: Gson
    ) = Retrofit.Builder().baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient)
        .build()

    @Provides
    @Singleton
    fun provideRAWGApi(
        retrofit: Retrofit
    ) = retrofit.create(RAWGApi::class.java)
}