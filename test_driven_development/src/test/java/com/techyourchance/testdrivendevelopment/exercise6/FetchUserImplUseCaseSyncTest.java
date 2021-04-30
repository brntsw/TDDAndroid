package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.Status;
import static com.techyourchance.testdrivendevelopment.exercise6.FetchUserUseCaseSync.UseCaseResult;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FetchUserImplUseCaseSyncTest {
    // region constants

    private static final String USER_ID = "userId";
    private static final String USERNAME = "username";
    private static final User USER = new User(USER_ID, USERNAME);

    //endregion constants

    // region helper fields

    FetchUserHttpEndpointSyncTestDouble fetchUserHttpEndpointSyncTestDouble;

    @Mock
    UsersCache mUsersCacheMock;

    //endregion helper fields

    FetchUserImplUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        fetchUserHttpEndpointSyncTestDouble = new FetchUserHttpEndpointSyncTestDouble();

        SUT = new FetchUserImplUseCaseSync(fetchUserHttpEndpointSyncTestDouble, mUsersCacheMock);
    }

    @Test
    public void fetchUser_correctParametersPassedToEndpoint() throws NetworkErrorException {
        // Arrange
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(fetchUserHttpEndpointSyncTestDouble.mUserId, is(USER_ID));
    }

    @Test
    public void fetchUser_success_successReturned() {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(Status.SUCCESS));
    }

    @Test
    public void fetchUser_notInCacheEndpointSuccess_correctUserReturned() {
        // Arrange
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(USER));
    }

    @Test
    public void fetchUser_notInCacheEndpointSuccess_userCached() {
        // Arrange
        ArgumentCaptor<User> ac = ArgumentCaptor.forClass(User.class);
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock).cacheUser(ac.capture());
        assertThat(ac.getValue(), is(USER));
    }

    @Test
    public void fetchUser_notInCacheEndpointAuthError_failureReturned() {
        // Arrange
        endpointAuthError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(Status.FAILURE));
    }

    @Test
    public void fetchUser_notInCacheEndpointAuthError_nullUserReturned() {
        // Arrange
        endpointAuthError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), nullValue());
    }

    @Test
    public void fetchUser_notInCacheEndpointAuthError_nothingCached() {
        // Arrange
        endpointAuthError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUser_notInCacheEndpointServerError_failureReturned() {
        // Arrange
        endpointServerError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(Status.FAILURE));
    }

    @Test
    public void fetchUser_notInCacheEndpointServerError_nullUserReturned() {
        // Arrange
        endpointServerError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), nullValue());
    }

    @Test
    public void fetchUser_notInCacheEndpointServerError_nothingCached() {
        // Arrange
        endpointServerError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUser_notInCacheEndpointNetworkError_networkErrorReturned() {
        // Arrange
        endpointNetworkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(Status.NETWORK_ERROR));
    }

    @Test
    public void fetchUser_notInCacheEndpointNetworkError_nullUserReturned() {
        // Arrange
        endpointNetworkError();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), nullValue());
    }

    @Test
    public void fetchUser_notInCacheEndpointNetworkError_nothingCached() {
        // Arrange
        endpointNetworkError();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock, never()).cacheUser(any(User.class));
    }

    @Test
    public void fetchUser_correctUserIdParameterCache() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        verify(mUsersCacheMock).getUser(ac.capture());
        assertThat(ac.getValue(), is(USER_ID));
    }

    @Test
    public void fetchUser_userInCache_successReturned() {
        // Arrange
        userInCache();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getStatus(), is(Status.SUCCESS));
    }

    @Test
    public void fetchUser_userInCache_cachedUserReturned() {
        // Arrange
        userInCache();
        // Act
        UseCaseResult result = SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(result.getUser(), is(USER));
    }

    @Test
    public void fetchUser_userInCache_endpointNotPolled() {
        // Arrange
        userInCache();
        // Act
        SUT.fetchUserSync(USER_ID);
        // Assert
        assertThat(fetchUserHttpEndpointSyncTestDouble.mRequestCount, is(0));
    }

    // region helper methods

    private void endpointAuthError() {
        fetchUserHttpEndpointSyncTestDouble.mAuthError = true;
    }

    private void endpointServerError() {
        fetchUserHttpEndpointSyncTestDouble.mServerError = true;
    }

    private void endpointNetworkError() {
        fetchUserHttpEndpointSyncTestDouble.mNetworkError = true;
    }

    private void userInCache() {
        when(mUsersCacheMock.getUser(anyString())).thenReturn(USER);
    }

    // endregion helper methods

    // region helper classes

    private class FetchUserHttpEndpointSyncTestDouble implements FetchUserHttpEndpointSync {

        public boolean mAuthError;
        public boolean mServerError;
        public boolean mNetworkError;
        public int mRequestCount;
        private String mUserId = "";

        @Override
        public EndpointResult fetchUserSync(String userId) throws NetworkErrorException {
            mUserId = userId;
            mRequestCount++;

            if(mAuthError) {
                return new EndpointResult(EndpointStatus.AUTH_ERROR, "", "");
            }
            else if(mServerError) {
                return new EndpointResult(EndpointStatus.GENERAL_ERROR, "", "");
            }
            else if(mNetworkError) {
                throw new NetworkErrorException();
            }
            else {
                return new EndpointResult(EndpointStatus.SUCCESS, USER_ID, USERNAME);
            }
        }
    }

    // endregion helper classes
}