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
    String Trailer_JSON_STR;
    String REVIEW_JSON_STR;

    public Movie(Long MOVIE_ID, String TITLE, String ORIGINAL_TITLE, String OVERVIEW,
                 Long VOTE_COUNT, Double VOTE_AVARAGE, String RELEASE_DATE, String POSTER_PATH, String Trailer_JSON_STR, String REVIEW_JSON_STR) {
        this.MOVIE_ID = MOVIE_ID;
        this.TITLE = TITLE;
        this.ORIGINAL_TITLE = ORIGINAL_TITLE;
        this.OVERVIEW = OVERVIEW;
        this.VOTE_COUNT = VOTE_COUNT;
        this.VOTE_AVARAGE = VOTE_AVARAGE;
        this.RELEASE_DATE = RELEASE_DATE;
        this.POSTER_PATH = POSTER_PATH;
        this.Trailer_JSON_STR = Trailer_JSON_STR;
        this.REVIEW_JSON_STR = REVIEW_JSON_STR;
    }
}

