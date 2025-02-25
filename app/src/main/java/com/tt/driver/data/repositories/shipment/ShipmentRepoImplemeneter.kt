package com.tt.driver.data.repositories.shipment

import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.ShipmentDetailsResponse
import com.tt.driver.data.network.ShipmentServiceApi
import com.tt.driver.data.repositories.BaseRepository
import javax.inject.Inject

class ShipmentRepoImplemeneter @Inject constructor(
    private val remoteDataSource: ShipmentServiceApi
) : BaseRepository(), ShipmentRepo {
    override suspend fun getShipmentInfo(slotShipmentId : Int,hashMap: HashMap<String,Any>): RemoteResult<ShipmentDetailsResponse> =
        makeApiCall { remoteDataSource.getShipmentDetails(slotShipmentId,hashMap) }

    override suspend fun getShipmentInfoByShipmentNumber(hashMap: HashMap<String,Any>): RemoteResult<ShipmentDetailsResponse> = makeApiCall { remoteDataSource.getShipmentDetailsByShipmentNumber(hashMap) }

}