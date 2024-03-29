package com.retake.filmography.filmographycatalog;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
    @HystrixCommand(fallbackMethod = "getFallBackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userID") String userID){

        //get all rated movies id
        UserRating ratings = getUserRating(userID);

        return ratings
                .getUserRating()
                .stream()
                .map(rating -> {
                    return getCatalogItem(rating);
                })
                .collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallBackCatalogItem")
    private CatalogItem getCatalogItem(Rating rating) {
        //for each movie id call movie info service and get details
        MovieDummy movieDummy = restTemplate.getForObject("http://moviedummyservice/movies/" + rating.getMovieID(), MovieDummy.class);

        //put them all together
        return new CatalogItem(movieDummy.getName(), "Test", rating.getRating());
    }

    private CatalogItem getFallBackCatalogItem(Rating rating){
        return new CatalogItem("Movie not found ", "", rating.getRating());
    }

    @HystrixCommand(fallbackMethod = "getFallBackUserRating")
    private UserRating getUserRating(@PathVariable("userID") String userID) {
        return restTemplate.getForObject("http://ratingservice/rating/users/" + userID, UserRating.class);
    }

    private UserRating getFallBackUserRating(@PathVariable("userID") String userID) {
        UserRating userRating = new UserRating();
        userRating.setUserId(userID);
        userRating.setUserRating(Arrays.asList(new Rating("0", 0)));
        return userRating;
    }

    //circuit breaker if no response hystrix
    public List<CatalogItem> getFallBackCatalog(@PathVariable("userID") String userID) {
        return Arrays.asList(new CatalogItem("No movie", "",0));
    }
}


//                    this way we can make api call asynchronous althoug resttemplate is async it is deprecated and will no longer support
//                    MovieDummy movieDummy = webClientBuilder.build()
//                            .get()
//                            .uri("http://localhost:8085/movies/" + rating.getMovieID())
//                            .retrieve()
//                            .bodyToMono(MovieDummy.class)
//                            .block();