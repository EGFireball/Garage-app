package com.idimi.garage.sysflows

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import retrofit2.Response

enum class ResourceSource{
    RS_UNKNOWN, RS_LOCAL, RS_REMOTE
}
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val resourceSource:ResourceSource = ResourceSource.RS_UNKNOWN
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        LOADING_FROM_SERVER,
        PROCESSING_REMOTE_DATA,
        PROCESSING_INVALID_DB_DATA,
        INVALID_DB_DATA_MADE_VALID,
        IRRELEVANT
    }

    companion object {
        fun <T> success(data: T?, resourceSource: ResourceSource = ResourceSource.RS_UNKNOWN): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null,
                resourceSource
            )
        }

        fun <T> error(msg: String, data: T? = null, resourceSource: ResourceSource = ResourceSource.RS_UNKNOWN): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                msg,
                resourceSource
            )
        }

        fun <T> loading(data: T? = null, resourceSource: ResourceSource = ResourceSource.RS_UNKNOWN): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null,
                resourceSource
            )
        }

        fun <T> loadingFromServer(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING_FROM_SERVER,
                data,
                null,
                resourceSource = ResourceSource.RS_REMOTE
            )
        }

        fun <T> processing(data: T? = null, resourceSource: ResourceSource = ResourceSource.RS_UNKNOWN): Resource<T> {
            return Resource(
                Status.PROCESSING_REMOTE_DATA,
                data,
                null,
                resourceSource
            )
        }

        fun <T> processingInvalidDbData(data: T? = null): Resource<T> {
            return Resource(
                Status.PROCESSING_INVALID_DB_DATA,
                data,
                null,
                resourceSource = ResourceSource.RS_LOCAL
            )
        }

        fun <T> processingInvalidData(data: T? = null): Resource<T> {
            return Resource(
                Status.PROCESSING_REMOTE_DATA,
                data,
                null,
                resourceSource = ResourceSource.RS_REMOTE
            )
        }

        fun <T> invalidDbDataMadeValid(data: T?): Resource<T> {
            return Resource(
                Status.INVALID_DB_DATA_MADE_VALID,
                data,
                null
            )
        }

        fun <T> irrelevant(data: T? = null, resourceSource: ResourceSource = ResourceSource.RS_UNKNOWN): Resource<T> {
            return Resource(
                Status.IRRELEVANT,
                data,
                null,
                resourceSource
            )
        }

    }
}

// Wrapper around Retrofit Response
sealed class ApiResponse<T> {
    companion object {

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(
                error.message ?: "Unknown error",
                0
            )
        }


        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                val headers = response.headers()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                        body,
                        headers
                    )
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(
                    errorMsg ?: "Unknown error",
                    response.code()
                )
            }
        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
    val body: T?,
    val headers: okhttp3.Headers?
) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String, val statusCode: Int) : ApiResponse<T>()

// Template method for accessing and updating cached resource
inline fun <DB, REMOTE> cacheBoundResource(
    crossinline fetchFromLocal: () -> Flow<DB>,
    crossinline shouldFetchFromRemote: (DB?) -> Boolean = { true },
    crossinline fetchFromRemote: suspend (DB?) -> Flow<ApiResponse<REMOTE>>?,
    crossinline processRemoteResponse: suspend (DB?, response: ApiSuccessResponse<REMOTE>) -> DB?,
    crossinline saveRemoteData: (DB?) -> Unit = { },
    crossinline onFetchFailed: (errorBody: String?, statusCode: Int) -> Unit = { _: String?, _: Int -> }
) = flow<Resource<DB>> {

    emit(Resource.loading(null))

    val localData = fetchFromLocal().first()

    if (shouldFetchFromRemote(localData)) {
        emit(Resource.loadingFromServer())

        fetchFromRemote(localData)!!.collect { apiResponse ->

            when (apiResponse) {
                is ApiSuccessResponse -> {
                    emit(Resource.processing())
                    val processedResultBody = processRemoteResponse(localData, apiResponse)
                    processedResultBody?.let {  saveRemoteData(it) }
                    emitAll(fetchFromLocal().map { dbData ->
                        Resource.success(dbData, resourceSource = ResourceSource.RS_REMOTE)
                    })
                }

                is ApiErrorResponse -> {
                    onFetchFailed(apiResponse.errorMessage, apiResponse.statusCode)
                    emitAll(fetchFromLocal().map {
                        Resource.error(
                            apiResponse.errorMessage,
                            it
                        )
                    })
                }

                is ApiEmptyResponse -> {
                    emitAll(flowOf(localData).map { Resource.error("ApiEmptyResponse arrived...") })
                }

            }
        }
    } else {
        if( localData != null ) {
            emitAll(flowOf(localData).map { Resource.success(it
                , resourceSource = ResourceSource.RS_LOCAL) })
        }else{
            emitAll(flowOf(localData).map { Resource.error("could not fetch data"
                , resourceSource = ResourceSource.RS_LOCAL) })
        }
    }
}