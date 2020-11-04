package com.example.demo.service;

import com.example.demo.data.DataHandler;
import com.example.demo.exceptions.CannotSaveDataException;
import com.example.demo.exceptions.NoDataException;
import com.example.demo.model.dto.DataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

	private final DataHandler fileDataHandler;

	@Override
	public DataDto getData(int id) throws NoDataException {
		return fileDataHandler.getDataById(id);
	}

	@Override
	public int save(DataDto dataDto) throws CannotSaveDataException {
		return fileDataHandler.save(dataDto, Optional.empty());
	}

	@Override
	public DataDto merge(DataDto dataDto, int id) throws CannotSaveDataException {
		try {
			DataDto dataById = fileDataHandler.getDataById(id);
			List<Integer> mergeDataArrays = mergeDataArrays(dataDto, dataById);
			DataDto merged = DataDto.of(mergeDataArrays);
			fileDataHandler.save(merged, Optional.of(id));
			return merged;
		} catch (NoDataException e) {
			save(dataDto);
			return dataDto;
		}
	}

	private List<Integer> mergeDataArrays(DataDto dataDto, DataDto dataById) {
		List<Integer> result = dataById.getData();
		result.addAll(dataDto.getData());
		result.sort(Integer::compareTo);
		return result;
	}
}
