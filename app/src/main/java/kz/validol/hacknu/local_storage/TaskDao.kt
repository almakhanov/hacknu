package kz.validol.hacknu.local_storage

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface TaskDao{
    @Insert
    fun insert(item: Task)

    @Query("SELECT * FROM Task")
    fun get(): Flowable<List<Task>>

    @Query("DELETE FROM Task")
    fun delete()
}