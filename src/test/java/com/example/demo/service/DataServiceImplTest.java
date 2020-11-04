package com.example.demo.service;

import com.example.demo.data.DataHandler;
import com.example.demo.exceptions.CannotSaveDataException;
import com.example.demo.exceptions.NoDataException;
import com.example.demo.model.dto.DataDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class DataServiceImplTest {

	@InjectMocks
	DataServiceImpl dataService;
	@Mock
	DataHandler dataHandler;
	DataDto dataDto;

	@SneakyThrows
	@BeforeEach
	void setUp() {
		dataDto = getDataDto();
		when(dataHandler.getDataById(1)).thenReturn(DataDto.of(asList(1, 2, 3, 4)));
		when(dataHandler.getDataById(12)).thenThrow(NoDataException.class);
		when(dataHandler.save(dataDto, Optional.empty())).thenReturn(2);
	}

	private DataDto getDataDto() {
		return DataDto.of(asList(1, 2, 3, 4));
	}

	@Test
	void checkGetDataWithCorrectId() throws NoDataException {
		assertThat(dataService.getData(1), equalTo(dataDto));
	}

	@Test
	void checkGetDataWithIncorrectId() {
		assertThrows(NoDataException.class, () -> dataHandler.getDataById(12));
	}

	@Test
	void save() throws CannotSaveDataException {
		assertThat(dataService.save(dataDto), equalTo(2));
	}

	@Test
	void merge() {
		//todo
	}
}