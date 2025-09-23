package com.idimi.garage.api.pojo

import com.google.gson.annotations.SerializedName

data class PoisPojo(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("rating") var rating: Double? = null,
    @SerializedName("large_image_url") var largeImageUrl: String? = null,
    @SerializedName("combined_rating_avg") var combinedRatingAvg: String? = null,
    @SerializedName("loc") var loc: ArrayList<Double> = arrayListOf(),
    @SerializedName("primary_category") var primaryCategory: String? = null,
    @SerializedName("primary_category_display_name") var primaryCategoryDisplayName: String? = null,
    @SerializedName("category_group") var categoryGroup: String? = null,
    @SerializedName("tags") var tags: ArrayList<String> = arrayListOf(),
    @SerializedName("v_145x145_url") var v145x145Url: String? = null,
    @SerializedName("v_320x320_url") var v320x320Url: String? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("booking_price") var bookingPrice: String? = null,
    @SerializedName("uber_rating") var uberRating: Int? = null,
    @SerializedName("combined_avg_rating") var combinedAvgRating: Double? = null,
    @SerializedName("engagement_score") var engagementScore: String? = null,
    @SerializedName("booking_url") var bookingUrl: String? = null,
    @SerializedName("reviews_count") var reviewsCount: Int? = null,
    @SerializedName("open_now") var openNow: Boolean? = null,
    @SerializedName("bookable") var bookable: Boolean? = null,
    @SerializedName("is_chain") var isChain: Boolean? = null,
    @SerializedName("tpc_id") var tpcId: String? = null,
    @SerializedName("allows_pets") var allowsPets: Boolean? = null,
    @SerializedName("searchable_price") var searchablePrice: String? = null,
    @SerializedName("booking_providers") var bookingProviders: ArrayList<String> = arrayListOf(),
    @SerializedName("travel_rank") var travelRank: String? = null,
    @SerializedName("branding") var branding: String? = null,
    @SerializedName("segments") var segments: ArrayList<String> = arrayListOf()
) {
}