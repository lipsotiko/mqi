package io.egia.mqi.measure;

import io.egia.mqi.patient.*;
import io.egia.mqi.visit.Visit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Component
public class MeasureProcessorImpl implements MeasureProcessor {
    private Logger log = LoggerFactory.getLogger(MeasureProcessorImpl.class);
    private List<Measure> measures;
    private Hashtable<Long, PatientData> patientDataHash = new Hashtable<>();
    private int rulesEvaluatedCount;
    private List<MeasureResult> measureResults = new ArrayList<>();
    private PatientMeasureLogRepo patientMeasureLogRepo;

    MeasureProcessorImpl(PatientMeasureLogRepo patientMeasureLogRepo) {
        this.patientMeasureLogRepo = patientMeasureLogRepo;
    }

    @Override
    public void process (List<Measure> measures,
                         List<Patient> patients,
                         List<Visit> visits,
                         MeasureMetaData measureMetaData,
                         ZonedDateTime timeExecuted) {
        this.measures = measures;
        appendToPatientDataHash(patients);
        appendToPatientDataHash(visits);
        this.patientDataHash.forEach((patientId, patientData) ->
                this.measures.forEach((measure) -> {
                    log.debug(String.format("Processing patient id: %s, measure: %s",
                            patientId,
                            measure.getMeasureName()));
                    try {
                        patientMeasureLogRepo.deleteByPatientIdAndMeasureId(patientId, measure.getMeasureId());
                        evaluatePatientDataByMeasure(patientData, measure, measureMetaData);
                        patientMeasureLogRepo.save(
                                PatientMeasureLog.builder()
                                        .patientId(patientId)
                                        .measureId(measure.getMeasureId())
                                        .lastUpdated(timeExecuted)
                                        .build()
                        );
                    } catch (MeasureProcessorException e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public void clear() {
        this.patientDataHash.clear();
        this.rulesEvaluatedCount = 0;
        this.measureResults.clear();
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
        MeasureStepper measureStepper = new MeasureStepper(patientData, measure, new MeasureResult(), measureMetaData);
        measureStepper.stepThroughMeasure();
        rulesEvaluatedCount = rulesEvaluatedCount + measureStepper.getRulesEvaluatedCount();
        this.measureResults.add(measureStepper.getMeasureResult());
    }


    Hashtable<Long, PatientData> getPatientDataHash() {
        return this.patientDataHash;
    }

    int getRulesEvaluatedCount() {
        return rulesEvaluatedCount;
    }

    List<MeasureResult> getMeasureResults() {
        return measureResults;
    }
}
