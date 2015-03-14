package com.kritsit.casetracker.server.datalayer;

import com.kritsit.casetracker.shared.domain.model.Case;
import com.kritsit.casetracker.shared.domain.model.Defendant;
import com.kritsit.casetracker.shared.domain.model.Evidence;
import com.kritsit.casetracker.shared.domain.model.Incident;
import com.kritsit.casetracker.shared.domain.model.Person;
import com.kritsit.casetracker.shared.domain.model.Staff;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CaseRepository implements ICaseRepository {
    private final Logger logger = LoggerFactory.getLogger(CaseRepository.class);	
	private final IPersistenceService db;
	private final IIncidentRepository incidentRepo;
	private final IPersonRepository personRepo;
	private final IUserRepository userRepo;
    private final IEvidenceRepository evidenceRepo;
	
	public CaseRepository(IPersistenceService db, IIncidentRepository incidentRepo, IPersonRepository personRepo, IUserRepository userRepo, IEvidenceRepository evidenceRepo) {
		this.db = db;
        this.incidentRepo = incidentRepo;
        this.personRepo = personRepo;
        this.userRepo = userRepo;
        this.evidenceRepo = evidenceRepo;
	}

    public List<Case> getCases() throws RowToModelParseException {
        try {
            logger.info("Fetching cases");
            String sql = "SELECT caseNumber, reference, caseType, details, animalsInvolved, nextCourtDate, outcome, returnVisit, returnDate FROM cases;";
            List<Map<String, String>> rs = db.executeQuery(sql);

            if(rs == null || rs.size() == 0) {
                logger.debug("No cases found");
                return null;
            }
           
            List<Case> cases = new ArrayList<>();
            for (Map<String, String> line : rs) {
                Case c = parseCase(line); 
                cases.add(c);
            }
            return cases;
        } catch(Exception e){
            logger.error("Error retrieving cases", e);
            throw new RowToModelParseException("Error retrieving cases from database");
        }
    }

    public List<Case> getCases(Staff inspector) throws RowToModelParseException {
        try {
            /*
            logger.info("Fetching defendant for case {}", caseNumber);
            String sql = "SELECT id, firstName, lastName, address, telephoneNumber, emailAddress, secondOffence FROM defendants INNER JOIN(cases) WHERE defendants.indexId=cases.defendantId AND cases.caseNumber=\'" + caseNumber + "\';";
            List<Map<String, String>> rs = db.executeQuery(sql);

            if(rs == null || rs.size() == 0) {
                logger.debug("No defendants found for case {}", caseNumber);
                return null;
            }
            
            String id = rs.get(0).get("id");
            String firstName = rs.get(0).get("firstName");
            String lastName = rs.get(0).get("lastName");
            String address = rs.get(0).get("address");
            String telephoneNumber = rs.get(0).get("telephoneNumber");
            String emailAddress = rs.get(0).get("emailAddress");
            boolean isSecondOffence = Boolean.parseBoolean(rs.get(0).get("secondOffence"));
            
            Defendant defendant = new Defendant(id, firstName, lastName, address, telephoneNumber, emailAddress, isSecondOffence);
            return defendant;
            */ return null;
        } catch(Exception e){
            logger.error("Error retrieving defendant for case}", e);
            throw new RowToModelParseException("Error retrieving defendant from database for case number: ");
        }
    }

    private Case parseCase(Map<String, String> row) throws RowToModelParseException {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String caseNumber = row.get("caseNumber");
            String reference = row.get("reference");
            String details = row.get("details");
            String animalsInvolved = row.get("animalsInvolved");
            String caseType = row.get("caseType");
            Date nextCourtDate = null;
            if (row.get("nextCourtDate") != null) {
                nextCourtDate = df.parse(row.get("nextCourtDate"));
            }
            String outcome = row.get("outcome");
            boolean isReturnVisit = Boolean.parseBoolean(row.get("returnVisit"));
            Date returnDate = null;
            if (isReturnVisit) {
                returnDate = df.parse(row.get("returnDate"));
            }
            Incident incident = incidentRepo.getIncident(caseNumber);
            Defendant defendant = personRepo.getDefendant(caseNumber);
            Person complainant = personRepo.getComplainant(caseNumber);
            Staff investigatingOfficer = userRepo.getInvestigatingOfficer(caseNumber);
            List<Evidence> evidence = evidenceRepo.getEvidence(caseNumber);

            return new Case(caseNumber, reference, details, animalsInvolved, investigatingOfficer, incident, defendant, complainant, nextCourtDate, evidence, isReturnVisit, returnDate, caseType, outcome);
        } catch (Exception e) {
            logger.error("Error retrieving case", e);
            throw new RowToModelParseException("Error retrieving case from database");
        }
    }
}
