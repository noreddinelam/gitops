package com.learn.gitops.rest;

import com.learn.gitops.services.JokeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/jokes")
@Log4j2
public class JokeController {

    private final JokeService jokeService;

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllJokes() {
        log.info("REST request to get all jokes");
        List<String> jokes = jokeService.findAllJokes();
        return ResponseEntity.ok(jokes);
    }

    @GetMapping("/add")
    public ResponseEntity<Void> addJoke(@RequestParam(value = "joke") String jokeText,
                                         @RequestParam(value = "category", required = false) String category) {
        log.info("REST request to add a new joke");
        jokeService.saveJoke(jokeText, category);
        return ResponseEntity.ok().build();
    }
}
