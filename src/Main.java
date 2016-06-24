import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * @author Alexander Vakanin
 *
 */
public class Main {
  private static final Logger log = Logger.getLogger(Main.class.getName());
  private static Scanner scanner = new Scanner(System.in);
  private static File file;

  /**
   * @param args
   */
  public static void main(String[] args) {
    int selection = 0;

    boolean loadFlag = false; // indicate if file is loaded successfully
    
    do {
      System.out.println("Please enter path to the file (txt format): ");
      System.out.print(">");
      String path = scanner.nextLine();
      file = new File(path);
      if (!file.exists()) {
        System.err.println(file.getAbsolutePath() + " does NOT exists");
      }
      else if (!FileValidator.checkFormat(file, ".txt")) {
        System.err.println(file.getAbsolutePath() + " is NOT in txt format");
      }
      else {
        System.out.println("You loaded file: " + file.getAbsolutePath());
        loadFlag = true;
      }
    } while (!loadFlag);

    System.out.println();
    
    Manipulator manipulator;
    if (file.length() < 1048576) { // 1Mb = 1048576 bytes
      manipulator = new SmallFileManipulator(file);
    }
    else {
      manipulator = new LargeFileManipulator(file);
    }

    do {
      System.out.println("---------- Menu ----------");
      System.out.println("1 - Validate the file contents");
      System.out.println("2 - Switch entire line from the file with an entire other line");
      System.out.println("3 - Switch number at specific index in one line with a number with specific index from another line");
      System.out.println("4 - Insert a number at a given position in the file");
      System.out.println("5 - Read a number at a given position");
      System.out.println("6 - Modify a number at a given position");
      System.out.println("7 - Remove a number at a given position");
      System.out.println("0 - Exit");
      System.out.println("--------------------------");
      
      System.out.print(">");
      try {
        selection = scanner.nextInt();
      }
      catch (Throwable e) {
        selection = Integer.MAX_VALUE;
      }
      scanner.nextLine();
      
      switch(selection) {
        case 0:
          System.out.println();
          System.out.println("TERMINATED");
          System.exit(0);
          break;
        case 1:
          validateFileContent();
          break;
        case 2:
          switchLines(manipulator);
          break;
        case 3:
          switchNumbers(manipulator);
          break;
        case 4:
          insertNumber(manipulator);
          break;
        case 5:
          readNumber(manipulator);
          break;
        case 6:
          modifyNumber(manipulator);
          break;
        case 7:
          removeNumber(manipulator);
          break;
        default:
            System.out.println("Invalid option. Please try again");
            break;
      }
      
      System.out.println();
      System.out.println("Do you want to continue (Y/n)");
      System.out.println(">");
      if (scanner.nextLine().equalsIgnoreCase("n")) {
        break;
      }
      System.out.println();
    } while(selection > 0);
    
    System.out.println();
    System.out.println("TERMINATED");
    scanner.close();
  }
  
  private static void validateFileContent() {
    if (FileValidator.validateFileContent(file)) {
      System.out.println("VALIDATION SUCCESSFUL");
    }
    else {
      System.out.println("VALIDATION FAILED");
    }
    
  }
  
  private static void switchLines(Manipulator manipulator) {
    System.out.println("Please enter line indexes separated with interval. Example: 17 18 (<first_line_index> <second_line_index>)");
    System.out.print(">");
    String input = scanner.nextLine();
    String[] indexes = input.trim().split(" +");
    if (indexes.length != 2) {
      System.err.println("Invalid input. Line indexes must be two numbers, separated with interval. Please try again");
      return;
    }
    try {
      int index1 = Integer.parseInt(indexes[0]);
      int index2 = Integer.parseInt(indexes[1]);
      manipulator.switchTwoLines(index1, index2);
      System.out.println("Operation SUCCESSFUL");
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input. Indexes must be numbers. Operation FAILED");
    }
    catch (IllegalArgumentException e) {
      System.err.println("Indexes cannot be less than 1. Operation FAILED");
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("There are not so many lines in file. Operation FAILED");
    } 
    catch (IOException e) {
      e.printStackTrace();
      log.log(Level.SEVERE, e.getMessage());
      System.err.println("Something wrong happened. Operation FAILED. Please try again");
    }
  }
  
  private static void switchNumbers(Manipulator manipulator) {
    System.out.println("Please enter line and character indexes separated with intervals.");
    System.out.println("Follow this pattern: <first_line_index> <first_line_number_index> <second_line_index> <second_line_number_index> (Example: 17 18 23 14)");
    System.out.print(">");
    String input = scanner.nextLine();
    String[] indexes = input.trim().split(" +");
    if (indexes.length != 4) {
      System.err.println("Invalid input. Indexes must be four numbers, separated with intervals. Please try again");
      return;
    }
    try {
      int lIndex1 = Integer.parseInt(indexes[0]);
      int lIndex2 = Integer.parseInt(indexes[2]);
      int chIndex1 = Integer.parseInt(indexes[1]);
      int chIndex2 = Integer.parseInt(indexes[3]);
      manipulator.switchNumber(lIndex1, chIndex1, lIndex2, chIndex2);
      System.out.println("Operation SUCCESSFUL");
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input. Indexes must be numbers. Operation FAILED");
    }
    catch (IllegalArgumentException e) {
      System.err.println("Indexes cannot be less than 1. Operation FAILED");
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("There are not so many lines in file / There are not so many characters in some of specified lines. Operation FAILED");
    } 
    catch (IOException e) {
      System.err.println("Something wrong happened. Operation FAILED. Please try again");
    }
  }
  
  private static void insertNumber(Manipulator manipulator) {
    System.out.println("Please enter line index, character index and number value separated with intervals. Number value must be from 0 to 9");
    System.out.println("Follow this pattern: <line_index> <line_number_index> <number to be inserted>");
    System.out.print(">");
    String input = scanner.nextLine();
    String[] args = input.trim().split(" +");
    if (args.length != 3) {
      System.err.println("Invalid input. Arguments must be three numbers, separated with intervals. Please try again");
      return;
    }
    try {
      int lIndex = Integer.parseInt(args[0]);
      int chIndex = Integer.parseInt(args[1]);
      int number = Integer.parseInt(args[2]);
      if (number < 0 || number > 9) {
        throw new IllegalArgumentException();
      }
      manipulator.insertNumber(lIndex, chIndex, number);
      System.out.println("Operation SUCCESSFUL");
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input. All arguments must be numbers. Operation FAILED");
    }
    catch (IllegalArgumentException e) {
      System.err.println("Indexes cannot be less than 1 and number value must be from 0 to 9. Operation FAILED");
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("There are not so many lines in file / There are not so many characters in some of specified lines. Operation FAILED");
    } 
    catch (IOException e) {
      System.err.println("Something wrong happened. Operation FAILED. Please try again");
    }
  }
  
  private static void readNumber(Manipulator manipulator) {
    System.out.println("Please enter line index and character index separated with interval. Example: 17 18");
    System.out.println("Follow this pattern: <line_index> <line_number_index>");
    System.out.print(">");
    String input = scanner.nextLine();
    String[] indexes = input.trim().split(" +");
    if (indexes.length != 2) {
      System.err.println("Invalid input. Indexes must be two numbers, separated with interval. Please try again");
      return;
    }
    try {
      int lIndex = Integer.parseInt(indexes[0]);
      int chIndex = Integer.parseInt(indexes[1]);
      char c = manipulator.readNumber(lIndex, chIndex);
      System.out.println(c);
      System.out.println("Operation SUCCESSFUL");
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input. Indexes must be numbers. Operation FAILED");
    }
    catch (IllegalArgumentException e) {
      System.err.println("Indexes cannot be less than 1. Operation FAILED");
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("There are not so many lines in file / There are not so many characters in some of specified lines. Operation FAILED");
    } 
    catch (IOException e) {
      System.err.println("Something wrong happened. Operation FAILED. Please try again");
    }
  }
  
  private static void modifyNumber(Manipulator manipulator) {
    System.out.println("Please enter line index, character index and number value separated with intervals. Number value must be from 0 to 9");
    System.out.println("Follow this pattern: <line_index> <line_number_index> <number to be set>");
    System.out.print(">");
    String input = scanner.nextLine();
    String[] args = input.trim().split(" +");
    if (args.length != 3) {
      System.err.println("Invalid input. Arguments must be three numbers, separated with intervals. Please try again");
      return;
    }
    try {
      int lIndex = Integer.parseInt(args[0]);
      int chIndex = Integer.parseInt(args[1]);
      int number = Integer.parseInt(args[2]);
      if (number < 0 || number > 9) {
        throw new IllegalArgumentException();
      }
      manipulator.modifyNumber(lIndex, chIndex, number);
      System.out.println("Operation SUCCESSFUL");
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input. All arguments must be numbers. Operation FAILED");
    }
    catch (IllegalArgumentException e) {
      System.err.println("Indexes cannot be less than 1 and number value must be from 0 to 9. Operation FAILED");
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("There are not so many lines in file / There are not so many characters in some of specified lines. Operation FAILED");
    } 
    catch (IOException e) {
      System.err.println("Something wrong happened. Operation FAILED. Please try again");
    }
  }

  private static void removeNumber(Manipulator manipulator) {
    System.out.println("Please enter line index and character index separated with interval. Example: 17 18");
    System.out.println("Follow this pattern: <line_index> <line_number_index>");
    System.out.print(">");
    String input = scanner.nextLine();
    String[] indexes = input.trim().split(" +");
    if (indexes.length != 2) {
      System.err.println("Invalid input. Indexes must be two numbers, separated with interval. Please try again");
      return;
    }
    try {
      int lIndex = Integer.parseInt(indexes[0]);
      int chIndex = Integer.parseInt(indexes[1]);
      manipulator.removeNumber(lIndex, chIndex);
      System.out.println("Operation SUCCESSFUL");
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input. Indexes must be numbers. Operation FAILED");
    }
    catch (IllegalArgumentException e) {
      System.err.println("Indexes cannot be less than 1. Operation FAILED");
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("There are not so many lines in file / There are not so many characters in some of specified lines. Operation FAILED");
    } 
    catch (IOException e) {
      System.err.println("Something wrong happened. Operation FAILED. Please try again");
    }
  }
}
