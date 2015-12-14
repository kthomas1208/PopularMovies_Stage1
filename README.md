# PopularMovies_Stage2
Udacity Android Nanodegree Project 2

Author: Kevin Thomas

## Purpose
This app is for Udacity's Android Nanodegree. It's part 2 of a 2 part project testing our knowledge of basic Android development. Some concepts covered are:
* Creating, managing, and launching new activities with intents
* Using an API to pull data from the Internet
* Managing layouts
* Running tasks asynchronously 
* Looking for help in the right places
* Implementing a tablet UI
* Launching external intents
* Data Persistence 
* Having fun programming

## Description
The app uses an API provided by [The Movie Database (TMDb)](https://www.themoviedb.org/) to get movie info. It displays a grid of movie posters sorted by either popularity or vote count, depending on the user's selection. When the user clicks on a movie poster, a more detailed view of the movie is displayed showing the movie's title, release year, rating, and plot synopsis.
In stage 2, we were required to add trailers and reviews for movies, as well as implement a tablet UI. 

### Running the app yourself
You'll need to download Android Studio or some other IDE and clone this repository from your version control system. 
You'll also need to [create an API key](https://www.themoviedb.org/account/signup) from The Movie Database to run the code. Once you have 
your key, edit the gradle.properties file in the project folder and add your key with quotes like so: `TMDBApiKey = "apikeyhere"`. As a word
of caution, never upload API keys from any site to a public repository like GitHub.

## Libraries/References
* [The Movie Database API ](https://www.themoviedb.org/documentation/api)
* [Picasso](http://square.github.io/picasso/) for managing ImageViews 
* [Stetho](http://facebook.github.io/stetho/) for inspecting the SQL database and SharedPreferences during testing and debugging
* Play icon for trailers made by [Freepik](http://www.freepik.com) from [www.flaticon.com](http://www.flaticon.com) is licensed under [CC BY 3.0](http://creativecommons.org/licenses/by/3.0/)

### Stack Overflow/Forum references
These are the links to Stack Overflow pages and forum threads that helped me during the development process
* [RatingStar color without using drawables](http://stackoverflow.com/questions/20209884/ratingbar-changing-star-color-without-using-custom-images
)
* [Fixing poster pictures in detail view](http://stackoverflow.com/questions/24105470/imageview-getting-very-small-under-a-linear-layout)
* [Starting YouTube Intent](http://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent)
* [How to get the trailers and reviews in one JSON response](https://discussions.udacity.com/t/how-to-display-movie-reviews/27758/2) (Udacity Forums)
