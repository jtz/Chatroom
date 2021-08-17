package edu.bu.met.cs665;

import edu.bu.met.cs665.db.ChatRoomDB;
import edu.bu.met.cs665.model.ChatRoom;
import edu.bu.met.cs665.model.ChatRoomFactory;
import edu.bu.met.cs665.model.User;
import edu.bu.met.cs665.model.UserBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class Main {

  private static Logger logger = Logger.getLogger(Main.class);
  private static User currentUser;
  private static ChatRoom currentChatRoom;

  /**
   * A main method to run examples.
   *
   * @param args not used
   */
  public static void main(String[] args) {
    startChatRoom();
    logger.info("*************** Thank you for using JTChatRoom! ******************************");
  }

  /** Start chatroom with main menu and choose action by input. */
  public static void startChatRoom() {

    Scanner scanner = new Scanner(System.in, "UTF-8");

    while (true) {
      System.out.println();
      logger.info("*************** Welcome to JTChatRoom Simulaiton System **********************");

      Integer item = null;
      while (item == null) {
        try {
          showUser();
          System.out.println();
          logger.info("1. Create User");
          logger.info("2. Choose User from list");
          logger.info("3. Create ChatRoom");
          logger.info("4. Choose ChatRoom from list");
          logger.info("5. Go to ChatRoom");
          logger.info("6. Exit JTChatRoom");
          System.out.println();
          logger.info("Please input item number to choose action from Main Menu: ");

          String line = scanner.nextLine();
          item = Integer.valueOf(line.trim());
          if (item < 1 || item > 6) {
            logger.warn("Please input correct item number.");
            item = null;
          }
        } catch (Exception e) {
          logger.warn("Please input item number.");
        }
      }

      switch (item) {
        case 1:
          createUser(scanner);
          break;
        case 2:
          chooseUser(scanner);
          break;
        case 3:
          createChatRoom(scanner);
          break;
        case 4:
          chooseChatRoom(scanner);
          break;
        case 5:
          chat(scanner);
          break;
        case 6:
          scanner.close();
          break;
        default:
          break;
      }
      if (item == 6) {
        break;
      }
    }
  }

  /** Show current user name, chatroom name, and the number of online users. */
  private static void showUser() {
    String name = "Guest";
    String chatRoomName = "NotSet";
    int number = 0;

    if (currentUser != null) {
      name = currentUser.getName();
      if (currentChatRoom != null) {
        chatRoomName = currentChatRoom.getName();
        number = currentChatRoom.userSize();
      }
    }
    System.out.println();
    logger.info(
        "Hello "
            + name
            + "! You are in ChatRoom: "
            + chatRoomName
            + ". Current Online Users: "
            + number);
  }

  /**
   * Create user with input name(required), sex, and country.
   *
   * @param scanner the input info
   */
  private static void createUser(Scanner scanner) {

    ChatRoomDB chatRoomDB = ChatRoomDB.getInstance();

    while (true) {
      System.out.println();
      logger.info(
          "Create User with input name|sex|country split by '|', only name is required. "
              + "Input 'B' to back to Main Menu.");
      String line = scanner.nextLine();

      if ("B".equals(line)) {
        // back to Main Menu
        break;
      }

      String[] items = line.split("\\|");
      if (items == null || items.length == 0 || items[0] == null || items[0].isEmpty()) {
        logger.warn("Please input correct name.");
      } else {
        String name = items[0].trim();
        try {
          // check if user name already exist
          boolean exist = chatRoomDB.checkUser(name);
          if (exist) {
            logger.info("User " + name + " exist in JTChatRoom, please input another user name.");
            continue;
          }
        } catch (Exception e) {
          logger.error("Get error when check user: " + name);
        }

        // build user
        UserBuilder builder = new UserBuilder(name);
        if (items.length > 1) {
          builder.setSex(items[1].trim());
        }
        if (items.length > 2) {
          builder.setCountry(items[2].trim());
        }
        currentUser = builder.build();

        try {
          chatRoomDB.addUser(currentUser);
        } catch (Exception e) {
          logger.error("Get error when add user: " + name);
        }
        break;
      }
    }
  }

  /**
   * Choose created user from database.
   *
   * @param scanner the input info
   */
  private static void chooseUser(Scanner scanner) {

    ChatRoomDB chatRoomDB = ChatRoomDB.getInstance();

    while (true) {
      System.out.println();
      List<User> userList = new ArrayList<User>();

      try {
        userList = chatRoomDB.listUser();
      } catch (Exception e1) {
        logger.error("Get error when get user list.");
      }

      // list all users that are already created in database
      for (int i = 0; i < userList.size(); i++) {
        logger.info((i + 1) + ". " + userList.get(i).getName());
      }
      logger.info("Input item number to choose user. Input 'B' to back to Main Menu.");

      String line = scanner.nextLine();
      if ("B".equals(line)) {
        break;
      }

      try {
        Integer item = Integer.valueOf(line.trim());
        if (item < 1 || item > userList.size() + 1) {
          logger.warn("Please input correct item number.");
        } else {
          // get user and chatroom
          currentUser = userList.get(item - 1);
          String chatRoom = chatRoomDB.getUserChatRoom(currentUser.getName());
          if (chatRoom != null) {
            ChatRoomFactory chatRoomFactory = ChatRoomFactory.getInstance();
            currentChatRoom = chatRoomFactory.createChatRoom(chatRoom);
          }
          break;
        }
      } catch (Exception e) {
        logger.warn("Please input item number.");
      }
    }
  }

  /**
   * Create chatroom with input name.
   *
   * @param scanner the input info
   */
  private static void createChatRoom(Scanner scanner) {

    ChatRoomDB chatRoomDB = ChatRoomDB.getInstance();

    while (true) {
      System.out.println();
      logger.info("Create ChatRoom with input name. Input 'B' to back to Main Menu.");
      String line = scanner.nextLine();
      if ("B".equals(line)) {
        break;
      }

      if (line == null || line.isEmpty()) {
        logger.warn("Please input correct name.");
      } else {
        ChatRoomFactory chatRoomFactory = ChatRoomFactory.getInstance();
        ChatRoom chatRoom = chatRoomFactory.createChatRoom((line).trim());
        try {
          chatRoomDB.addChatRoom(chatRoom);
        } catch (Exception e) {
          logger.error("Get error when add ChatRoom: " + chatRoom);
        }
        logger.info("Successfully add ChatRoom: " + chatRoom.getName());
        break;
      }
    }
  }

  /**
   * Choose created chatroom from database.
   *
   * @param scanner the input info
   */
  private static void chooseChatRoom(Scanner scanner) {

    if (currentUser == null) {
      logger.warn("Please choose or create User first, back to Main Menu.");
      return;
    }

    ChatRoomDB chatRoomDB = ChatRoomDB.getInstance();

    while (true) {
      System.out.println();
      List<ChatRoom> chatRoomList = new ArrayList<ChatRoom>();

      try {
        chatRoomList = chatRoomDB.listChatRoom();
      } catch (Exception e1) {
        logger.error("Get error when get chatRoom list.");
      }

      // list all chatrooms that are already created in database
      for (int i = 0; i < chatRoomList.size(); i++) {
        logger.info((i + 1) + ". " + chatRoomList.get(i).getName());
      }
      logger.info("Input item number to choose ChatRoom. Input 'B' can back to Main Menu.");

      String line = scanner.nextLine();
      if ("B".equals(line)) {
        break;
      }

      try {
        Integer item = Integer.valueOf(line.trim());
        if (item < 1 || item > chatRoomList.size() + 1) {
          logger.warn("Please input with correct item number.");
        } else {
          // current user must exist current chatroom before enter chosen chatroom
          if (currentChatRoom != null) {
            currentChatRoom.detach(currentUser);
            currentChatRoom = null;
          }
          ChatRoom chatRoom = chatRoomList.get(item - 1);
          chatRoom.attach(currentUser);
          currentChatRoom = chatRoom;
          break;
        }
      } catch (Exception e) {
        logger.warn("Please input with item number.");
      }
    }
  }

  /**
   * Start chatting.
   *
   * @param scanner the input info
   */
  private static void chat(Scanner scanner) {

    if (currentUser == null) {
      logger.warn("Please choose or create User first, back to Main Menu.");
      return;
    }
    if (currentChatRoom == null) {
      logger.warn("Please choose or create ChatRoom first, back to Main Menu.");
      return;
    }

    showUser();

    while (true) {
      System.out.println();
      logger.info(
          "Input message that will send to all users in this ChatRoom. "
              + "Input 'B' to back to Main Menu. "
              + "Input 'BB' to quit current ChatRoom and back to Main Menu. ");
      String line = scanner.nextLine();
      if ("B".equals(line)) {
        // back to Main Menu
        break;
      } else if ("BB".equals(line)) {
        // current user exit current chatroom, and then set current chatroom null
        currentChatRoom.detach(currentUser);
        currentChatRoom = null;
        break;
      } else {
        // send message to all users in this current chatroom
        currentChatRoom.sendMessage(currentUser, line);
      }
    }
  }
}
