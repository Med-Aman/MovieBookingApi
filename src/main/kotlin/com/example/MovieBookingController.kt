package com.example

import com.example.models.Movie
import com.example.models.Show
import com.example.models.Theater
import com.example.models.Ticket
import com.example.repository.Repository
import io.micronaut.http.annotation.*
import javax.inject.Inject

@Controller
class MovieBookingController(@Inject val repository: Repository) {

    @Get("/say-hello/{name}")
    fun sayHello(@PathVariable name: String) = "Hello $name"

    @Get("/movie-list")
    fun getMovieList(): List<Movie> = repository.movieList

    @Get("/movie-list/{id}")
    fun getMovieNameByID(@PathVariable id: Int): Movie =
        repository.movieList.firstOrNull { it.id == id } ?: throw NoSuchElementException("No Movie Found")

    @Get("/movie/{movieId}/{theaterId}")
    fun getShowDetails(
        @PathVariable movieId: Int,
        @PathVariable theaterId: Int
    ): List<Show> = (repository.theaterList().filter { it.theaterId == theaterId })
        .flatMap { it.screen }
        .flatMap { it.showList }
        .filter { it.movieId == movieId }

    @Get("/theater")
    fun getMovieListByTheater(
        @QueryValue theatername: String
    ): List<Theater> = repository.theaterList().filter { it.name.equals(theatername, ignoreCase = true) }

    @Post("/add-movie")
    fun addMovie(
        @Body m1: Movie
    ): String {
        return "You have added ${m1.name} with ${m1.id}"
    }

    @Get("/book-ticket")
    fun getBookingDetails(
        @QueryValue movie: String,
        @QueryValue theater: String,
        @QueryValue show: Int
    ): Ticket {
        val time = repository.getShowTime(repository.getTheaterId(theater), show)
        val screen = repository.getScreenId(
            repository.getTheaterId(theater),
            repository.getMovieId(movie), show
        )
        val price = repository.getShowPrice(repository.getTheaterId(theater), show)
        if (time != "Invalid Id" && screen != 0 && price != 0) {
            return Ticket(movie, theater, time, screen, price)
        } else {
            throw NoSuchElementException()
        }
    }
}
