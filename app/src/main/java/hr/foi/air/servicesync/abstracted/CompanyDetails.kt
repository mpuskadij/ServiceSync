package hr.foi.air.servicesync.abstracted

import androidx.compose.runtime.Composable
import com.example.maps.interfaces.IMapProvider
import com.google.firebase.firestore.GeoPoint

abstract class CompanyDetails
{
    @Composable
    abstract fun GetCompanyDescription(description: String)

    @Composable
    abstract fun GetCompanyLocation(geoPoint: GeoPoint, mapProvider: IMapProvider)

    @Composable
    abstract fun GetCompanyCategory(companyCategory: String)

    @Composable
    abstract fun GetCompanyName(companyName: String)

    @Composable
    abstract fun GetCompanyImage(pictureURL: String)

    @Composable
    abstract fun GetCompanyWorkingHours(time: Int)
}