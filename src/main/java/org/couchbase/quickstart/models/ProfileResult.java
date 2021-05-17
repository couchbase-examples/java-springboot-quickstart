package org.couchbase.quickstart.models;

public class ProfileResult extends Profile {
    private String message;

    public String getMessage() { return message;}
    public void setMessage(String message){
        this.message = message;
    }

    public ProfileResult() { }

    public ProfileResult(Profile profile, String message){
        super(profile);
        this.message = message;
    }
}
