package ps.crossworking.screen.user.form

import android.net.Uri
import ps.crossworking.model.User
import ps.crossworking.repository.IAuthRepository
import javax.inject.Inject

interface IAddNameAndPicUriUseCase {
    suspend operator fun invoke(name: String, about: String, pictureUri: Uri?): User
}

class AddNameAndPicUriUseCase @Inject constructor(
    val repo: IAuthRepository
) : IAddNameAndPicUriUseCase {
    override suspend fun invoke(name: String, about: String, pictureUri: Uri?): User {
        return repo.updateOwnUser(name, about, pictureUri)
    }
}
