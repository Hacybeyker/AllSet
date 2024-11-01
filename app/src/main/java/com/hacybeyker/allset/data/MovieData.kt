package com.hacybeyker.allset.data

/**
 * Created by Carlos Osorio on 15/06/2021
 */

class MovieData {
    companion object {
        fun fetchMovieData(): ArrayList<Movie> {
            return arrayListOf(
                Movie(
                    "Example 01",
                    "This is a movie Example 01",
                    "https://plus.unsplash.com/premium_photo-1680871320511-8be2085ff281?q=80&w=3659&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    "https://plus.unsplash.com/premium_photo-1680871320511-8be2085ff281?q=80&w=3659&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    9.5
                ),
                Movie(
                    "Example 02",
                    "this is a movie Example 02",
                    "https://images.unsplash.com/photo-1525431836161-e40d6846e656?q=80&w=3056&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    "https://images.unsplash.com/photo-1525431836161-e40d6846e656?q=80&w=3056&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    9.5
                ),
                Movie(
                    "Example 03",
                    "this is a movie Example 03",
                    "https://plus.unsplash.com/premium_photo-1680871320511-8be2085ff281?q=80&w=3659&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    "https://plus.unsplash.com/premium_photo-1680871320511-8be2085ff281?q=80&w=3659&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    9.5
                ),
                Movie(
                    "Example 04",
                    "this is a movie Example 04",
                    "https://images.unsplash.com/photo-1525431836161-e40d6846e656?q=80&w=3056&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    "https://images.unsplash.com/photo-1525431836161-e40d6846e656?q=80&w=3056&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    9.5
                )
            )
        }
    }
}