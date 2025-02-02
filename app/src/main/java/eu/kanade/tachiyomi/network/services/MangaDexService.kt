package eu.kanade.tachiyomi.network.services

import com.skydoves.sandwich.ApiResponse
import eu.kanade.tachiyomi.network.ProxyRetrofitQueryMap
import eu.kanade.tachiyomi.source.online.models.dto.AggregateDto
import eu.kanade.tachiyomi.source.online.models.dto.AtHomeDto
import eu.kanade.tachiyomi.source.online.models.dto.AtHomeImageReportDto
import eu.kanade.tachiyomi.source.online.models.dto.ChapterDto
import eu.kanade.tachiyomi.source.online.models.dto.ChapterListDto
import eu.kanade.tachiyomi.source.online.models.dto.LegacyIdDto
import eu.kanade.tachiyomi.source.online.models.dto.LegacyMappingDto
import eu.kanade.tachiyomi.source.online.models.dto.MangaDto
import eu.kanade.tachiyomi.source.online.models.dto.MangaListDto
import eu.kanade.tachiyomi.source.online.models.dto.ResultDto
import eu.kanade.tachiyomi.source.online.utils.MdApi
import eu.kanade.tachiyomi.source.online.utils.MdConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MangaDexService {

    @GET("${MdApi.manga}?includes[]=${MdConstants.Types.coverArt}")
    suspend fun search(@QueryMap options: ProxyRetrofitQueryMap): Response<MangaListDto>

    @GET("${MdApi.manga}/{id}?includes[]=${MdConstants.Types.coverArt}&includes[]=${MdConstants.Types.author}&includes[]=${MdConstants.Types.artist}")
    suspend fun viewManga(@Path("id") id: String): ApiResponse<MangaDto>

    @GET("${MdApi.manga}/{id}/aggregate")
    suspend fun aggregateChapters(
        @Path("id") mangaId: String,
        @Query(value = "translatedLanguage[]") translatedLanguages: List<String>,
    ): ApiResponse<AggregateDto>

    @Headers("Cache-Control: no-cache")
    @GET("${MdApi.manga}/{id}/feed?limit=500&contentRating[]=${MdConstants.ContentRating.safe}&contentRating[]=${MdConstants.ContentRating.suggestive}&contentRating[]=${MdConstants.ContentRating.erotica}&contentRating[]=${MdConstants.ContentRating.pornographic}&includes[]=${MdConstants.Types.scanlator}&order[volume]=desc&order[chapter]=desc")
    suspend fun viewChapters(
        @Path("id") id: String,
        @Query(value = "translatedLanguage[]") translatedLanguages: List<String>,
        @Query("offset") offset: Int,
    ): Response<ChapterListDto>

    @Headers("Cache-Control: no-cache")
    @GET("${MdApi.chapter}?order[publishAt]=desc")
    suspend fun latestChapters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("translatedLanguage[]") translatedLanguages: List<String>,
        @Query("contentRating[]") contentRating: List<String>,
    ): Response<ChapterListDto>

    @Headers("Cache-Control: no-cache")
    @GET("${MdApi.chapter}/{id}")
    suspend fun viewChapter(@Path("id") id: String): ApiResponse<ChapterDto>

    @Headers("Cache-Control: no-cache")
    @GET("${MdApi.manga}/random")
    suspend fun randomManga(): Response<MangaDto>

    @POST(MdApi.legacyMapping)
    suspend fun legacyMapping(@Body legacyMapping: LegacyIdDto): Response<List<LegacyMappingDto>>

    @Headers("Cache-Control: no-cache")
    @GET("${MdApi.atHomeServer}/{chapterId}")
    suspend fun getAtHomeServer(
        @Path("chapterId") chapterId: String,
        @Query("forcePort443") forcePort443: Boolean,
    ): ApiResponse<AtHomeDto>

    @POST(MdConstants.atHomeReportUrl)
    suspend fun atHomeImageReport(@Body atHomeImageReportDto: AtHomeImageReportDto): Response<ResultDto>
}
