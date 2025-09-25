package com.idimi.garage.sysflows

import android.util.Log
import com.idimi.garage.GarageApp
import com.idimi.garage.datamodel.model.Place
import com.idimi.garage.repo.GarageRepo
import com.idimi.garage.util.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.yield

fun collectPlacesAsFlow(
    garageRepo: GarageRepo,
) : Flow<Resource<List<Place>>> {
    return cacheBoundResource(
        fetchFromLocal = {
            garageRepo.db.getPlaceDao().getAllPlacesAsFlow()
        },
        shouldFetchFromRemote = {
            isNetworkAvailable(GarageApp.getAppContext()) && it.isNullOrEmpty()
        },
        fetchFromRemote = {
            yield()
            flowOf(ApiResponse.create(garageRepo.garageAPI.getPois()))
        },
        processRemoteResponse = { data, responseString ->
            val places = mutableListOf<Place>()
            data?.let { places.addAll(it) }
            if (responseString.body != null) {
                val placesJSONArray = responseString.body.getAsJsonArray("pois")
                places.addAll(garageRepo.parsePoisAndPersistItIntoDatabase(placesJSONArray))
            }
            places
        },
    ).map {
        when (it.status) {
            Resource.Status.SUCCESS -> Resource.success(it.data)
            Resource.Status.ERROR -> Resource.error("No Data", emptyList())
            Resource.Status.LOADING -> Resource.loading(emptyList())
            Resource.Status.LOADING_FROM_SERVER -> Resource.loadingFromServer(emptyList())
            Resource.Status.PROCESSING_REMOTE_DATA -> Resource.loadingFromServer(emptyList())
            Resource.Status.PROCESSING_INVALID_DB_DATA -> Resource.loadingFromServer(emptyList())
            Resource.Status.INVALID_DB_DATA_MADE_VALID -> Resource.loadingFromServer(emptyList())
            Resource.Status.IRRELEVANT -> Resource.loadingFromServer(emptyList())
        }
    }.flowOn(Dispatchers.IO).filter { it.data != null }.retry(3) {
        return@retry true
    }.catch {
        emit(Resource(Resource.Status.ERROR, listOf(), "exception..." + it.message))
    }
}