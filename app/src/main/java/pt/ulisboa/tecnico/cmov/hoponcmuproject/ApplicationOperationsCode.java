package pt.ulisboa.tecnico.cmov.hoponcmuproject;

/**
 * Created by peolie on 13-04-2018.
 */

public enum ApplicationOperationsCode {
    SIGN_UP,
    LOGIN,
    LIST_MONUMENTS,
    LIST_QUIZES,
    SCORE;

    public String toString() {
        return Integer.toString(this.ordinal());
    }
}
