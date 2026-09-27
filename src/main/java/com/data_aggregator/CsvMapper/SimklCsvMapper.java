package com.data_aggregator.CsvMapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.data_aggregator.Models.AggregatedCsvRow;
import com.data_aggregator.Models.Post;

public class SimklCsvMapper implements CsvRowMapper<Post> {

    private int id;

    public SimklCsvMapper() {
    }

    public SimklCsvMapper(int id) {
        this.id = id;
    }

    public List<AggregatedCsvRow> getRows(List<Post> posts) {
        return toRows(posts, id);
    }

    @Override
    public String getSourceName() {
        return "simkl";
    }

    @Override
    public Class<Post> getDataType() {
        return Post.class;
    }

    @Override
    public List<AggregatedCsvRow> toRows(List<Post> posts, int id) {
        List<AggregatedCsvRow> rows = new ArrayList<>();
        for (Post post : posts) {
            AggregatedCsvRow row = new AggregatedCsvRow();

            row.id = id;
            row.source = "simkl";
            row.timestamp = OffsetDateTime.now().toString();

            row.title = post.title();
            row.url = post.url();
            row.poster = post.poster();
            row.fanart = post.fanart();

            row.simklId = post.ids().simklId();
            row.slug = post.ids().slug();

            row.releaseDate = post.releaseDate();

            row.rank = post.rank();
            row.dropRate = post.dropRate();

            row.watched = post.watched();
            row.planToWatch = post.planToWatch();

            row.simklRating = post.ratings().simkl().rating();
            row.simklVotes = post.ratings().simkl().votes();

            row.imdbRating = post.ratings().imdb().rating();
            row.imdbVotes = post.ratings().imdb().votes();

            row.country = post.country();
            row.runtime = post.runtime();
            row.status = post.status();
            row.dvdDate = post.dvdDate();
            row.overview = post.overview();

            rows.add(row);
        }

        return rows;
    }
}
