package com.learn.gitops.services;

import com.learn.gitops.models.Joke;
import com.learn.gitops.repositories.JokeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JokeService {

    private final JokeRepository jokeRepository;

    public List<String> findAllJokes() {
        return jokeRepository.findAll().stream()
                .map((joke) -> joke.getJokeText() + (joke.getCategory() != null ? " (" + joke.getCategory() + ")" : ""))
                .toList();
    }

    public void saveJoke(String jokeText, String category) {
        Joke joke = Joke.builder()
                .jokeText(jokeText)
                .category(category)
                .build();
        jokeRepository.save(joke);
    }
}
