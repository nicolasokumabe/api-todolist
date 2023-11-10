package br.com.nicolasokumabe.todolist;

public class PasswordChangeModel {
  private String username;
  private String currentPassword;
  private String newPassword;

  public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public String getCurrentPassword() {
    return currentPassword;
}

public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
}

public String getNewPassword() {
    return newPassword;
}

public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
}
}
