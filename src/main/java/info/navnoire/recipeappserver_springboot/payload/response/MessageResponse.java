package info.navnoire.recipeappserver_springboot.payload.response;

/**
 * Created by Victoria Berezina on 20/06/2021 in RecipeApp project
 */
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
