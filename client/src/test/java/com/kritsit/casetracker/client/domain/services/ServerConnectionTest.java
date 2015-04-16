package com.kritsit.casetracker.client.domain.services;

import com.kritsit.casetracker.client.domain.Domain;
import com.kritsit.casetracker.shared.domain.model.Case;
import com.kritsit.casetracker.shared.domain.model.Permission;
import com.kritsit.casetracker.shared.domain.model.Staff;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;

public class ServerConnectionTest extends TestCase {
    IConnectionService connection;
    Process server;

    public ServerConnectionTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ServerConnectionTest.class);
    }

    public void setUp() {
        connection = Domain.getServerConnection();
    }

    public void testConnection_PortOutOfBounds() {
        try {
        connection.open("localhost", 1244);
            connection.open("localhost", 65555);
        } catch (IllegalArgumentException ex) {
            assertTrue("Port must be in range".equals(ex.getMessage()));
        }
    }

    public void testConnection_UnknownHostException() {
        try {
            connection.open("ThisIsNotAValidHost", 1244);
        } catch (IllegalArgumentException ex) {
            assertTrue("Host not found".equals(ex.getMessage()));
        }
    }

    public void testConnection_Succeed() {
        assertTrue(connection.open("localhost", 1244));
    }

    public void testLogin_Correct() {
        connection.open("localhost", 1244);
        assertTrue(connection.login("inspector", "inspector".hashCode()));
    }

    public void testGetUser() {
        connection.open("localhost", 1244);
        assertTrue(connection.getUser("inspector", "inspector".hashCode()) != null);
    }

    public void testGetCases_NoUser() {
        connection.open("localhost", 1244);
        List<Case> caseList = connection.getCases(null);
        assertTrue(caseList != null);
    }

    public void testGetCases_User() {
        connection.open("localhost", 1244);
        Staff user = new Staff("inspector", "test", "inspector", "department", "position", Permission.EDITOR);
        List<Case> caseList = connection.getCases(user);
        assertTrue(caseList != null);
    }

    public void tearDown() throws IOException {
        Domain.resetServerConnection();
        if (connection.isOpen()) {
            connection.close();
        }
    }
}
