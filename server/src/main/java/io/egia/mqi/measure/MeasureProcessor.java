package io.egia.mqi.measure;

import io.egia.mqi.patient.Patient;
import io.egia.mqi.visit.Visit;

import java.util.List;

public interface MeasureProcessor {
    void setChunkId(Long chunkId);
    void setMeasures(List<Measure> measures);
    void setPatientData(List<Patient> patients, List<Visit> visits);
    void process();
    void clear();
    void setRules(Rules rules);
}
