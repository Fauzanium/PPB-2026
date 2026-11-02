package me.fauzanium.tugas10loginmvvm.data.repository

import me.fauzanium.tugas10loginmvvm.data.local.dao.UserDao
import me.fauzanium.tugas10loginmvvm.data.local.entity.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun login(username: String, password: String): User? {
        return userDao.login(username, password)
    }
}
