package io.egia.mqi;

import io.egia.mqi.chunk.ChunkRepo;
import io.egia.mqi.job.JobRepo;
import io.egia.mqi.measure.MeasureResultRepo;
import io.egia.mqi.patient.Patient;
import io.egia.mqi.patient.PatientMeasureLogRepo;
import io.egia.mqi.patient.PatientRepo;
import io.egia.mqi.visit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static io.egia.mqi.visit.CodeSystem.*;

@RestController
public class SeedDdController {

    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private VisitRepo visitRepo;
    @Autowired
    private VisitCodeRepo visitCodeRepo;
    @Autowired
    private ChunkRepo chunkRepo;
    @Autowired
    private PatientMeasureLogRepo patientMeasureLogRepo;
    @Autowired
    private CodeSetRepo codeSetRepo;
    @Autowired
    private CodeSetGroupRepo codeSetGroupRepo;
    @Autowired
    private MeasureResultRepo measureResultRepo;
    @Autowired
    private JobRepo jobRepo;

    @GetMapping("/seed")
    public Map<String, Integer> seedDb() {

        chunkRepo.deleteAll();
        visitCodeRepo.deleteAll();
        visitRepo.deleteAll();
        patientRepo.deleteAll();
        codeSetRepo.deleteAll();
        codeSetGroupRepo.deleteAll();
        patientMeasureLogRepo.deleteAll();
        measureResultRepo.deleteAll();
        jobRepo.deleteAll();

        for (long i = 1L; i <= 500; i++) {
            Patient patient = new Patient();
            patient.setFirstName("Vango");
            patient.setLastName("Laouto");
            patient.setGender('M');
            patient.setDateOfBirth(new GregorianCalendar(1956, Calendar.APRIL, 28).getTime());
            Patient savedPatient = patientRepo.saveAndFlush(patient);

            Visit visit = new Visit();
            visit.setPatientId(savedPatient.getPatientId());
            Visit savedVisit = visitRepo.saveAndFlush(visit);

            VisitCode code_1 = new VisitCode();
            code_1.setVisitId(savedVisit.getVisitId());
            code_1.setCodeValue("xyz");
            code_1.setCodeSystem(ICD_9);
            visitCodeRepo.saveAndFlush(code_1);

            VisitCode code_2 = new VisitCode();
            code_2.setVisitId(savedVisit.getVisitId());
            code_2.setCodeValue("abc.defgh");
            code_2.setCodeSystem(ICD_10);
            visitCodeRepo.saveAndFlush(code_2);

            VisitCode code_3 = new VisitCode();
            code_3.setVisitId(savedVisit.getVisitId());
            code_3.setCodeValue("99");
            code_3.setCodeSystem(POS);
            visitCodeRepo.saveAndFlush(code_3);

            VisitCode code_4 = new VisitCode();
            code_4.setVisitId(savedVisit.getVisitId());
            code_4.setCodeValue("22");
            code_4.setCodeSystem(REV);
            visitCodeRepo.saveAndFlush(code_4);
        }

        CodeSetGroup codeSetA = codeSetGroupRepo.save(CodeSetGroup.builder().groupName("MENTAL HEALTH").build());
        CodeSetGroup codeSetB = codeSetGroupRepo.save(CodeSetGroup.builder().groupName("BIKE ACCIDENT").build());

        codeSetRepo.save(buildCodeSet(codeSetA, POS, "99"));
        codeSetRepo.save(buildCodeSet(codeSetA, REV, "22"));
        codeSetRepo.save(buildCodeSet(codeSetB, ICD_9, "xyz"));
        codeSetRepo.save(buildCodeSet(codeSetB, ICD_10, "abc.defgh"));

        Map<String, Integer> results = new HashMap<>();
        results.put("Code Set Groups:", codeSetGroupRepo.findAll().size());
        results.put("Code Set Codes:", codeSetRepo.findAll().size());
        results.put("Visit Codes:", visitCodeRepo.findAll().size());
        results.put("Visits:", visitRepo.findAll().size());
        results.put("Patients:", patientRepo.findAll().size());
        return results;
    }

    private CodeSet buildCodeSet(CodeSetGroup codeSetGroup, CodeSystem codeSystem, String codeValue) {
        return CodeSet.builder().codeSetGroup(codeSetGroup).codeSystem(codeSystem).codeValue(codeValue).build();
    }
}
