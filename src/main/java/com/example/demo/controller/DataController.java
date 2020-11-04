package com.example.demo.controller;

import com.example.demo.exceptions.CannotSaveDataException;
import com.example.demo.exceptions.NoDataException;
import com.example.demo.model.dto.DataDto;
import com.example.demo.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/data")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DataController {

	private final DataService service;

	@GetMapping("{id}")
	public ResponseEntity<?> getData(@PathVariable int id) {
		try {
			return ResponseEntity.ok(service.getData(id));
		} catch (NoDataException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No data found!");
		}
	}

	@PostMapping(value = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> save(@RequestBody DataDto dataDto) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dataDto));
		} catch (CannotSaveDataException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can not save data!");
		}
	}

	@PostMapping(value = "merge/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> merge(@RequestBody DataDto dataDto, @PathVariable int id) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(service.merge(dataDto, id));
		} catch (CannotSaveDataException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Can not save data!");
		}
	}
}

