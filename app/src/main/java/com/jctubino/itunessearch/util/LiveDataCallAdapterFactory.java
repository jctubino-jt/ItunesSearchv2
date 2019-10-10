package com.jctubino.itunessearch.util;

import androidx.lifecycle.LiveData;

import com.jctubino.itunessearch.request.responses.ApiResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    /**
     * This method performs a number of checks and then return a number of response type for the Retrofit request.
     * (@bodyType is the ResponsibleType. It can be MovieResponse or MovieSearchResponse)
     *
     * Check 1) ReturnType returns LiveData
     * 2) Type LiveData<t> is of ApiResponse.class
     * 3) Make sure ApiResponse is parameterize, ApiResponse<T> exists.
     *
     */


    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        //Check #1. Make sure CallAdapter is returning a type of LiveData
        if(CallAdapter.Factory.getRawType(returnType) != LiveData.class){
            return null;
        }

        //Check #2. Type that LiveData is wrapping
        //This will look inside the return tyoe and see what type is inside that type (Getting T. What T is)
        Type observableType = CallAdapter.Factory.getParameterUpperBound(0,(ParameterizedType) returnType);

        //Check if its a type ApiEesponse
        Type rawObservableType = CallAdapter.Factory.getRawType(observableType);
        if(rawObservableType != ApiResponse.class){
            throw new IllegalArgumentException("Type must be a defined resource");
        }

        //Check 3. Check if ApiResponse is parameterized.
        //T is either MovieResponse or T will be a MovieSearchResponse

        if(!(observableType instanceof ParameterizedType)){
            throw new IllegalArgumentException("resource must be parameterized");
        }

        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0,(ParameterizedType)observableType);
        return new LiveDataCallAdapter<Type>(bodyType);
    }
}
