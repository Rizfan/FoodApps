package com.rizfan.foodapps.di

import com.rizfan.foodapps.data.FoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMakananRepository(): FoodRepository {
        return FoodRepository()
    }
}