package sk.gymdb.thinis.delegate;

/**
 * Created by matejkobza on 19.1.2014.
 */
public interface LoginDelegate {

    void loginSuccessful(String output);

    void loginUnsuccessful(String output);

}
