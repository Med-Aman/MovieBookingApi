package com.example.controllers

import com.example.models.Movie
import com.example.models.Show
import com.example.models.Theater
import com.example.services.MovieBookingService
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.http.annotation.*
import javax.inject.Inject

@Controller
class MovieBookingController(@Inject val movieBookingService: MovieBookingService) {
    private val obj = ObjectMapper()

    @Get("/say-hello/{name}")
    fun sayHello(@PathVariable name: String) = movieBookingService.greet(name)

    @Get("/movie-list")
    fun getMovieList(): String? = obj.writeValueAsString(movieBookingService.getMovieList())

    @Get("/movie/{movieId}")
    fun getTheater(@PathVariable movieId: Int): List<Theater> = movieBookingService.getTheater(movieId)

    @Get("/movie-list/{id}")
    fun getMovieByID(@PathVariable id: Int): Movie =
        movieBookingService.getMovieDetails(id)

    @Get("/movie/{movieId}/{theaterId}")
    fun getShowDetails(
        @PathVariable movieId: Int,
        @PathVariable theaterId: Int
    ): List<Show> = movieBookingService.getShowDetails(movieId, theaterId)

    @Get("/theater")
    fun getMovieListByTheater(
        @QueryValue theatername: String
    ): List<Theater> = movieBookingService.getMovieListByTheater(theatername)

    @Get("/book-ticket")
    fun getBookingDetails(
        @QueryValue movie: String,
        @QueryValue theater: String,
        @QueryValue show: Int
    ) = movieBookingService.getBookingDetails(movie, theater, show)

    // just for post request,not serving the main purpose of the function.
    @Post("/add-movie")
    fun addMovie(
        @Body m1: Movie
    ) = movieBookingService.addMovie(m1)

    @Post("/add-theater")
    fun addTheater(
        @Body t1: Theater
    ) = movieBookingService.addTheater(t1)
}
