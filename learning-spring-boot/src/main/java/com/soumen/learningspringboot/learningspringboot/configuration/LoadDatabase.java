package com.soumen.learningspringboot.learningspringboot.configuration;

import com.soumen.learningspringboot.learningspringboot.model.Chapter;
import com.soumen.learningspringboot.learningspringboot.model.Image;
import com.soumen.learningspringboot.learningspringboot.model.Product;
import org.springframework.data.mongodb.core.MongoOperations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class LoadDatabase {

	public static String UPLOAD_ROOT = "C:\\data";
	@Bean
	CommandLineRunner init(MongoOperations operations) {
		return args -> {
			operations.dropCollection(Image.class);
			operations.dropCollection(Chapter.class);
			operations.dropCollection(Product.class);
			operations.insert(new Image("1","learning-spring-boot-cover.jpg"));
			operations.insert(new Image("2","learning-spring-boot-2nd-edition-cover.jpg"));
			operations.insert(new Image("3","bazinga.png"));
			operations.findAll(Image.class).forEach(image -> {
				System.out.println(image.toString());
			});

			operations.insert(new Chapter("Quick Start with Java"));
			operations.insert(new Chapter("Reactive Web with Spring Boot"));
			operations.insert(new Chapter("...and more!"));
			operations.findAll(Chapter.class).forEach(chapter -> {
				System.out.println(chapter.toString());
			});


			operations.insert(new Product(null, "Big Latte", 2.99));
			operations.insert(new Product(null, "Big Decaf", 2.49));
			operations.insert(new Product(null, "Green Tea", 1.99));
			operations.findAll(Product.class).forEach(product -> {
				System.out.println(product.toString());
			});

		};
	}

	/**
	 * Pre-load some test images
	 *
	 * @return Spring Boot {@link CommandLineRunner} automatically
	 *         run after app context is loaded.
	 */
	@Bean
	CommandLineRunner setUp() throws IOException {
		return (args) -> {
			FileSystemUtils.deleteRecursively(new File(UPLOAD_ROOT));

			Files.createDirectory(Paths.get(UPLOAD_ROOT));

			FileCopyUtils.copy("Test file",
					new FileWriter(UPLOAD_ROOT +
							"/learning-spring-boot-cover.jpg"));

			FileCopyUtils.copy("Test file2",
					new FileWriter(UPLOAD_ROOT +
							"/learning-spring-boot-2nd-edition-cover.jpg"));

			FileCopyUtils.copy("Test file3",
					new FileWriter(UPLOAD_ROOT + "/bazinga.png"));
		};
	}
}
