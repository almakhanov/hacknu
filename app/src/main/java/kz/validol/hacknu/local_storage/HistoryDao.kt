package kz.validol.hacknu.local_storage

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface HistoryDao{
    @Insert
    fun insert(item: History)

    @Query("SELECT * FROM History")
    fun get(): Flowable<List<History>>

    @Query("DELETE FROM History")
    fun delete()
}