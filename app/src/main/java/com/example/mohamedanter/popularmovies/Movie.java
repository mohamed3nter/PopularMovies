package com.example.mohamedanter.popularmovies;

/**
 * Created by Mohamed Anter on 8/22/2015.
 */
public class Movie {
    Long MOVIE_ID;
    String TITLE;
    String ORIGINAL_TITLE;
    String OVERVIEW;
    Long VOTE_COUNT;
    Double VOTE_AVARAGE;
    String RELEASE_DATE;
    String POSTER_PATH;

    public Movie(Long MOVIE_ID, String TITLE, String ORIGINAL_TITLE, String OVERVIEW,
                 Long VOTE_COUNT, Double VOTE_AVARAGE, String RELEASE_DATE, String POSTER_PATH) {
        this.MOVIE_ID = MOVIE_ID;
        this.TITLE = TITLE;
        this.ORIGINAL_TITLE = ORIGINAL_TITLE;
        this.OVERVIEW = OVERVIEW;
        this.VOTE_COUNT = VOTE_COUNT;
        this.VOTE_AVARAGE = VOTE_AVARAGE;
        this.RELEASE_DATE = RELEASE_DATE;
        this.POSTER_PATH = POSTER_PATH;
    }


}
