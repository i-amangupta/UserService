package com.user.service.UserService.service.impl;

import com.user.service.UserService.entity.User;
import com.user.service.UserService.exception.ResourceNotFoundException;
import com.user.service.UserService.model.Hotel;
import com.user.service.UserService.model.Rating;
import com.user.service.UserService.repository.UserRepository;
import com.user.service.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;

    private static final String GET_RATINGS_BY_USER = "http://localhost:8083/ratings/getRatingsByUser?userId=";
    private static final String GET_HOTEL_BY_ID = "http://localhost:8082/hotels/getHotelById?hotelId=";

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = userRepository.findAll();
        userList.stream().forEach(user -> {
            Rating[] ratingArray = restTemplate.getForObject(GET_RATINGS_BY_USER + user.getUserId(), Rating[].class);
            List<Rating> ratings = Arrays.asList(ratingArray);
            ratings.stream().forEach(rating ->
                rating.setHotel(restTemplate.getForEntity(GET_HOTEL_BY_ID + rating.getHotelId(), Hotel.class).getBody())
            );
            user.setRatings(ratings);
        });
        return userList;
    }

    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found: " + userId));
        Rating[] ratingArray = restTemplate.getForObject(GET_RATINGS_BY_USER + user.getUserId(), Rating[].class);
        List<Rating> ratings = Arrays.asList(ratingArray);
        ratings.stream().forEach(rating ->
            rating.setHotel(restTemplate.getForEntity(GET_HOTEL_BY_ID + rating.getHotelId(), Hotel.class).getBody())
        );
        user.setRatings(ratings);
        return user;
    }
}
