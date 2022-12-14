package ar.edu.itba.paw.webapp.dto;



public class RecoveryDto {
    private String token;
    private String password;

    public static RecoveryDto from(String token, String password){
        final RecoveryDto dto = new RecoveryDto();

        dto.setToken(token);
        dto.setPassword(password);

        return dto;
    }

    public String getToken() {
        return token;
    }

    public String getPassword() {
        return password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPassword(String password) {
        this.password = password;
    }

  
}
