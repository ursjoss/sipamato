package ch.difty.scipamato.core.sync.launcher;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link SyncJobResult} collects log messages for one or more job steps
 * and keeps track of the entire jobs result. If all steps succeed, the jobs
 * result is success. If only one step fails, the job fails.
 */
public class SyncJobResult {

    private final List<LogMessage> logMessages = new ArrayList<>();
    private       JobResult        result      = JobResult.UNKNOWN;

    public boolean isSuccessful() {
        return result == JobResult.SUCCESS;
    }

    public boolean isFailed() {
        return result == JobResult.FAILURE;
    }

    @NotNull
    public List<LogMessage> getMessages() {
        return new ArrayList<>(logMessages);
    }

    public void setSuccess(@NotNull final String msg) {
        if (result != JobResult.FAILURE)
            result = JobResult.SUCCESS;
        logMessages.add(new LogMessage(msg, MessageLevel.INFO));
    }

    public void setFailure(@NotNull final String msg) {
        result = JobResult.FAILURE;
        logMessages.add(new LogMessage(msg, MessageLevel.ERROR));
    }

    public void setWarning(@NotNull final String msg) {
        logMessages.add(new LogMessage(msg, MessageLevel.WARNING));
    }

    private enum JobResult {
        UNKNOWN,
        SUCCESS,
        FAILURE
    }

    public enum MessageLevel {
        INFO,
        WARNING,
        ERROR
    }

    @Data
    @AllArgsConstructor
    public static class LogMessage {
        @NotNull
        private String       message;
        @NotNull
        private MessageLevel messageLevel;
    }
}