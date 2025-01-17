package hr.foi.air.servicesync.business

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import hr.foi.air.servicesync.backend.ImageUpload
import hr.foi.air.servicesync.backend.ImgurResponse
import hr.foi.air.servicesync.backend.RetrofitInstance
import retrofit2.Call
import java.io.ByteArrayOutputStream

class ImageProcessor(
    private val context: Context
) {
    fun processImage(uri: Uri, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        try {
            val mimeType = context.contentResolver.getType(uri)
            val bitmap = if (mimeType == "image/heic" || mimeType == "image/heif") {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            val resizedImage = resizeBitmap(bitmap, maxWidth = 1024, maxHeight = 1024)
            val encodedImage = encodeImageToBase64(resizedImage)

            uploadImageToImgur(encodedImage, onSuccess, onFailure)
        } catch (e: Exception) {
            onFailure("Error processing image: ${e.localizedMessage}")
        }
    }
    private fun resizeBitmap(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val aspectRatio = image.width.toFloat() / image.height
        val width = if (aspectRatio > 1) maxWidth else (maxHeight * aspectRatio).toInt()
        val height = if (aspectRatio > 1) (maxWidth / aspectRatio).toInt() else maxHeight
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
    private fun encodeImageToBase64(image: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
    private fun uploadImageToImgur(
        encodedImage: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val imageUpload = ImageUpload(encodedImage)
        RetrofitInstance.api.uploadImage(imageUpload).enqueue(object : retrofit2.Callback<ImgurResponse> {
            override fun onResponse(call: Call<ImgurResponse>, response: retrofit2.Response<ImgurResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val imageUrl = response.body()!!.data.link
                    onSuccess(imageUrl)
                } else {
                    onFailure("Error: ${response.message()} or empty body")
                }
            }

            override fun onFailure(call: Call<ImgurResponse>, t: Throwable) {
                onFailure("Error: ${t.message}")
            }
        })
    }
}
