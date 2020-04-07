package com.soumen.learningspringboot.learningspringboot.service;/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import com.soumen.learningspringboot.learningspringboot.model.Image;
import com.soumen.learningspringboot.learningspringboot.repository.ImageRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;

/**
 * @author Greg Turnquist
 */
@Service
public class ImageService {

	public static String UPLOAD_ROOT = "C:\\data";

	// tag::injection[]
	private final ResourceLoader resourceLoader;

	private final ImageRepository imageRepository;

	public ImageService(ResourceLoader resourceLoader,
						ImageRepository imageRepository) {
		this.resourceLoader = resourceLoader;
		this.imageRepository = imageRepository;
	}
	// end::injection[]

	// tag::1[]
	public Flux<Image> findAllImages() {
		return imageRepository.findAll()
				.log("findAll");
	}
	// end::1[]

	public Mono<Resource> findOneImage(String filename) {
		return Mono.fromSupplier(() ->
				resourceLoader.getResource(
						"file:" + UPLOAD_ROOT + "/" + filename))
				.log("findOneImage");
	}

	// tag::2[]
	public Mono<Void> createImage(Flux<FilePart> files) {
		return files
				.log("createImage-files")
				.flatMap(file -> {
					Mono<Image> saveDatabaseImage = imageRepository.save(
							new Image(
									UUID.randomUUID().toString(),
									file.filename()))
							.log("createImage-save");

					Mono<Void> copyFile = Mono.just(
							Paths.get(UPLOAD_ROOT, file.filename())
									.toFile())
							.log("createImage-picktarget")
							.map(destFile -> {
								try {
									destFile.createNewFile();
									return destFile;
								} catch (IOException e) {
									throw new RuntimeException(e);
								}
							})
							.log("createImage-newfile")
							.flatMap(file::transferTo)
							.log("createImage-copy");

					return Mono.when(saveDatabaseImage, copyFile)
							.log("createImage-when");
				})
				.log("createImage-flatMap")
				.then()
				.log("createImage-done");
	}
	// end::2[]

	// tag::3[]
	public Mono<Void> deleteImage(String filename) {
		Mono<Void> deleteDatabaseImage = imageRepository
				.findByName(filename)
				.log("deleteImage-find")
				.flatMap(imageRepository::delete)
				.log("deleteImage-record");

		Mono<Object> deleteFile = Mono.fromRunnable(() -> {
			try {
				Files.deleteIfExists(
						Paths.get(UPLOAD_ROOT, filename));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		})
				.log("deleteImage-file");

		return Mono.when(deleteDatabaseImage, deleteFile)
				.log("deleteImage-when")
				.then()
				.log("deleteImage-done");
	}
	// end::3[]


}
