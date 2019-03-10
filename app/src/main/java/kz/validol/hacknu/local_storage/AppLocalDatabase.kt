package kz.validol.hacknu.local_storage

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Database(entities = [
    Customer::class,
    Task::class,
    History::class
], version = 1)
@TypeConverters(Converters::class)
abstract class AppLocalDatabase: RoomDatabase() {
    abstract fun customerDao() : CustomerDao
    abstract fun taskDao() : TaskDao
    abstract fun historyDao() : HistoryDao
}

@Entity(tableName = "Customer")
data class Customer (
    @PrimaryKey
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name : String,
    @SerializedName("password") var password: String,
    @SerializedName("email") var email: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("age") var age: Int,
    @SerializedName("current_points") var current_points: Int,
    @SerializedName("completed_tasks") var completed_tasks: Int,
    @SerializedName("rank") var rank: String,
    @SerializedName("levelup_points") var levelup_points: Int,
    @SerializedName("tasks")val tasks: List<Int>
) : Serializable



@Entity(tableName = "History")
data class History(
    @PrimaryKey
    var title: String,
    var data: String,
    var from_field: String,
    var to_field: String,
    var balance: Int
)


@Entity(tableName = "Task")
data class Task(
    @PrimaryKey
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("date") var date: String,
    @SerializedName("type") var expiredDate: String?,
    @SerializedName("description") var description: String,
    @SerializedName("bonus") var bonus: Int,
    @SerializedName("status") var status: String, //done, onProgress, failed, new
    @SerializedName("donePercent") var donePercent: Int
) : Serializable