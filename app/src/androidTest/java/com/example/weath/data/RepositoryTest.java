package com.example.weath.data;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weath.data.domainModels.Coordinate;
import com.example.weath.data.local.DatabaseManager;
import com.example.weath.data.local.dataTransferObjects.CityFullDto;
import com.example.weath.data.remote.RemoteDataSource;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryTest {
    private Repository repository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RemoteDataSource remoteDataSource;

    @Mock
    private DatabaseManager databaseManager;

    @Before
    public void initializeRepository(){
        repository = new Repository(remoteDataSource, databaseManager);
    }

    @Test
    public void getCityByLocationAsync_FetchFromInternetWhenThereIsNotExistingInDatabase(){
        Coordinate coordinate = new Coordinate(40.71, -74.00);

        LiveData<Boolean> notExisting = new MutableLiveData<>(false);

        when(databaseManager.isExisting(coordinate))
        .thenReturn(notExisting);

        CityFullDto cityFullDto = new CityFullDto();
        cityFullDto.name = "TestName";
        cityFullDto.country = "TestCountry";

        LiveData<CityFullDto> expected = new MutableLiveData<>(cityFullDto);

        when(remoteDataSource.getCityByLocationAsync(coordinate))
        .thenReturn(new MutableLiveData<CityFullDto>());

        LiveData<CityFullDto> result = repository.getCityByLocationAsync(coordinate);

        assertThat(result.getValue(), (Matcher<? super CityFullDto>) expected.getValue());
    }
}