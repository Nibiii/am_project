package com.example.zad1

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Entity(tableName = "trails")
data class Trail(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var description: String,
    var difficulty: String,
    var length: Int,
    var imageUrl: String,
    var stopwatchRunning: Boolean = false,
    var stopwatchStartTime: Long = 0L,
    var elapsedTime: Long = 0L
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readLong(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(difficulty)
        parcel.writeInt(length)
        parcel.writeString(imageUrl)
        parcel.writeByte(if (stopwatchRunning) 1 else 0)
        parcel.writeLong(stopwatchStartTime)
        parcel.writeLong(elapsedTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Trail> {
        override fun createFromParcel(parcel: Parcel): Trail {
            return Trail(parcel)
        }

        override fun newArray(size: Int): Array<Trail?> {
            return arrayOfNulls(size)
        }
    }
}

class TrailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TrailRepository
    val allTrails: LiveData<List<Trail>>

    init {
        val trailDao = (application as MyApplication).database.trailDao()
        repository = TrailRepository(trailDao)
        allTrails = repository.allTrails
    }

    fun insert(trail: Trail) = viewModelScope.launch {
        repository.insert(trail)
    }

    fun update(trail: Trail) = viewModelScope.launch {
        repository.update(trail)
    }

    fun delete(trail: Trail) = viewModelScope.launch {
        repository.delete(trail)
    }

    fun getTrailById(id: Int): LiveData<Trail> {
        return repository.getTrailById(id)
    }

    suspend fun getTrailByIdSuspend(id: Int): Trail? {
        return repository.getTrailByIdSuspend(id)
    }
}

class TrailRepository(private val trailDao: TrailDao) {

    val allTrails: LiveData<List<Trail>> = trailDao.getAllTrails()

    suspend fun insert(trail: Trail) {
        trailDao.insert(trail)
    }

    suspend fun update(trail: Trail) {
        trailDao.update(trail)
    }

    suspend fun delete(trail: Trail) {
        trailDao.delete(trail)
    }

    fun getTrailById(id: Int): LiveData<Trail> {
        return trailDao.getTrailById(id)
    }

    suspend fun getTrailByIdSuspend(id: Int): Trail? {
        return trailDao.getTrailByIdSuspend(id)
    }
}

@Database(entities = [Trail::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trailDao(): TrailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trail_database"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.trailDao())
                }
            }
        }

        suspend fun populateDatabase(trailDao: TrailDao) {
            val trails = listOf(
                Trail(name = "Trail 1", description = "Description 1", difficulty = "Easy", length = 1, imageUrl = "https://img.lovepik.com/free-png/20211211/lovepik-nepal-hiking-trail-png-image_401486031_wh1200.png"),
                Trail(name = "Trail 2", description = "Description 2", difficulty = "Medium", length = 1, imageUrl = "https://thumbs.dreamstime.com/b/hiking-trail-photo-woods-taken-tilt-shift-lens-to-achieve-dreamy-look-excellent-background-design-work-54512538.jpg"),
                Trail(name = "Trail 3", description = "Description 3", difficulty = "Hard", length = 1, imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRWOPVz813BpHkOv2SmEva98TChtSHXqz0zAw&s")
            )
            trails.forEach { trailDao.insert(it) }
        }
    }
}

@Dao
interface TrailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trail: Trail)

    @Update
    suspend fun update(trail: Trail)

    @Delete
    suspend fun delete(trail: Trail)

    @Query("SELECT * FROM trails WHERE id = :id")
    fun getTrailById(id: Int): LiveData<Trail>

    @Query("SELECT * FROM trails")
    fun getAllTrails(): LiveData<List<Trail>>

    @Query("SELECT * FROM trails WHERE id = :id")
    suspend fun getTrailByIdSuspend(id: Int): Trail?
}