package io.egia.mqi.measure;

import io.egia.mqi.job.Job;
import io.egia.mqi.job.JobRepo;
import io.egia.mqi.job.JobStatus;
import io.egia.mqi.patient.Patient;
import io.egia.mqi.patient.PatientData;
import io.egia.mqi.patient.PatientMeasureLog;
import io.egia.mqi.patient.PatientRecordInterface;
import io.egia.mqi.visit.Visit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.*;

import static io.egia.mqi.job.JobStatus.FAILURE;

@Component
public class MeasureProcessor implements Processor {
    private Logger log = LoggerFactory.getLogger(MeasureProcessor.class);
    private List<Measure> measures;
    private Hashtable<Long, PatientData> patientDataHash = new Hashtable<>();
    private int rulesEvaluatedCount;
    private List<MeasureWorkspace> measureWorkspaces = new ArrayList<>();
    private JobRepo jobRepo;

    MeasureProcessor(JobRepo jobRepo) {
        this.jobRepo = jobRepo;
    }

    @Override
    public void process(List<Measure> measures,
                        List<Patient> patients,
                        List<Visit> visits,
                        MeasureMetaData measureMetaData,
                        ZonedDateTime timeExecuted, Long jobId) {
        this.measures = measures;
        appendToPatientDataHash(patients);
        appendToPatientDataHash(visits);
        for (Map.Entry<Long, PatientData> entry : this.patientDataHash.entrySet()) {
            Long patientId = entry.getKey();
            PatientData patientData = entry.getValue();
            for (Measure measure : this.measures) {
                log.debug(String.format("Processing patient id: %s, measure: %s",
                        patientId,
                        measure.getMeasureName()));
                try {
                    evaluatePatientDataByMeasure(patientData, measure, measureMetaData);
                } catch (MeasureProcessorException e) {
                    Optional<Job> optionalJob = jobRepo.findById(jobId);
                    optionalJob.ifPresent(job -> {
                        job.setJobStatus(FAILURE);
                        jobRepo.save(job);
                    });
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @Override
    public void clear() {
        this.patientDataHash.clear();
        this.rulesEvaluatedCount = 0;
        this.measureWorkspaces.clear();
    }

    @Override
    public List<PatientMeasureLog> getLog() {
        List<PatientMeasureLog> log = new ArrayList<>();
        measures.forEach(m ->
                patientDataHash.forEach((patientId, data) ->
                        log.add(PatientMeasureLog.builder()
                                .measureId(m.getMeasureId())
                                .patientId(patientId)
                                .lastUpdated(ZonedDateTime.now())
                                .build())
                )
        );

        return log;
    }

    @Override
    public List<MeasureResult> getResults() {
        List<MeasureResult> results = new ArrayList<>();
        measureWorkspaces.stream()
                .filter(mw -> mw.getResultCode() != null)
                .forEach(mw -> results.add(MeasureResult.builder()
                        .patientId(mw.getPatientId())
                        .measureId(mw.getMeasureId())
                        .resultCode(mw.getResultCode())
                        .build())
                );

        return results;
    }

    private <T extends PatientRecordInterface> void appendToPatientDataHash(List<T> patientRecords) {
        for (T patientRecord : patientRecords) {
            PatientData patientData = this.patientDataHash.get(patientRecord.getPatientId());
            if (patientData == null) {
                patientData = new PatientData(patientRecord.getPatientId());
            }

            patientRecord.updatePatientData(patientData);
            this.patientDataHash.put(patientRecord.getPatientId(), patientData);
        }
    }

    private void evaluatePatientDataByMeasure(PatientData patientData, Measure measure, MeasureMetaData measureMetaData)
            throws MeasureProcessorException {

        if (measure.getMeasureLogic() == null) {
            throw new MeasureProcessorException("No logic was Supplied for measure: " + measure.getMeasureName());
        }

        MeasureWorkspace measureWorkspace = new MeasureWorkspace(patientData.getPatientId(), measure.getMeasureId());
        MeasureStepper measureStepper = new MeasureStepper(patientData, measure, measureWorkspace, measureMetaData);
        rulesEvaluatedCount = measureStepper.stepThroughMeasure() + rulesEvaluatedCount;
        this.measureWorkspaces.add(measureStepper.getMeasureWorkspace());
    }

    Hashtable<Long, PatientData> getPatientDataHash() {
        return this.patientDataHash;
    }

    int getRulesEvaluatedCount() {
        return rulesEvaluatedCount;
    }

}