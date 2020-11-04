package com.example.demo.data;

import com.example.demo.exceptions.CannotSaveDataException;
import com.example.demo.exceptions.NoDataDirException;
import com.example.demo.exceptions.NoDataException;
import com.example.demo.model.dto.DataDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
class FileDataHandlerTest {

	@Autowired
	DataHandler dataHandler;
	@Autowired
	ObjectMapper mapper;

	DataDto expectedDataDto;
	int expectedNextId;

	@BeforeEach
	void setup() throws IOException, NoDataDirException {
		InputStream is = getClass().getClassLoader().getResourceAsStream("data/1.json");
		expectedDataDto = mapper.readValue(is, DataDto.class);

		expectedNextId = getLastId() + 1;
	}

	private int getLastId() throws NoDataDirException {
		File dataDir = getDataDir();
		return Arrays.stream(dataDir.listFiles())
				.map(File::getName)
				.map(s -> s.replace(".json", ""))
				.mapToInt(Integer::parseInt)
				.max()
				.orElse(1);
	}

	private File getDataDir() throws NoDataDirException {
		String rootPathName = getClass().getClassLoader().getResource(".").getPath();
		if (rootPathName == null) {
			throw new NoDataDirException();
		}
		return new File(rootPathName + "/data");
	}

	@Test
	void checkGetDataByIdWithCorrectId() throws NoDataException {
		DataDto dataById = dataHandler.getDataById(1);
		Assertions.assertEquals(expectedDataDto, dataById);
	}

	@Test
	void checkGetDataByIdWithIncorrectId() {
		Assertions.assertThrows(NoDataException.class, () -> dataHandler.getDataById(expectedNextId));
	}

	@Test
	void save() throws CannotSaveDataException {
		int savedId = dataHandler.save(expectedDataDto, Optional.empty());
		Assertions.assertEquals(expectedNextId, savedId);
	}
}