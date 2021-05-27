package org.couchbase.quickstart.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class Profile {
    private String pid;
    private String firstName, lastName, email, password;

    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName =  firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = getEncryptedPassword(password);
    }

    public Profile() { }

    public Profile(String pid, String firstName, String lastName, String email, String password){
        this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Profile(Profile profile) {
        this.pid = profile.getPid();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.email = profile.getEmail();
        this.password = profile.getPassword();
    }

    //simple encoding to encrypt the password using BCrypt
    //for a more robust way to encrypt passwords, see documentation:
    //https://docs.couchbase.com/java-sdk/current/concept-docs/encryption.html
    private String getEncryptedPassword(String decryptedPassword){
        //note to see if the password matches the encrypted password you can use the
        //encoder.matches method
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(decryptedPassword);
    }
}
