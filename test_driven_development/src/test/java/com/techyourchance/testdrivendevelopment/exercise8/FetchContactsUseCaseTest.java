package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {
    // region constants
    private static final String FILTER_TERM = "filterTerm";
    private static final String ID = "ID";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String IMAGE_URL = "IMAGE_URL";
    private static final String FULL_PHONE_NUMBER = "FULL_PHONE_NUMBER";
    private static final int AGE = 20;
    //endregion constants

    // region helper fields

    FetchContactsUseCaseTd fetchContactsUseCaseTd;
    @Mock FetchContactsUseCase.Listener mListenerMock1;
    @Mock FetchContactsUseCase.Listener mListenerMock2;
    @Captor ArgumentCaptor<List<Contact>> mAcListContacts;

    //endregion helper fields

    FetchContactsUseCase SUT;

    @Before
    public void setup() throws Exception {
        fetchContactsUseCaseTd = new FetchContactsUseCaseTd();
        SUT = new FetchContactsUseCase(fetchContactsUseCaseTd);
    }

    @Test
    public void fetchContacts_correctFilterTermPassedToEndpoint() {
        // Arrange
        // Act
        SUT.fetchContacts(FILTER_TERM);
        // Assert
        assertThat(fetchContactsUseCaseTd.mInvocationCount, is(1));
        assertThat(fetchContactsUseCaseTd.mFilterTerm, is(FILTER_TERM));
    }

    @Test
    public void fetchContacts_success_observersNotifiedWithCorrectData() {
        // Arrange
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContacts(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onContactsFetched(mAcListContacts.capture());
        verify(mListenerMock2).onContactsFetched(mAcListContacts.capture());
        List<List<Contact>> captures = mAcListContacts.getAllValues();
        List<Contact> capture1 = captures.get(0);
        List<Contact> capture2 = captures.get(1);
        assertThat(capture1, is(getContacts()));
        assertThat(capture2, is(getContacts()));
    }

    @Test
    public void fetchContacts_success_unsubscribedObserversNotNotified() {
        // Arrange
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.unregisterListener(mListenerMock2);
        SUT.fetchContacts(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onContactsFetched(any(List.class));
        verifyNoMoreInteractions(mListenerMock2);
    }

    @Test
    public void fetchContacts_generalError_observersNotifiedOnFailure() {
        // Arrange
        generalError();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContacts(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onFetchContactsFailed();
        verify(mListenerMock2).onFetchContactsFailed();
    }

    @Test
    public void fetchContacts_networkError_observersNotifiedOnFailure() {
        // Arrange
        networkError();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchContacts(FILTER_TERM);
        // Assert
        verify(mListenerMock1).onFetchContactsFailed();
        verify(mListenerMock2).onFetchContactsFailed();
    }


    // region helper methods

    private List<ContactSchema> getContactsSchemes() {
        List<ContactSchema> contacts = new ArrayList<>();
        contacts.add(new ContactSchema(ID, FULL_NAME, FULL_PHONE_NUMBER, IMAGE_URL, AGE));
        return contacts;
    }

    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(ID, FULL_NAME, IMAGE_URL));
        return contacts;
    }

    private void generalError() {
        fetchContactsUseCaseTd.isGeneralError = true;
    }

    private void networkError() {
        fetchContactsUseCaseTd.isNetworkError = true;
    }

    // endregion helper methods

    // region helper classes

    private class FetchContactsUseCaseTd implements GetContactsHttpEndpoint {
        public int mInvocationCount;
        public boolean isNetworkError;
        private String mFilterTerm;
        private boolean isGeneralError;

        @Override
        public void getContacts(String filterTerm, Callback callback) {
            mInvocationCount++;
            mFilterTerm = filterTerm;

            if(isGeneralError) {
                callback.onGetContactsFailed(FailReason.GENERAL_ERROR);
            }
            else if(isNetworkError) {
                callback.onGetContactsFailed(FailReason.NETWORK_ERROR);
            }
            else {
                callback.onGetContactsSucceeded(getContactsSchemes());
            }
        }
    }

    // endregion helper classes
}