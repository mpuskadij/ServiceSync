package hr.foi.air.servicesync.business

import android.content.Context
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
fun setupCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: androidx.camera.view.PreviewView,
    onCodeScanned: (String) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val barcodeScanner = BarcodeScanning.getClient()

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalyzer = ImageAnalysis.Builder().build().also { imageAnalysis ->
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    barcodeScanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                barcode.rawValue?.let { rawValue ->
                                    if (rawValue.startsWith("ServiceSync::")) {
                                        val remainingCode = rawValue.removePrefix("ServiceSync::")
                                        imageProxy.close()
                                        onCodeScanned(remainingCode)
                                        return@addOnSuccessListener
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Invalid QR Code",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                        .addOnFailureListener {
                            // Handle errors if needed
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                }
            }
        }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalyzer
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }, ContextCompat.getMainExecutor(context))
}
