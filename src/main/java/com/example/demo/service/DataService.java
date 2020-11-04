package com.example.demo.service;

import com.example.demo.exceptions.CannotSaveDataException;
import com.example.demo.exceptions.NoDataException;
import com.example.demo.model.dto.DataDto;

public interface DataService {
	
	DataDto getData(int id) throws NoDataException;
	
	int save(DataDto dataDto) throws CannotSaveDataException;

	DataDto merge(DataDto dataDto, int id) throws CannotSaveDataException;
}
