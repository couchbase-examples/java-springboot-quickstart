package org.couchbase.quickstart.models;

import org.mindrot.jbcrypt.*;

public class Profile {
    private String pid;
    private String firstName, lastName, email, password;
    private Integer balance;

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
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Integer getBalance() { return balance; }
    public void setBalance(Integer balance) {
      this.balance = balance;
    }

    public Profile() { }

    public Profile(String pid, String firstName, String lastName, String email, String password, Integer balance){
        this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public Profile(Profile profile) {
        this.pid = profile.getPid();
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.email = profile.getEmail();
        this.password = profile.getPassword();
        this.balance = profile.getBalance();
    }

    public String toString() {
        return "Profile: { pid="+this.pid+",firstName="+this.firstName+",lastName="+this.lastName+",email="+this.email+",password="+this.password+",balance="+this.balance+" }";
    }
}
