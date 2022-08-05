package ps.crossworking.mockRepositories

import android.net.Uri
import ps.crossworking.model.User
import ps.crossworking.repository.IUserRepository
import ps.crossworking.testUser

class UserRepositoryMock: IUserRepository {
    val mem = mutableMapOf(testUser.userId to testUser)

    var exception: Exception? = null

    fun prepareException(e: Exception) {
        exception = e
    }

    override suspend fun getUser(userId: String): User {
        if(exception != null)
            throw exception as Exception
        return mem[userId]!!
    }

    override suspend fun uploadProfilePicture(pictureLocalUri: Uri): Uri {
        if(exception != null)
            throw exception as Exception
        return Uri.parse("test")
    }
}