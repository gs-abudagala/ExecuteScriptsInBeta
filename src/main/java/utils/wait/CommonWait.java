package utils.wait;

/**
 * Created by vmenon on 8/6/15.
 */
public class CommonWait {
    public static final long DEFAULT_WAIT = 30000;
    public static final long DEFAULT_INTERVAL = 1000;

    /**
     * Wait for the ExpectedCommonWaitCondition.apply method to return a boolean truth or not null object for default
     * waitPeriod of 30000ms with the default retryInterval of 1000ms
     * @param expectedCommonWaitCondition  ExpectedCommonWaitCondition instance which contains the code that needs to be executed.
     * @param <T> Generic class, object of which should be returned by the code.
     * @return T Generic class object returned by the apply method of the ExpectedCommonWaitCondition class
     */
    public static <T> T waitForCondition(ExpectedCommonWaitCondition<T> expectedCommonWaitCondition) {
        return waitForCondition(DEFAULT_WAIT, DEFAULT_INTERVAL, expectedCommonWaitCondition);
    }

    /**
     * Wait for the ExpectedCommonWaitCondition.apply method to return a boolean truth or not null object for the given
     * waitPeriod with the given retryInterval
     * @param waitPeriod Time period in milliseconds the code should waitUntil for the retryCondition succeed
     * @param expectedCommonWaitCondition ExpectedCommonWaitCondition instance which contains the code that needs to be executed.
     * @param <T> Generic class, object of which should be returned by the code.
     * @return T Generic class object returned by the apply method of the ExpectedCommonWaitCondition class
     */
    public static <T> T waitForCondition(long waitPeriod, ExpectedCommonWaitCondition<T> expectedCommonWaitCondition) {
        return waitForCondition(waitPeriod, DEFAULT_INTERVAL, expectedCommonWaitCondition);
    }

    /**
     * Wait for the ExpectedCommonWaitCondition.apply method to return a boolean truth or not null object for the given
     * waitPeriod with the given retryInterval
     * @param waitPeriod Time period in milliseconds the code should waitUntil for the retryCondition succeed
     * @param retryInterval Retry interval in milliseconds
     * @param expectedCommonWaitCondition ExpectedCommonWaitCondition instance which contains the code that needs to be executed.
     * @param <T> Generic class, object of which should be returned by the code.
     * @return T Generic class object returned by the apply method of the ExpectedCommonWaitCondition class
     */
    public static <T> T waitForCondition(long waitPeriod, long retryInterval, ExpectedCommonWaitCondition<T> expectedCommonWaitCondition) {
        T returnValue = null;
        boolean flag = false;
        long startTime = System.currentTimeMillis();
        long executionTime = 0;
        while (!flag && executionTime < waitPeriod) {
            try {
                returnValue = expectedCommonWaitCondition.apply();
                if (returnValue != null && Boolean.class.equals(returnValue.getClass())) {
                    if (Boolean.TRUE.equals(returnValue)) {
                        return returnValue;
                    }
                } else if (returnValue != null) {
                    return returnValue;
                }
                Thread.sleep(retryInterval);
            } catch (Exception e) {

            }
            executionTime = System.currentTimeMillis() - startTime;
        }
        return returnValue;
    }
}
