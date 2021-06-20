package info.navnoire.recipeappserver_springboot.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * Created by Victoria Berezina on 20/06/2021 in RecipeApp project
 */
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
