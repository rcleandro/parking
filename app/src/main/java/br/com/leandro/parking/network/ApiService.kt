package br.com.leandro.parking.network

import br.com.leandro.parking.model.Entrance
import br.com.leandro.parking.model.Historic
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("parking")
    fun addRegistration(
        @Field("plate") plate: String,
    ): Call<Entrance>

    @POST("parking/{plate}/pay")
    fun registerPayment(
        @Path("plate") plate: String,
    ): Call<Int>

    @POST("parking/{plate}/out")
    fun registerDeparture(
        @Path("plate") plate: String,
    ): Call<Int>

    @GET("parking/{plate}")
    fun getHistory(
        @Path("plate") plate: String,
    ): Call<List<Historic>>
}