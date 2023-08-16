package co.mbznetwork.gamesforyou.di

import android.content.Context
import androidx.room.Room
import co.mbznetwork.gamesforyou.datasource.database.GAME_DB_NAME
import co.mbznetwork.gamesforyou.datasource.database.GameDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, GameDb::class.java, GAME_DB_NAME).build()

    @Provides
    @Singleton
    fun provideGameDao(
        gameDb: GameDb
    ) = gameDb.gameDao()

    @Provides
    @Singleton
    fun provideFavoriteGameDao(
        gameDb: GameDb
    ) = gameDb.favoriteGameDao()

    @Provides
    @Singleton
    fun provideGameRemoteKeyDao(
        gameDb: GameDb
    ) = gameDb.gameRemoteKeyDao()
}