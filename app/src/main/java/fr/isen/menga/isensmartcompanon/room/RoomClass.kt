package fr.isen.menga.isensmartcompanon.room

import android.app.Application
import android.content.Context
import android.media.RouteListingPreference
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.TypeConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class UserRepository(context : Context) {

    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "user-database"
    ).build()

    private val userDao = db.userDao()

    suspend fun addUser(user: User) {
        userDao.insertUser(user)
    }
    suspend fun deleteUsers() {
        userDao.deleteAllUsers()
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }
}

class UserViewModel(private val repository: UserRepository)  : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users


    fun fetchUsers() {
        viewModelScope.launch {
            val userList = repository.getAllUsers()
            _users.value = userList
        }
    }

    fun addUser(users: User) {
        viewModelScope.launch {
            repository.addUser(users)
            fetchUsers() // Rafraîchit la liste après l'ajout
        }
    }
    fun deleteUsers() {
        viewModelScope.launch {
            repository.deleteUsers()
            fetchUsers() // Rafraîchit la liste après l'ajout
        }
    }
}
class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

