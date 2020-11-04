package com.example.demo.data;

import com.example.demo.exceptions.CannotSaveDataException;
import com.example.demo.exceptions.NoDataDirException;
import com.example.demo.exceptions.NoDataException;
import com.example.demo.model.dto.DataDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FileDataHandler implements DataHandler {

	private final ObjectMapper objectMapper;

	@Override
	public DataDto getDataById(int id) throws NoDataException {
		String filename = String.format("data/%d.json", id);
		try {
			@Cleanup InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
			if (Objects.isNull(inputStream)) {
				throw new NoDataException();
			}
			return objectMapper.readValue(inputStream, DataDto.class);
		} catch (IOException e) {
			throw new NoDataException();
		}
	}

	@Override
	public int save(DataDto dataDto, Optional<Integer> id) throws CannotSaveDataException {
		try {
			File dataDir = getDataDir();
			int newId = id.orElse(getLastId(dataDir) + 1);
			File newFile = new File(dataDir + "/" + newId + ".json");
			objectMapper.writeValue(newFile, dataDto);
			return newId;
		} catch (NoDataDirException | IOException e) {
			throw new CannotSaveDataException();
		}
	}

	private File getDataDir() throws NoDataDirException {
		String rootPathName = getClass().getClassLoader().getResource(".").getPath();
		if (rootPathName == null) {
			throw new NoDataDirException();
		}
		return new File(rootPathName + "/data");
	}

	private int getLastId(File dataDir) {
		return Arrays.stream(dataDir.listFiles())
				.map(File::getName)
				.map(s -> s.replace(".json", ""))
				.mapToInt(Integer::parseInt)
				.max()
				.orElse(1);
	}
}