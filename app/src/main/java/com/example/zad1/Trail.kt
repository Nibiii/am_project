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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

@Entity(tableName = "trails")
data class Trail(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var name: String,
    var description: String,
    var difficulty: String,
    var length: Int,
    var stopwatchRunning: Boolean = false,
    var stopwatchStartTime: Long = 0L
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readByte() != 0.toByte(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(difficulty)
        parcel.writeInt(length)
        parcel.writeByte(if (stopwatchRunning) 1 else 0)
        parcel.writeLong(stopwatchStartTime)
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

class TrailAdapter : RecyclerView.Adapter<TrailAdapter.TrailViewHolder>() {

    private var trails: List<Trail> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)
        return TrailViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrailViewHolder, position: Int) {
        val trail = trails[position]
        holder.bind(trail)
    }

    override fun getItemCount(): Int = trails.size

    fun setTrails(trails: List<Trail>) {
        this.trails = trails
        notifyDataSetChanged()
    }

    class TrailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trailName: TextView = itemView.findViewById(R.id.trailName)
        private val trailDescription: TextView = itemView.findViewById(R.id.trailDescription)

        fun bind(trail: Trail) {
            trailName.text = trail.name
            trailDescription.text = trail.description
        }
    }
}

class TrailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TrailRepository
    val allTrails: LiveData<List<Trail>>

    init {
        val trailDao = AppDatabase.getDatabase(application).trailDao()
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
}

@Database(entities = [Trail::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trailDao(): TrailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trail_database"
                ).build()
                INSTANCE = instance
                instance
            }
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
}