package br.com.nicolasokumabe.todolist;

public class ErrorResponse {
    private int businessCode;
    private String error;

    public ErrorResponse(int businessCode, String error) {
        this.businessCode = businessCode;
        this.error = error;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

