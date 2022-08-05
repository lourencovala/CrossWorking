package ps.crossworking.repository

import android.net.Uri
import ps.crossworking.exceptions.*
import ps.crossworking.model.User
import ps.crossworking.service.IImageStorage
import ps.crossworking.service.UserService
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

interface IUserRepository {
    suspend fun getUser(userId: String): User
    suspend fun uploadProfilePicture(pictureLocalUri: Uri): Uri
}

class UserRepository @Inject constructor(
    private val service: UserService,
    private val serviceImage: IImageStorage
) : IUserRepository {

    override suspend fun getUser(userId: String): User {
        try {
            return service.getUser(userId)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw UserNotFoundException()
                        in 400..499 -> throw RequestApiException()
                        in 500..599 -> throw ApiInternalException()
                        else -> throw UnknownException()
                    }
                }

                is UnknownHostException -> throw NetworkException()

                else -> throw UnknownException()
            }
        }
    }

    override suspend fun uploadProfilePicture(pictureLocalUri: Uri): Uri {
        try {
            return serviceImage.uploadImage(pictureLocalUri)
        } catch (e: Exception) {
            throw UploadingPhotoException()
        }
    }
}
