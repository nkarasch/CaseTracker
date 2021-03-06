package com.kritsit.casetracker.server.datalayer;

import com.kritsit.casetracker.shared.domain.model.Person;
import com.kritsit.casetracker.shared.domain.model.Defendant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonRepository implements IPersonRepository {
    private final Logger logger = LoggerFactory.getLogger(PersonRepository.class);	
	private final IPersistenceService db;
	
	public PersonRepository(IPersistenceService db){
		this.db = db;
	}

    public Person getComplainant(String caseNumber) throws RowToModelParseException {
        try {
            logger.info("Fetching complainant for case {}", caseNumber);
            String sql = "SELECT id, firstName, lastName, address, telephoneNumber, " + 
                "emailAddress FROM complainants INNER JOIN(cases) " + 
                "WHERE complainants.indexId=cases.complainantId AND cases.caseNumber=?;";
            List<Map<String, String>> rs = db.executeQuery(sql, caseNumber);

            if(rs == null || rs.isEmpty()) {
                logger.debug("No complainants found for case {}", caseNumber);
                return null;
            }
            
            String id = rs.get(0).get("id");
            String firstName = rs.get(0).get("firstName");
            String lastName = rs.get(0).get("lastName");
            String address = rs.get(0).get("address");
            String telephoneNumber = rs.get(0).get("telephoneNumber");
            String emailAddress = rs.get(0).get("emailAddress");
            
            Person complainant = new Person(id, firstName, lastName, address, 
                    telephoneNumber, emailAddress);
            return complainant;
        } catch(Exception e){
            logger.error("Error retrieving complainant for case {}", caseNumber, e);
            throw new RowToModelParseException("Error retrieving complainant from " +
                    "database for case number: " + caseNumber);
        }
    }

    public Defendant getDefendant(String caseNumber) throws RowToModelParseException {
        try {
            logger.info("Fetching defendant for case {}", caseNumber);
            String sql = "SELECT id, firstName, lastName, address, telephoneNumber, " +
                "emailAddress, secondOffence FROM defendants INNER JOIN(cases) " +
                "WHERE defendants.indexId=cases.defendantId AND cases.caseNumber=?;";
            List<Map<String, String>> rs = db.executeQuery(sql, caseNumber);

            if(rs == null || rs.isEmpty()) {
                logger.debug("No defendants found for case {}", caseNumber);
                return null;
            }
            
            String id = rs.get(0).get("id");
            String firstName = rs.get(0).get("firstName");
            String lastName = rs.get(0).get("lastName");
            String address = rs.get(0).get("address");
            String telephoneNumber = rs.get(0).get("telephoneNumber");
            String emailAddress = rs.get(0).get("emailAddress");
            boolean isSecondOffence = "1".equals(rs.get(0).get("secondOffence"));
            
            Defendant defendant = new Defendant(id, firstName, lastName, address, 
                    telephoneNumber, emailAddress, isSecondOffence);
            return defendant;
        } catch(Exception e){
            logger.error("Error retrieving defendant for case {}", caseNumber, e);
            throw new RowToModelParseException("Error retrieving defendant from " +
                    "database for case number: " + caseNumber);
        }
    }
    
    public void insertDefendant(Defendant defendant) throws RowToModelParseException{
        try{
            logger.info("Inserting defendant {}", defendant.getName());
            String isSecondOffence = (defendant.isSecondOffence()) ? "1" : "0";
            String sql = "INSERT INTO defendants VALUES (NULL, ?, ?, ?, ?, ?, " +
                "?, ?);";
            db.executeUpdate(sql, defendant.getId(), defendant.getFirstName(), 
                    defendant.getLastName(), defendant.getAddress(), 
                    defendant.getTelephoneNumber(), defendant.getEmailAddress(),
                    isSecondOffence);
                
        }
        catch(Exception e){
            logger.error("Error inserting defendant into the database", e);
            throw new RowToModelParseException("Error inserting values to database");
        }
    }
    
    public void insertComplainant(Person complainant) throws RowToModelParseException{
        try{
            logger.info("Inserting complainant {}", complainant.getName());
            String sql = "INSERT INTO complainants VALUES (NULL, ?, ?, ?, ?, ?, " +
                "?);";
            db.executeUpdate(sql, complainant.getId(), complainant.getFirstName(), 
                    complainant.getLastName(), complainant.getAddress(), 
                    complainant.getTelephoneNumber(), complainant.getEmailAddress());
        }
        catch(Exception e){
            logger.error("Error inserting complainant into the database", e);
            throw new RowToModelParseException("Error inserting values to database");
        }
    }
}
