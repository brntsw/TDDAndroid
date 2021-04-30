package com.techyourchance.testdrivendevelopment.exercise6;

import com.techyourchance.testdrivendevelopment.exercise6.networking.FetchUserHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise6.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise6.users.User;
import com.techyourchance.testdrivendevelopment.exercise6.users.UsersCache;

public class FetchUserImplUseCaseSync implements FetchUserUseCaseSync {

    private FetchUserHttpEndpointSync mFetchUserHttpEndpointSync;
    private UsersCache usersCache;

    public FetchUserImplUseCaseSync(FetchUserHttpEndpointSync mFetchUserHttpEndpointSync, UsersCache usersCache) {
        this.mFetchUserHttpEndpointSync = mFetchUserHttpEndpointSync;
        this.usersCache = usersCache;
    }

    @Override
    public UseCaseResult fetchUserSync(String userId) {
        FetchUserHttpEndpointSync.EndpointResult result;

        try {
            if(usersCache.getUser(userId) == null)
                result = mFetchUserHttpEndpointSync.fetchUserSync(userId);
            else
                return new UseCaseResult(Status.SUCCESS, usersCache.getUser(userId));
        } catch (NetworkErrorException e) {
            return new UseCaseResult(Status.NETWORK_ERROR, null);
        }

        switch (result.getStatus()) {
            case SUCCESS:
                User user = new User(result.getUserId(), result.getUsername());
                usersCache.cacheUser(user);
                return new UseCaseResult(Status.SUCCESS, user);
            case AUTH_ERROR:
            case GENERAL_ERROR:
                return new UseCaseResult(Status.FAILURE, null);
            default:
                throw new RuntimeException("Invalid status: " + result.getStatus());
        }
    }
}
