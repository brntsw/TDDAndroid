package com.techyourchance.testdrivendevelopment.exercise7;

import com.techyourchance.testdrivendevelopment.example9.networking.NetworkErrorException;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointResult;
import com.techyourchance.testdrivendevelopment.exercise7.networking.GetReputationHttpEndpointSync.EndpointStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FetchReputationUseCaseSyncTest {
    // region constants

    final int REPUTATION = 1;

    //endregion constants

    // region helper fields
    @Mock
    GetReputationHttpEndpointSync getReputationHttpEndpointSyncMock;
    //endregion helper fields

    FetchReputationUseCaseSync SUT;

    @Before
    public void setup() throws Exception {
        SUT = new FetchReputationUseCaseSync(getReputationHttpEndpointSyncMock);
        success();
    }

    @Test
    public void fetchReputation_endpointsuccess_successReturned() {
        // Arrange
        // Act
        EndpointResult result = SUT.fetchReputation();
        // Assert
        assertThat(result.getStatus(), is(EndpointStatus.SUCCESS));
    }
    
    @Test
    public void fetchReputation_endpointsuccess_reputationReturned() {
        // Arrange
        // Act
        EndpointResult result = SUT.fetchReputation();
        // Assert
        assertThat(result.getReputation(), is(REPUTATION));
    }

    @Test
    public void fetchReputation_endpointGeneralError_generalErrorReturned() {
        // Arrange
        generalError();
        // Act
        EndpointResult result = SUT.fetchReputation();
        // Assert
        assertThat(result.getStatus(), is(EndpointStatus.GENERAL_ERROR));
    }

    @Test
    public void fetchReputation_endpointGeneralError_reputationZeroReturned() {
        // Arrange
        generalError();
        // Act
        EndpointResult result = SUT.fetchReputation();
        // Assert
        assertThat(result.getReputation(), is(0));
    }

    @Test
    public void fetchReputation_endpointNetworkError_networkErrorReturned() {
        // Arrange
        networkError();
        // Act
        EndpointResult result = SUT.fetchReputation();
        // Assert
        assertThat(result.getStatus(), is(EndpointStatus.NETWORK_ERROR));
    }

    @Test
    public void fetchReputation_endpointNetworkError_reputationZeroReturned() {
        // Arrange
        networkError();
        // Act
        EndpointResult result = SUT.fetchReputation();
        // Assert
        assertThat(result.getReputation(), is(0));
    }


    // region helper methods

    private void success() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new EndpointResult(EndpointStatus.SUCCESS, REPUTATION));
    }

    private void generalError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new EndpointResult(EndpointStatus.GENERAL_ERROR, 0));
    }

    private void networkError() {
        when(getReputationHttpEndpointSyncMock.getReputationSync())
                .thenReturn(new EndpointResult(EndpointStatus.NETWORK_ERROR, 0));
    }

    // endregion helper methods

    // region helper classes

    // endregion helper classes
}