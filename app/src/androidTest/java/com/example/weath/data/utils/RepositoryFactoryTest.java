package com.example.weath.data.utils;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.weath.domain.Repository;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RepositoryFactoryTest {

    @Test
    public void canCreateRepository(){
        Repository repository = RepositoryFactory.createRepository(
                InstrumentationRegistry.getInstrumentation().getTargetContext());
    }
}