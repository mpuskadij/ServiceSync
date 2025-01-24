package hr.foi.air.servicesync.backend

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ImgurApi {
    @POST("3/image")
    fun uploadImage(@Body image: ImageUpload): Call<ImgurResponse>
}

data class ImageUpload(val image: String, val type: String = "base64")

data class ImgurResponse(val data: ImageData)
data class ImageData(val link: String)

