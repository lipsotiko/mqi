package io.egia.mqi.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Job {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long jobId;
	private Status status;
	private Date startTime;
	private Date endTime;
	@Column(updatable=false,insertable=false) private Date lastUpdated;

	public enum Status {
		PENDING, RUNNING, SUCCESS, FAILURE
	}
}
