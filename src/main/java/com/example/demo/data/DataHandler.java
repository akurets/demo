package com.example.demo.data;

import com.example.demo.exceptions.CannotSaveDataException;
import com.example.demo.exceptions.NoDataException;
import com.example.demo.model.dto.DataDto;

import java.util.Optional;

public interface DataHandler {
	
	DataDto getDataById(int id) throws NoDataException;
	
	int save(DataDto dataDto, Optional<Integer> id) throws CannotSaveDataException;
}
