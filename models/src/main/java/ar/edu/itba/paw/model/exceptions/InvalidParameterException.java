package ar.edu.itba.paw.model.exceptions;

public class InvalidParameterException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    String fieldName;
    String message;

    public InvalidParameterException(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
