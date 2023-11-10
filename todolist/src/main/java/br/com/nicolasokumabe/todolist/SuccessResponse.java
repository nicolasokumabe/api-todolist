package br.com.nicolasokumabe.todolist;

public class SuccessResponse {
    private int businessCode;
   private String message;

   public SuccessResponse(int businessCode, String message) {
       this.businessCode = businessCode;
       this.message = message;
   }

   public int getBusinessCode() {
       return businessCode;
   }

   public void setBusinessCode(int businessCode) {
       this.businessCode = businessCode;
   }

   public String getMessage() {
       return message;
   }

   public void setMessage(String message) {
       this.message = message;
   }
}
