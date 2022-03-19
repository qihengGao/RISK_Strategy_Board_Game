package edu.duke.ece651.risk.server;

import edu.duke.ece651.risk.shared.RiskGameMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientTest {

    @Test
    void readObject() throws IOException, ClassNotFoundException {
        Client client = mock(Client.class);
        doCallRealMethod().when(client).readObject();
        doCallRealMethod().when(client).setOis(any());
        ObjectInput objectInput = mock(ObjectInput.class);
        doThrow(IOException.class).when(objectInput).readObject();
        client.setOis(objectInput);
        client.readObject();
    }

    @Test
    void writeObject() throws IOException {
        Client client = mock(Client.class);
        doCallRealMethod().when(client).writeObject(any());
        doCallRealMethod().when(client).setOos(any());
        ObjectOutputStream objectInput = mock(ObjectOutputStream.class);
        doThrow(IOException.class).when(objectInput).reset();
        client.setOos(objectInput);
        RiskGameMessage riskGameMessage = mock(RiskGameMessage.class);
        client.writeObject(riskGameMessage);
    }
}