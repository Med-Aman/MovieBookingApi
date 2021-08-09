package com.example

import com.example.models.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.kotest.annotation.MicronautTest

@MicronautTest
class MovieBookingControllerTest(
    @Client("/") private val client: HttpClient
) : StringSpec({
    val jsonObj = jacksonObjectMapper()
    val mapper = ObjectMapper().registerModule(
        KotlinModule(nullIsSameAsDefault = true)
    )

    "should greet" {
        val response = client.toBlocking().retrieve("/say-hello/name")
        response shouldBe "Hello name"
    }

    "should return list of movies" {
        val response = client.toBlocking().retrieve("/movie-list")
        val actualMovieList: List<Movie> = jsonObj.readValue(response)
        val expectedMovieList = listOf(
            Movie(1, "Avengers"),
            Movie(2, "Thor"),
            Movie(3, "Dhoom"),
            Movie(4, "Bahubali"),
            Movie(5, "Superman"),
        )
        actualMovieList shouldBe expectedMovieList
    }

    "should return filtered theater list based on movie choice" {
        val response = client.toBlocking().retrieve("/movie/5")
        val actualResponse: List<Theater> = jsonObj.readValue(response)
        val expected = listOf(
            Theater(
                theaterId = 1, name = "Inox",
                screen = listOf(
                    Screen(
                        id = 1, showList = listOf()
                    ),
                    Screen(
                        id = 2,
                        showList = listOf(
                            Show(2, "1 PM", movieId = 5, 200),
                        )
                    )
                )
            ),
            Theater(
                theaterId = 2, name = "PVR",
                screen = listOf(
                    Screen(
                        id = 1, showList = listOf()
                    ),
                    Screen(
                        id = 2,
                        showList = listOf(
                            Show(1, "10 AM", movieId = 5, 160),
                        )
                    )
                )
            )
        )
        actualResponse shouldBe expected
    }

    "should return movie" {
        val response = client.toBlocking().retrieve("/movie-list/1")
        val movie: Movie = jsonObj.readValue(response)
        movie shouldBe Movie(1, "Avengers")
    }

    "should return List of shows when movie id and theater id are params" {
        val response = client.toBlocking().retrieve("/movie/1/1")
        val actualResponse: List<Show> = jsonObj.readValue(response)
        val expectedResponse = listOf(
            Show(1, "9 AM", movieId = 1, 160),
            Show(3, "4 PM", movieId = 1, 240)
        )
        actualResponse shouldBe expectedResponse
    }

    "should return theater object" {
        val response = client.toBlocking().retrieve("/theater?theatername=Inox")
        val actualResponse: List<Theater> = jsonObj.readValue(response)
        val expectedResponse = listOf(
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
            )
        )
        actualResponse shouldBe expectedResponse
    }

    "should add a movie" {
        val payLoad = mapper.writeValueAsString(Movie(1, "XYZ"))
        val res = client.toBlocking().retrieve(HttpRequest.POST("/add-movie", payLoad))
        res shouldBe "Successfully added XYZ"
    }

    "should add a Theater" {
        val payLoad = mapper.writeValueAsString(
            Theater(
                theaterId = 1, name = "SRS",
                screen = listOf(
                    Screen(
                        id = 1,
                        showList = listOf(
                            Show(1, "9 AM", movieId = 1, 160),
                        )
                    ),
                    Screen(
                        id = 2,
                        showList = listOf(
                            Show(1, "10 AM", movieId = 4, 160),
                        )
                    )
                )
            )
        )
        val res = client.toBlocking().retrieve(HttpRequest.POST("/add-theater", payLoad))

        res shouldBe "Successfully added SRS"
    }

    "should return a ticket instance" {
        val response = client.toBlocking().retrieve("/book-ticket?movie=Avengers&theater=Inox&show=1")
        val actualResponse: Ticket = jsonObj.readValue(response)
        actualResponse shouldBe Ticket("Avengers", "Inox", "9 AM", 1, 160)
    }
})
