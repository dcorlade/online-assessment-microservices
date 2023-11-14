package app.jwt;

public class UsernameAndPasswordAuthRequest {

    private transient String netId;
    private transient String password;

    public UsernameAndPasswordAuthRequest() {
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
