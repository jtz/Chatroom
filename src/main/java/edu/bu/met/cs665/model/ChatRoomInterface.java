package edu.bu.met.cs665.model;

public interface ChatRoomInterface {

  /** Initialize chatroom by getting user lists. */
  public void initChatRoom();

  /**
   * Attach user to chatroom.
   *
   * @param user User object
   */
  public void attach(User user);

  /**
   * Detach user from chatroom.
   *
   * @param user User object
   */
  public void detach(User user);

  /**
   * Send message to all users in current chatroom.
   *
   * @param user User object
   * @param message message that will be sent
   */
  public void sendMessage(User user, String message);

  /**
   * Notify all users.
   *
   * @param user User object
   * @param message message that will be sent
   */
  public void notifyAllUsers(User user, String message);
}
