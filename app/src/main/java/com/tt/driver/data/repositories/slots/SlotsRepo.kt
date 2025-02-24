package com.tt.driver.data.repositories.slots

import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.http.SlotsResponse

interface SlotsRepo {

    suspend fun getSlotDependOnType(type: String, slotId: Int): RemoteResult<SlotsResponse>


}