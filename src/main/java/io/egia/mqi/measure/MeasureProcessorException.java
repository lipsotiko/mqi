package io.egia.mqi.measure;

public class MeasureProcessorException extends Exception {

    MeasureProcessorException(String msg) {
        super(msg);
        System.err.println("Error: " + msg);
    }
}