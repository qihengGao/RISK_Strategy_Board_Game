package edu.duke.ece651.risk.shared.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import org.junit.jupiter.api.Test;

public class SocketFactoryTest {
    
    @Test
    public void test_createSocket(){
        SocketFactory factory = new SocketFactory();
        assertInstanceOf(Socket.class, factory.createSocket());
    }

    @Test
    public void test_createSocketAddress(){
        SocketFactory factory = new SocketFactory();
        SocketAddress address = factory.createSocketAddress("127.0.0.1", 1234);
        String expected = "/127.0.0.1:1234";
        assertEquals(expected, address.toString());
    }

    // @Test
    // public void test_createObjectInputStream() throws IOException{
    //     SocketFactory factory = mock(SocketFactory.class);
    //     InputStream inputStream = mock(InputStream.class);
        
    //     doCallRealMethod().when(factory).createObjectInputStream(inputStream);
    //     factory.createObjectInputStream(inputStream);
    // }

    @Test
    public void test_createObjectOutputStream() throws IOException{
        SocketFactory factory = new SocketFactory();
        assertInstanceOf(ObjectOutputStream.class, factory.createObjectOutputStream(System.out));
    }
}
