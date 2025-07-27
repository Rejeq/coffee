package com.rejeq.sws.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.rejeq.sws.data.source.datastore.AuthPreferences
import com.rejeq.sws.data.source.datastore.DataStoreSource
import com.rejeq.sws.data.source.datastore.DefaultAuthPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStoreSource(
        @ApplicationContext context: Context,
    ): DataStoreSource = DataStoreSource(
        dataStore = PreferenceDataStoreFactory.create(
            corruptionHandler = null,
            produceFile = { context.preferencesDataStoreFile("sws_pref") },
        ),
    )

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindsModule {
        @Binds
        @Singleton
        abstract fun bindAuthPreferences(
            pref: DefaultAuthPreferences,
        ): AuthPreferences
    }
}
