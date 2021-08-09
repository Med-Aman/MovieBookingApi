package com.example.repository

import com.example.models.Movie
import com.example.models.Screen
import com.example.models.Show
import com.example.models.Theater
import javax.inject.Singleton

@Singleton
class Repository {
    val movieList: List<Movie> = listOf(
        Movie(1, "Avengers"),
        Movie(2, "Thor"),
        Movie(3, "Dhoom"),
        Movie(4, "Bahubali"),
        Movie(5, "Superman"),
    )

    fun theaterList() = listOf(
        Theater(
            theaterId = 1, name = "Inox",
            screen = listOf(
                Screen(
                    id = 1,
                    showList = listOf(
                        Show(1, "9 AM", movieId = 1, 160),
                        Show(2, "12 PM", movieId = 2, 200),
                        Show(3, "3 PM", movieId = 3, 240)
                    )
                ),
                Screen(
                    id = 2,
                    showList = listOf(
                        Show(1, "10 AM", movieId = 4, 160),
                        Show(2, "1 PM", movieId = 5, 200),
                        Show(3, "4 PM", movieId = 1, 240)
                    )
                )
            )
        ),
        Theater(
            theaterId = 2, name = "PVR",
            screen = listOf(
                Screen(
                    id = 1,
                    showList = listOf(
                        Show(1, "9 AM", movieId = 2, 160),
                        Show(2, "12 PM", movieId = 3, 200),
                        Show(3, "3 PM", movieId = 4, 240)
                    )
                ),
                Screen(
                    id = 2,
                    showList = listOf(
                        Show(1, "10 AM", movieId = 5, 160),
                        Show(2, "1 PM", movieId = 1, 200),
                        Show(3, "4 PM", movieId = 2, 240)
                    )
                )
            )
        )
    )

    fun getTheaterName(theaterId: Int): String {
        return theaterList().find { it.theaterId == theaterId }?.name ?: "Not Found"
    }

    fun getTheaterId(theaterName: String): Int {
        return theaterList().find { it.name.equals(theaterName, true) }?.theaterId ?: 0
    }

    fun getMovieName(id: Int): String {
        return movieList.find { it.id == id }?.name ?: "Not Found"
    }

    fun getMovieId(movieName: String): Int {
        return movieList.find { it.name.equals(movieName, true) }?.id ?: 0
    }

    fun getScreenId(theaterId: Int, movieId: Int, showId: Int): Int {
        return theaterList().filter { it.theaterId == theaterId }.flatMap { it.screen }
            .find { Screen -> Screen.showList.firstOrNull { it.id == showId && it.movieId == movieId } != null }?.id
            ?: 0
    }

    fun getShowList(theaterId: Int, movieId: Int): List<Show> {
        return theaterList().filter { it.theaterId == theaterId }.flatMap { it.screen }.flatMap { it.showList }
            .filter { it.movieId == movieId }
    }

    fun getShowTime(theaterId: Int, showId: Int): String {
        return (theaterList().filter { it.theaterId == theaterId }).flatMap { it.screen }.flatMap { it.showList }
            .find { it.id == showId }?.time ?: "Invalid Id"
    }

    fun getShowPrice(theaterId: Int, showId: Int): Int {
        return (theaterList().filter { it.theaterId == theaterId }).flatMap { it.screen }.flatMap { it.showList }
            .find { it.id == showId }?.price ?: 0
    }
}
