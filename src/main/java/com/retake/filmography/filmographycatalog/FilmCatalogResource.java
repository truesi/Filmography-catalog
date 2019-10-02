package com.retake.filmography.filmographycatalog;


import com.retake.filmography.filmographycatalog.models.CatalogItem;
import com.retake.filmography.filmographycatalog.models.MovieDummy;
import com.retake.filmography.filmographycatalog.models.Rating;
import com.retake.filmography.filmographycatalog.models.UserRating;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class FilmCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    //use webclient for asynchronous purposes
    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userID}")
    public List<CatalogItem> getCatalog(@PathVariable("userID") String userID){

        //get all rated movies id
        UserRating ratings = restTemplate.getForObject("http://film-rating-service/rating/users/" + userID, UserRating.class);

        return ratings
                .getUserRating()
                .stream()
                .map(rating -> {
                    //for each movie id call movie info service and get details
                    MovieDummy movieDummy = restTemplate.getForObject("http://movie-dummy-service/movies/" + rating.getMovieID(), MovieDummy.class);
//                    this way we can make api call asynchronous althoug resttemplate is async it is deprecated and will no longer support
//                    MovieDummy movieDummy = webClientBuilder.build()
//                            .get()
//                            .uri("http://localhost:8085/movies/" + rating.getMovieID())
//                            .retrieve()
//                            .bodyToMono(MovieDummy.class)
//                            .block();
                    //put them all together
                    return new CatalogItem(movieDummy.getName(), "Test", rating.getRating());
                    })
                .collect(Collectors.toList());


    }
}
