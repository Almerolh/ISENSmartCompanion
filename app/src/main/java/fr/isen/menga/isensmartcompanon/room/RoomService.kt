package fr.isen.menga.isensmartcompanon.room

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "Question") val question: String,
    @ColumnInfo(name = "reponse") val reponse: String,
    val Date : String
)

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUsers(users: List<User>)

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM user WHERE uid = :userId")
    suspend fun getUserById(userId: Int): User

    @Query("DELETE FROM user")
    suspend fun deleteAllUsers()
}

@Database(entities = [User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Méthode pour obtenir une instance unique de la base de données
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user-database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}

/*val db = Room.databaseBuilder(
    context.applicationContext,
    AppDatabase::class.java, "database-name"
).build()

val userDao = db.userDao()
val users: List<User> = userDao.getAll()*/