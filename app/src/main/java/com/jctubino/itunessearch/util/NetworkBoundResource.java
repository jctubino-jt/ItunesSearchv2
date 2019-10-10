package com.jctubino.itunessearch.util;


import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.jctubino.itunessearch.AppExecutors;
import com.jctubino.itunessearch.request.responses.ApiResponse;

import okhttp3.Cache;

// CacheObject: Type for the Resource data. (database cache)
// RequestObject: Type for the API response. (network request)
public abstract class NetworkBoundResource<CacheObject, RequestObject> {

    private static final String TAG = "NetworkBoundResource";
    private AppExecutors appExecutors;
    private MediatorLiveData<Resource<CacheObject>> result = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors){
        this.appExecutors = appExecutors;
        init();
    }

    //first view of the database cache
    private void init(){
        //update livedata for loading status
        result.setValue((Resource<CacheObject>) Resource.loading(null));

        //observe livedata source from local database
        final LiveData<CacheObject> dbSource = loadFromDb();

        //Step 1
        result.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(@Nullable CacheObject cacheObject) {

                // Remove observer from local db. Need to decide if read local db or network
                result.removeSource(dbSource);

                //Step 2 -- If condition
                //should we refresh the cache?
                // get data from network if conditions in shouldFetch(boolean) are true
                if(shouldFetch(cacheObject)){
                    //get data from network
                    fetchFromNetwork(dbSource);
                }
                else{
                    //Step 3
                    result.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject cacheObject) {
                            // Null and empty is handled in ApiResponse class
                            setValue(Resource.success(cacheObject));
                        }
                    });
                }

            }
        });
    }

    /** 1. Observe Local DB
     *  2. if (condition) query the network
     *  3. stop observing the local db
     *  4. Insert new data into local db
     *  5. Begin observing local db again to see the refreshed data from network
     */

    private void fetchFromNetwork(final LiveData<CacheObject> dbSource){
        Log.d(TAG, "fetchFromNetwork: called.");

        //update LiveData for loading status
        result.addSource(dbSource, new Observer<CacheObject>() {
            @Override
            public void onChanged(CacheObject cacheObject) {
                setValue(Resource.loading(cacheObject));
            }
        });

        final LiveData<ApiResponse<RequestObject>> apiResponse = createCall();
        result.addSource(apiResponse, new Observer<ApiResponse<RequestObject>>() {
            @Override
            public void onChanged(ApiResponse<RequestObject> RequestObjectApiResponse) {
                result.removeSource(dbSource);
                result.removeSource(apiResponse);

                /*
                    3 cases
                    1)ApiSuccessResponse
                    2)ApiErrorResponse
                    3)ApiEmptyResponse
                 */

                if(RequestObjectApiResponse instanceof ApiResponse.ApiSuccessResponse){
                    Log.d(TAG, "onChanged: ApiSuccessResponse");
                    appExecutors.diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            //save the response to the local database
                            saveCallResult((RequestObject)processResponse((ApiResponse.ApiSuccessResponse)RequestObjectApiResponse));

                            appExecutors.mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    // we specially request a new live data,
                                    // otherwise we will get immediately last cached value,
                                    // which may not be updated with latest results received from network.
                                    // as opposed to use the @dbSource variable passed as input
                                    result.addSource(loadFromDb(), new Observer<CacheObject>() {
                                        @Override
                                        public void onChanged(CacheObject cacheObject) {
                                            setValue(Resource.success(cacheObject));
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else if(RequestObjectApiResponse instanceof ApiResponse.ApiEmptyResponse){
                    Log.d(TAG, "onChanged: ApiEmptyResponse");
                    appExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            result.addSource(loadFromDb(), new Observer<CacheObject>() {
                                @Override
                                public void onChanged(CacheObject cacheObject) {
                                    setValue(Resource.success(cacheObject));
                                }
                            });
                        }
                    });
                }
                else if(RequestObjectApiResponse instanceof ApiResponse.ApiErrorResponse){
                    Log.d(TAG, "onChanged: ApiErrorResponse");
                    result.addSource(dbSource, new Observer<CacheObject>() {
                        @Override
                        public void onChanged(CacheObject CacheObject) {
                            setValue(
                                    Resource.error(
                                            ((ApiResponse.ApiErrorResponse) RequestObjectApiResponse).getErrorMessage(),
                                            CacheObject
                                    )
                            );
                        }
                    });
                }
            }
        });
    }

    //retrieving body from api response
    private CacheObject processResponse(ApiResponse.ApiSuccessResponse response){
        return (CacheObject)response.getBody();
    }

    private void setValue(Resource<CacheObject> newValue){
        if(result.getValue() != newValue){
            result.setValue(newValue);
        }
    }


    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    // Called to get the cached data from the database.
    @NonNull
    @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    // Called to create the API call.
    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<CacheObject>> getAsLiveData(){
        return result;
    };
}
