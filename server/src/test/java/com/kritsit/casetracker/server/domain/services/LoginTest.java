package com.kritsit.casetracker.server.domain.services;

import com.kritsit.casetracker.server.datalayer.IPersistenceService;
import com.kritsit.casetracker.server.datalayer.IUserRepository;
import com.kritsit.casetracker.server.datalayer.RowToModelParseException;
import com.kritsit.casetracker.server.datalayer.UserRepository;
import com.kritsit.casetracker.server.domain.Domain;
import com.kritsit.casetracker.server.domain.model.AuthenticationException;
import com.kritsit.casetracker.shared.domain.model.Staff;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LoginTest extends TestCase {
    IPersistenceService persistence;
    ILoginService login;

    public LoginTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(LoginTest.class);
    }

    public void setUp() {
        persistence = Domain.getPersistenceService();
        IUserRepository repo = new UserRepository(persistence);
        login = new Login(repo);
    }

    public void testCreation() {
        assertTrue(login instanceof ILoginService);
    }

    public void testLoginAttempt_IncorrectUser() {
    	try {
		    int password = "inspector".hashCode();
		    String username = "wrongInspector";
		    Staff succeeded = login.login(username, password);
    	}
    	catch(RowToModelParseException | AuthenticationException e){}
    }

    public void testLoginAttempt_IncorrectPassword() {
    	try {
	        int password = "wrong inspector".hashCode();
	        String username = "inspector";
	        Staff succeeded = login.login(username, password);
	        fail("Exception was not thrown");
        }
    	catch(RowToModelParseException | AuthenticationException e){}
    }

    public void testLoginAttempt_Succeeded() throws RowToModelParseException, AuthenticationException {
        int password = "inspector".hashCode();
        String username = "inspector";
        Staff succeeded = login.login(username, password);
        assertTrue(succeeded != null);
    }
    
    public void tearDown() {
        persistence.close();
        Domain.resetPersistenceService();
    }
}
