package hr.foi.air.servicesync.business

import android.graphics.Bitmap
import android.util.Base64
import hr.foi.air.servicesync.backend.ImageUpload
import hr.foi.air.servicesync.backend.ImgurResponse
import hr.foi.air.servicesync.backend.RetrofitInstance
import retrofit2.Call
import java.io.ByteArrayOutputStream

fun encodeImageToBase64(image: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}

fun uploadImageToImgur(imageBase64: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
    val imageUpload = ImageUpload(imageBase64)
    RetrofitInstance.api.uploadImage(imageUpload).enqueue(object : retrofit2.Callback<ImgurResponse> {
        override fun onResponse(call: Call<ImgurResponse>, response: retrofit2.Response<ImgurResponse>) {
            if (response.isSuccessful) {
                val imageUrl = response.body()?.data?.link ?: ""
                onSuccess(imageUrl)
            } else {
                onFailure("Error: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<ImgurResponse>, t: Throwable) {
            onFailure("Error: ${t.message}")
        }
    })
}
