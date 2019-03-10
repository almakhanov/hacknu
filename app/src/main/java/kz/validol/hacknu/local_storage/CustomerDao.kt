package kz.validol.hacknu.local_storage

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface CustomerDao{
    @Insert
    fun insert(consumer: Customer)

    @Query("SELECT * FROM Customer")
    fun get(): Flowable<List<Customer>>

    @Query("DELETE FROM Customer")
    fun delete()
}