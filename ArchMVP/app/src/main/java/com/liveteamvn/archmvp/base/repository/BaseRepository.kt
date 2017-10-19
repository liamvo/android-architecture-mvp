package com.liveteamvn.archmvp.base.repository

import android.arch.persistence.room.RoomDatabase

/**
 * Created by liam on 11/10/2017.
 */

abstract class BaseRepository<out DB : RoomDatabase, out DA, out S>(database: DB, service: S) : IRepository {
    val dao: DA by lazy {
        provideDao()
    }
    val db: DB = database
    val sv: S = service

    abstract fun provideDao(): DA
}