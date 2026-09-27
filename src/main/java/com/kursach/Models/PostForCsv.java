package com.kursach.Models;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "id",
    "source",
    "timestamp",
    "title",
    "url",
    "poster",
    "fanart",
    "ids.simkl_id",
    "ids.slug",
    "release_date",
    "rank",
    "drop_rate",
    "plan_to_watch",
    "ratings.simkl.rating",
    "ratings.simkl.votes",
    "ratings.imdb.rating",
    "ratings.imdb.votes",
    "country",
    "runtime",
    "status",
    "dvd_date",
    "overview"
})
public class PostForCsv {
    public PostForCsv(String source) {
        this.source = source;
    }

    public void fillPost(int id, Post post) {
        this.id = id;
        this.title = post.title();
        this.url = post.url();
        this.poster = post.poster();
        this.fanart = post.fanart();
        this.simklId = post.ids().simklId();
        this.slug = post.ids().slug();
        this.releaseDate = post.releaseDate();
        this.rank = post.rank();
        this.dropRate = post.dropRate();
        this.watched = post.watched();
        this.planToWatch = post.planToWatch();
        this.simklRating = post.ratings().simkl().rating();
        this.simklVotes = post.ratings().simkl().votes();
        this.imdbRating = post.ratings().imdb().rating();
        this.imdbVotes = post.ratings().imdb().votes();
        this.country = post.country();
        this.runtime = post.runtime();
        this.status = post.status();
        this.dvdDate = post.dvdDate();
        this.overview = post.overview();
    }

    public int id;
    public String source;
    public String timestamp = OffsetDateTime.now().toString();
    public String title;
    public String url;
    public String poster;
    public String fanart;
    public @JsonProperty("ids.simkl_id") int simklId;
    public @JsonProperty("ids.slug") String slug;
    public @JsonProperty("release_date") String releaseDate;
    public int rank;
    public @JsonProperty("drop_rate") String dropRate;
    public int watched;
    public @JsonProperty("plan_to_watch") int planToWatch;
    public @JsonProperty("ratings.simkl.rating") double simklRating;
    public @JsonProperty("ratings.simkl.votes") int simklVotes;
    public @JsonProperty("ratings.imdb.rating") double imdbRating;
    public @JsonProperty("ratings.imdb.votes") int imdbVotes;
    public String country;
    public String runtime;
    public String status;
    public @JsonProperty("dvd_date") String dvdDate;
    public String overview;
}
