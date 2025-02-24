package com.tt.driver.data.repositories.runsheet

import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.SlotsResponse
import com.tt.driver.data.network.SlotService
import com.tt.driver.data.repositories.BaseRepository
import com.tt.driver.data.repositories.slots.SlotsRepo
import javax.inject.Inject

class SlotRepoImplemeneter @Inject constructor(
    private val remoteDataSource: SlotService
) : BaseRepository(), SlotsRepo {
    override suspend fun getSlotDependOnType(type: String, slotId: Int): RemoteResult<SlotsResponse> =
        makeApiCall { remoteDataSource. getSlotDependOnType(slotId,HashMap<String, Any>().also {
            it.put("status",type)
        }) }


}