package com.example.services

import com.example.models.Movie
import com.example.models.Theater
import com.example.models.Ticket
import com.example.repository.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieBookingService(@Inject val repository: Repository) {

    fun greet(name: String) = "Hello $name"

    fun getMovieList() = repository.movieList

    fun getTheater(movieId: Int) = repository.theaterList.map { theater ->
        theater.copy(
            screen = theater.screen.map { screen ->
                screen.copy(showList = screen.showList.filter { it.movieId == movieId })
            }
        )
    }

    fun getMovieDetails(id: Int) = repository.movieList.firstOrNull { it.id == id } ?: throw NoSuchElementException("No Movie Found")

    fun getShowDetails(movieId: Int, theaterId: Int) = (repository.theaterList.filter { it.theaterId == theaterId })
        .flatMap { it.screen }
        .flatMap { it.showList }
        .filter { it.movieId == movieId }

    fun getMovieListByTheater(theaterName: String) = repository.theaterList.filter { it.name.equals(theaterName, ignoreCase = true) }

    fun getBookingDetails(movie: String, theater: String, show: Int): Ticket {
        val time = repository.getShowTime(repository.getTheaterId(theater), show)
        val screen = repository.getScreenId(
            repository.getTheaterId(theater), repository.getMovieId(movie), show
        )
        val price = repository.getShowPrice(repository.getTheaterId(theater), show)
        if (time != "Invalid Id" && screen != 0 && price != 0) {
            return Ticket(movie, theater, time, screen, price)
        } else {
            throw NoSuchElementException()
        }
    }

    fun addMovie(movie: Movie): String {
        repository.movieList.add(movie)
        return "Successfully added ${movie.name}"
    }

    fun addTheater(theater: Theater): String {
        repository.theaterList.add(theater)
        return "Successfully added ${theater.name}"
    }
}
