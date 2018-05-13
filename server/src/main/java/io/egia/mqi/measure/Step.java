package io.egia.mqi.measure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Step {
    private int stepId;
    private String rule;
    private Map<String, String> parameters;
    private int success;
    private int failure;
}