package io.egia.mqi.job;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JobMeasure {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long jobMeasureId;
	private Long jobId;
	private Long measureId;
	
	public Long getJobMeasureId() {
		return jobMeasureId;
	}
	public void setJobMeasureId(Long jobMeasureId) {
		this.jobMeasureId = jobMeasureId;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public Long getMeasureId() {
		return measureId;
	}
	public void setMeasureId(Long measureId) {
		this.measureId = measureId;
	}
}