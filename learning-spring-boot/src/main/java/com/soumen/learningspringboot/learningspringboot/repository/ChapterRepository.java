package com.soumen.learningspringboot.learningspringboot.repository;

import com.soumen.learningspringboot.learningspringboot.model.Chapter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChapterRepository  extends ReactiveCrudRepository<Chapter, String> {
}
