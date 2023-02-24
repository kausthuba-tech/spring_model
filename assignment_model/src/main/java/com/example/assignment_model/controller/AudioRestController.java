package com.example.assignment_model.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.assignment_model.model.Audio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@EnableAsync
@RequestMapping("/audioApi")
@Tag(name = "audioApi")
public class AudioRestController {

	ConcurrentHashMap<String, Audio> audioDB = new ConcurrentHashMap<>();

	{
		audioDB.put("eminem", new Audio("eminem", "Mockingbird", "Slimshady", 1, 2010, 542, 2500));
		audioDB.put("usher", new Audio("usher", "ushery", "gotham", 2, 2001, 121, 1200));
		audioDB.put("akon", new Audio("akon", "Smackthat", "Broken", 3, 2003, 250, 2200));
	}

	@Async
	@GetMapping(value = "/audios")
	@Operation(summary = "Get all audio items")
	public CompletableFuture<ConcurrentHashMap<String, Audio>> getAllAudioItems() {
		return CompletableFuture.completedFuture(audioDB);
	}

	@Async
	@PostMapping(value = "/newAudio")
	@Operation(summary = "Create a new audio item")
	public CompletableFuture<ResponseEntity<?>> createAudioItem(@RequestBody Audio newAudio) {
		try {
			audioDB.put(newAudio.getArtistName(), newAudio);
			return CompletableFuture
					.completedFuture(new ResponseEntity<>("Audio Item added successfully", HttpStatus.OK));
		} catch (ConfigDataResourceNotFoundException ex) {
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null));
		}
	}

	@Async
	@GetMapping(value = "/audio/{artistName}")
	@Operation(summary = "Get an audio item by artist name")
	public CompletableFuture<Audio> findByArtistName(@PathVariable("artistName") String artistName) {
		return CompletableFuture.completedFuture(audioDB.get(String.valueOf(artistName)));
	}

	@Async
	@GetMapping(value = "/audio/{artistName}/{property}")
	@Operation(summary = "Get an audio item property by an artist name")
	public CompletableFuture<Object> getPropertyByArtistName(@PathVariable("artistName") String artistName,
			@PathVariable("property") String property) {
		Audio audio = audioDB.get(artistName);
		if (audio != null) {
			if ("artistName".equals(property)) {
				return CompletableFuture.completedFuture(audio.getArtistName());
			} else if ("trackTitle".equals(property)) {
				return CompletableFuture.completedFuture(audio.getTrackTitle());
			} else if ("albumTitle".equals(property)) {
				return CompletableFuture.completedFuture(audio.getAlbumTitle());
			} else if ("trackNumber".equals(property)) {
				return CompletableFuture.completedFuture(audio.getTrackNumber());
			} else if ("year".equals(property)) {
				return CompletableFuture.completedFuture(audio.getYear());
			} else if ("reviewCount".equals(property)) {
				return CompletableFuture.completedFuture(audio.getReviewCount());
			} else if ("copiesSold".equals(property)) {
				return CompletableFuture.completedFuture(audio.getCopiesSold());
			} else {
				return CompletableFuture.completedFuture("Invalid property name");
			}

		} else {
			return CompletableFuture.completedFuture("Audio not found");
		}
	}

	@Async
	@GetMapping(value = "/copiesSold")
	@Operation(summary = "Get the total copies sold of all the audio items")
	public CompletableFuture<Integer> getTotalCopiesSold() {
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		List<CompletableFuture<Integer>> futures = new CopyOnWriteArrayList<>();
		audioDB.forEach((key, value) -> {
			CompletableFuture<Integer> future = CompletableFuture.supplyAsync(value::getCopiesSold, executorService);
			futures.add(future);
		});
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
				.thenApply(v -> futures.stream().map(CompletableFuture::join).reduce(0, Integer::sum));
	}
}
