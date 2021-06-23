package info.navnoire.recipeappserver_springboot.controller.exhandling;

public class DuplicateUserDataException extends RuntimeException {
    public DuplicateUserDataException(String data, String message) {
        super(String.format("Failed for [%s]: %s", data, message));
    }
}
