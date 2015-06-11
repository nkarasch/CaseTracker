package com.kritsit.casetracker.client.domain.services;

import java.util.ArrayList;
import java.util.List;

public class InputToModelParseResult {
    boolean isSuccessful;
    String reason;
    List<String> failedInputs;

    public InputToModelParseResult(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
        failedInputs = new ArrayList<>();
    }

    public InputToModelParseResult(boolean isSuccessful, String reason) {
        this.isSuccessful = isSuccessful;
        this.reason = reason;
        failedInputs = new ArrayList<>();
    }

    public void addFailedInput(String input) {
        setIsSuccessful(false);
        failedInputs.add(input);
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getReason() {
        if (isSuccessful()) {
            return "Case created successfully";
        }
        if (reason != null && !reason.isEmpty() && failedInputs.isEmpty()) {
            return reason;
        }
        if (failedInputs.isEmpty()) {
            return "Input failed to parse into model";
        }
        String message = failedInputs.get(0);
        for (int i = 1; i < failedInputs.size(); i++) {
            message += ", " + failedInputs.get(i);
        }
        int andIndex = message.lastIndexOf(',');
        if (andIndex != -1) {
            message = new StringBuilder(message).replace(andIndex, andIndex + 1, " and").toString();
        }
        message += " required";
        return message;
    }
}
