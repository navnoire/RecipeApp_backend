package info.navnoire.recipeappserver_springboot.controller.exhandling;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
