package utils.wait;

/**
 * Created by vmenon on 8/6/15.
 *
 * Default interface implementation to be implemented by the classes to make use of the CommonWait class for waiting for
 * a particular code to succeed.
 */
public interface ExpectedCommonWaitCondition<T> {
    public T apply();
}
