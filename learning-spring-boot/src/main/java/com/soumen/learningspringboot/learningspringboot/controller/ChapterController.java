package com.soumen.learningspringboot.learningspringboot.controller;

import com.soumen.learningspringboot.learningspringboot.model.Chapter;
import com.soumen.learningspringboot.learningspringboot.repository.ChapterRepository;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChapterController {

	private final ChapterRepository repository;

	public ChapterController(ChapterRepository repository) {
		this.repository = repository;
	}

	@GetMapping("/chapters")
	public Flux<Chapter> listing() {
		return repository.findAll();
	}
}
