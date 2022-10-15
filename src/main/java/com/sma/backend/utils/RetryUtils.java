package com.sma.backend.utils;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * This is common class for retry solution.
 *
 * @author : Mak Sophea
 * @since : 08/20/2021
 */
@Slf4j
public final class RetryUtils {

    private RetryUtils() {
        // Nothing process
    }

    public static Object withRetryObject(int maxTimes, long intervalWait, CallToRetryString call) throws Exception {
        validate(maxTimes, intervalWait);
        Exception thrown = null;
        for (int i = 0; i < maxTimes; i++) {
            try {
                return call.processWithString();
            }
            catch (Exception ex) {
                thrown = ex;
                log.info("withRetryObject() Encountered failure due to error {}, attempt retry {} of {}",
                        ex.getMessage(), (i + 1), maxTimes, ex);
            }
            try {
                Thread.sleep(intervalWait);
            }
            catch (InterruptedException wakeAndAbort) {
                log.error(wakeAndAbort.getMessage(), wakeAndAbort);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw thrown;
    }

    public static boolean withRetry(int maxTimes, long intervalWait, CallToRetry call) throws Exception {

        validate(maxTimes, intervalWait);

        Exception thrown = null;
        for (int i = 0; i < maxTimes; i++) {
            try {
                call.process();
                return true;
            }
            catch (Exception ex) {
                thrown = ex;
                log.info("withRetry() Encountered failure due to error {}, attempt retry {} of {}", ex.getMessage(),
                        (i + 1), maxTimes, ex);
            }
            try {
                Thread.sleep(intervalWait);
            }
            catch (InterruptedException wakeAndAbort) {
                log.error(wakeAndAbort.getMessage(), wakeAndAbort);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw thrown;
    }

    public static boolean withRetryWithIOException(int maxTimes, long intervalWait, CallToRetry call)
            throws IOException {
        validate(maxTimes, intervalWait);
        IOException thrown = null;
        for (int i = 0; i < maxTimes; i++) {
            try {
                call.process();
                return true;
            }
            catch (IOException ex) {
                thrown = ex;
                log.info("withRetryWithIOException() Encountered failure due to error {}, attempt retry {} of {}",
                        ex.getMessage(), (i + 1), maxTimes, ex);
            }
            try {
                Thread.sleep(intervalWait);
            }
            catch (InterruptedException wakeAndAbort) {
                log.error(wakeAndAbort.getMessage(), wakeAndAbort);
                // Restore interrupted state...
                Thread.currentThread().interrupt();
                break;
            }
        }
        throw thrown;
    }

    private static void validate(int maxTimes, long intervalWait) {
        if (maxTimes <= 0) {
            throw new IllegalArgumentException("Must run at least one time");
        }
        if (intervalWait <= 0) {
            throw new IllegalArgumentException("Initial wait must be at least 1");
        }
    }

    /** interface class. */
    public interface CallToRetryString {

        Object processWithString() throws Exception;

    }

    public interface CallToRetry {

        void process() throws IOException;

    }

}
