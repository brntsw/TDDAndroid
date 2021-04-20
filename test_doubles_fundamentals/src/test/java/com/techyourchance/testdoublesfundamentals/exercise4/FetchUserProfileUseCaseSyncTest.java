package com.techyourchance.testdoublesfundamentals.exercise4;

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException;
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync;
import com.techyourchance.testdoublesfundamentals.exercise4.users.User;
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache;
import com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.UseCaseResult;

import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FetchUserProfileUseCaseSyncTest {

    public static final String USERID = "userId";
    public static final String FULLNAME = "fullname";
    public static final String IMAGEURL = "imageurl";

    UserProfileHttpEndpointSyncTd userProfileHttpEndpointSyncTd;
    UsersCacheTd usersCacheTd;

    private FetchUserProfileUseCaseSync SUT;

    @Before
    public void setUp() throws Exception {
        userProfileHttpEndpointSyncTd = new UserProfileHttpEndpointSyncTd();
        usersCacheTd = new UsersCacheTd();
        SUT = new FetchUserProfileUseCaseSync(userProfileHttpEndpointSyncTd, usersCacheTd);
    }

    @Test
    public void fetchUser_success_userIdPassedToEndpoint() {
        SUT.fetchUserProfileSync(USERID);
        assertThat(userProfileHttpEndpointSyncTd.mUserId, is(USERID));
    }

    @Test
    public void fetchUser_success_userCached() {
        SUT.fetchUserProfileSync(USERID);
        User userCached = usersCacheTd.getUser(USERID);
        assertThat(userCached.getUserId(), is(USERID));
        assertThat(userCached.getFullName(), is(FULLNAME));
        assertThat(userCached.getImageUrl(), is(IMAGEURL));
    }

    @Test
    public void fetchUser_generalError_userNotCached() {
        userProfileHttpEndpointSyncTd.mIsGeneralError = true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(usersCacheTd.getUser(USERID), is(nullValue()));
    }

    @Test
    public void fetchUser_authError_userNotCached() {
        userProfileHttpEndpointSyncTd.mIsAuthError = true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(usersCacheTd.getUser(USERID), is(nullValue()));
    }

    @Test
    public void fetchUser_serverError_userNotCached() {
        userProfileHttpEndpointSyncTd.mIsServerError = true;
        SUT.fetchUserProfileSync(USERID);
        assertThat(usersCacheTd.getUser(USERID), is(nullValue()));
    }

    @Test
    public void fetchUser_success_successReturned() {
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.SUCCESS));
    }

    @Test
    public void fetchUser_serverError_errorReturned() {
        userProfileHttpEndpointSyncTd.mIsServerError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUser_authError_errorReturned() {
        userProfileHttpEndpointSyncTd.mIsAuthError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUser_generalError_errorReturned() {
        userProfileHttpEndpointSyncTd.mIsGeneralError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.FAILURE));
    }

    @Test
    public void fetchUser_networkError_networkErrorReturned() {
        userProfileHttpEndpointSyncTd.mIsNetworkError = true;
        UseCaseResult result = SUT.fetchUserProfileSync(USERID);
        assertThat(result, is(UseCaseResult.NETWORK_ERROR));
    }

    private static class UserProfileHttpEndpointSyncTd implements UserProfileHttpEndpointSync {
        public String mUserId = "";
        public boolean mIsGeneralError;
        public boolean mIsAuthError;
        public boolean mIsServerError;
        public boolean mIsNetworkError;

        @Override
        public EndpointResult getUserProfile(String userId) throws NetworkErrorException {
            mUserId = userId;
            if(mIsGeneralError) {
                return new EndpointResult(EndpointResultStatus.GENERAL_ERROR, "", "", "");
            }
            else if(mIsAuthError) {
                return new EndpointResult(EndpointResultStatus.AUTH_ERROR, "", "","");
            }
            else if(mIsServerError) {
                return new EndpointResult(EndpointResultStatus.SERVER_ERROR, "", "", "");
            }
            else if(mIsNetworkError) {
                throw new NetworkErrorException();
            }

            return new EndpointResult(EndpointResultStatus.SUCCESS, USERID, FULLNAME, IMAGEURL);
        }
    }

    private static class UsersCacheTd implements UsersCache {
        private List<User> mUsers = new ArrayList<>(1);

        @Override
        public void cacheUser(User user) {
            User existingUser = getUser(user.getUserId());
            if (existingUser != null) {
                mUsers.remove(existingUser);
            }
            mUsers.add(user);
        }

        @Nullable
        @Override
        public User getUser(String userId) {
            for (User user : mUsers) {
                if (user.getUserId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }
    }
}