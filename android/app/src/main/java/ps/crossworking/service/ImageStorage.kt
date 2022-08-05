package ps.crossworking.service

import android.content.ContentResolver
import android.net.Uri
import com.amplifyframework.kotlin.core.Amplify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*
import javax.inject.Inject

interface IImageStorage {
    suspend fun uploadImage(imageLocalUri: Uri): Uri
}

class S3Service @Inject constructor(
    private val contentResolver: ContentResolver
) : IImageStorage {


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override suspend fun uploadImage(imageLocalUri: Uri): Uri {
        val inputStream =
            contentResolver.openInputStream(
                imageLocalUri
            )!!

        val id = UUID.randomUUID().toString() + ".jpg"
        val upload = Amplify.Storage.uploadInputStream(id, inputStream)
        upload.result()
        val url = Amplify.Storage.getUrl(id).url
        return Uri.parse("https://" + url.host + url.path)
    }
}
