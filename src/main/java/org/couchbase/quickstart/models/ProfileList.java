package org.couchbase.quickstart.models;

import java.util.ArrayList;
import java.util.List;

public class ProfileList {

    private List<Profile> profiles;
    public List<Profile> getProfiles() { return profiles; }
    public void setProfiles(List<Profile> profiles) { this.profiles = profiles;}

    private String message;
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public ProfileList() {
        profiles = new ArrayList<Profile>();
    }
}
