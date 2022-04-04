package com.example.go4luncch;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.go4luncch.models.User;
import com.example.go4luncch.repositories.UserRepository;
import com.example.go4luncch.viewmodels.WorkmatesViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)

public class WorkmatesViewModelUnitTest {

    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private final Application mApplication = BaseApplication.getApplication();
    private final UserRepository mUserRepository = Mockito.mock(UserRepository.class);

    private final MutableLiveData<User> mCurrentUserDataTest = new MutableLiveData<>();
    private final MutableLiveData<List<User>> lWorkmatesLiveDataTest = new MutableLiveData<>();

    @Before
    public void setUp() {
        lWorkmatesLiveDataTest.setValue(DataTest.generateCoworkerTest());

        Mockito.when(mUserRepository.getAllUsers()).thenReturn(lWorkmatesLiveDataTest);
    }


    @Test
    public void findWorkmatesListExceptCurrentUser() throws InterruptedException {
        lWorkmatesLiveDataTest.setValue(DataTest.generateCoworkerTest());

        WorkmatesViewModel workmateViewModel = new WorkmatesViewModel(mUserRepository);
        List<User> workmatesList = LiveDataTest.getValue(workmateViewModel.getWorkmates());

        Assert.assertEquals("Laurence Name", workmatesList.get(0).getUserName());
        Assert.assertEquals("laurencemail@orange.fr", workmatesList.get(0).getUserMail());
        Assert.assertEquals("https://i.pravatar.cc/150?u=a042581f4e29026704d", workmatesList.get(0).getUrlPicture());
        Assert.assertEquals("Laurence Id", workmatesList.get(0).getUid());
        Assert.assertEquals("Restaurant 1", workmatesList.get(0).getChosenRestaurantId());
        Assert.assertEquals("Restaurant 1 name", workmatesList.get(0).getChosenRestaurantName());
        Assert.assertEquals("address Restaurant 1", workmatesList.get(0).getChosenRestaurantAddress());
        Assert.assertEquals("04/03/2022",  workmatesList.get(0).getChosenRestaurantDate());

        Assert.assertEquals("Thierry Name", workmatesList.get(1).getUserName());
        Assert.assertEquals("thierrymail@orange.fr", workmatesList.get(1).getUserMail());
        Assert.assertEquals("https://i.pravatar.cc/150?u=a042581f4e29026704e", workmatesList.get(1).getUrlPicture());
        Assert.assertEquals("Thierry Id", workmatesList.get(1).getUid());
        Assert.assertEquals("Chosen Restaurant Id Thierry", workmatesList.get(1).getChosenRestaurantId());
        Assert.assertEquals("Chosen Restaurant Name Thierry", workmatesList.get(1).getChosenRestaurantName());
        Assert.assertEquals("Chosen Restaurant Address Thierry", workmatesList.get(1).getChosenRestaurantAddress());
        Assert.assertEquals("04/03/2022",  workmatesList.get(1).getChosenRestaurantDate());

        Assert.assertEquals("Marie Name", workmatesList.get(2).getUserName());
        Assert.assertEquals("ronmail@orange.fr", workmatesList.get(2).getUserMail());
        Assert.assertEquals("https://i.pravatar.cc/150?u=a042581f4e29026704f", workmatesList.get(2).getUrlPicture());
        Assert.assertEquals("Marie Id", workmatesList.get(2).getUid());
        Assert.assertEquals("Chosen Restaurant Id Marie", workmatesList.get(2).getChosenRestaurantId());
        Assert.assertEquals("Chosen Restaurant Name Marie", workmatesList.get(2).getChosenRestaurantName());
        Assert.assertEquals("Chosen Restaurant Address Marie", workmatesList.get(2).getChosenRestaurantAddress());
        Assert.assertEquals("04/03/2022",  workmatesList.get(2).getChosenRestaurantDate());

    }


}

