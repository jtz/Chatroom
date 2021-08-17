package edu.bu.met.cs665.model;

public class ChatRoomFactory {

  private static ChatRoomFactory instance = new ChatRoomFactory();

  private ChatRoomFactory() {}

  /**
   * Singleton pattern to get the only one ChatRoomFactory instance.
   *
   * @return ChatRoomFactory instance
   */
  public static ChatRoomFactory getInstance() {
    return instance;
  }

  /**
   * Factory pattern to create ChatRoom with name.
   *
   * @param name ChatRoom name
   * @return CharRoom instance
   */
  public ChatRoom createChatRoom(String name) {
    ChatRoom chatRoom = new ChatRoom(name);
    chatRoom.initChatRoom();
    return chatRoom;
  }
}
