package ps.crossworking.screen.user.form

import android.net.Uri
import ps.crossworking.common.UserInstance
import ps.crossworking.repository.IUserRepository
import javax.inject.Inject

interface IUploadImageAwsUseCase {
    suspend operator fun invoke(pictureUri: Uri?): Uri?
}

class UploadImageAwsUseCase @Inject constructor(
    val repo: IUserRepository
) : IUploadImageAwsUseCase {
    override suspend fun invoke(pictureUri: Uri?): Uri? {
        return if (pictureUri != null && pictureUri.toString() != UserInstance.user.value!!.profileImage)
            repo.uploadProfilePicture(pictureUri)
        else
            null
    }
}
