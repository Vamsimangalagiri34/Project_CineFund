package com.cinefund.cinefund_movie_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cinefund.cinefund_movie_service.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{

}
