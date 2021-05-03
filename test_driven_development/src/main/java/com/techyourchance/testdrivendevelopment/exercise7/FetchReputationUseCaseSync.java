package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointResult;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus;

public class FetchReputationUseCaseSync {

    private final GetReputationHttpEndpointSync getReputationHttpEndpointSync;

    public FetchReputationUseCaseSync(GetReputationHttpEndpointSync getReputationHttpEndpointSync) {
        this.getReputationHttpEndpointSync = getReputationHttpEndpointSync;
    }

    public EndpointResult fetchReputation() {
        EndpointResult result = getReputationHttpEndpointSync.getReputationSync();
        switch (result.getStatus()) {
            case SUCCESS:
                return new EndpointResult(EndpointStatus.SUCCESS, result.getReputation());
            case NETWORK_ERROR:
                return new EndpointResult(EndpointStatus.NETWORK_ERROR, 0);
            case GENERAL_ERROR:
                return new EndpointResult(EndpointStatus.GENERAL_ERROR, 0);
            default:
                throw new RuntimeException("Invalid result status: " + result.getStatus());
        }
    }
}
