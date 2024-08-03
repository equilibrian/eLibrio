package su.elibrio.mobile.model.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import su.elibrio.mobile.model.database.repository.BookDao
import su.elibrio.mobile.model.database.repository.BookRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideBookRepository(bookDao: BookDao) : BookRepository {
        return BookRepository(bookDao)
    }
}