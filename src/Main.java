import java.io.File;
import java.io.IOException;
import java.util.Scanner;



/**
 * @author avakanin
 *
 */
public class Main {
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
    
    FileManipulator manipulator = new FileManipulator(file);
    
    do {
      System.out.println("---------- Menu ----------");
      System.out.println("1 - Validate the file contents");
      System.out.println("2 - Switch entire line from the file with an entire other line");
      System.out.println("3 - Switch number at specific index in one line with a number with specific index from another line");
      System.out.println("4 - Update");
      System.out.println("5 - Remove");
      System.out.println("0 - Exit");
      System.out.println("--------------------------");
      
      System.out.print(">");
      selection = scanner.nextInt();
      scanner.nextLine();
      
      switch(selection) {
        case 0:
          System.exit(0);
          break;
        case 1:
          FileValidator.validateFileContent(file);
          break;
        case 2:
          switchLines(manipulator);
          break;
        case 3:
          switchNumbers(manipulator);
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
  
  private static void switchLines(FileManipulator manipulator) {
    System.out.println("Please enter line indexes separated with interval. Example: 17 18 (<first_line_index> <second_line_index>)");
    System.out.print(">");
    String input = scanner.nextLine();
    String[] indexes = input.split(" +");
    if (indexes.length != 2) {
      System.err.println("Invalid input. Line indexes must be only two numbers, separated with interval. Please try again");
      return;
    }
    try {
      int index1 = Integer.parseInt(indexes[0]);
      int index2 = Integer.parseInt(indexes[1]);
      manipulator.switchTwoLines(index1, index2);
      System.out.println("Switch line operation SUCCESSFUL");
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input. Line indexes must be numbers. Please try again");
    }
    catch (IllegalArgumentException e) {
      System.err.println("Line indexes cannot be less than 1. Operation FAILED");
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("There is no so many lines in file. Operation FAILED");
    } 
    catch (IOException e) {
      System.err.println("Something wrong was happened. Switch operation FAILED. Please try again");
    }
  }
  
  private static void switchNumbers(FileManipulator manipulator) {
    System.out.println("Please enter line and character indexes separated with intervals.");
    System.out.println("Follow this pattern: <first_line_index> <first_line_number_index> <second_line_index> <second_line_number_index> (Example: 17 18 23 14)");
    System.out.print(">");
    String input = scanner.nextLine();
    String[] indexes = input.split(" +");
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
      System.out.println("Switch line operation SUCCESSFUL");
    }
    catch (NumberFormatException e) {
      System.err.println("Invalid input. Indexes must be numbers. Please try again");
    }
    catch (IllegalArgumentException e) {
      System.err.println("Indexes cannot be less than 1. Operation FAILED");
    }
    catch (IndexOutOfBoundsException e) {
      System.err.println("There is no so many lines in file / There is no so many characters in some of specified lines. Operation FAILED");
    } 
    catch (IOException e) {
      System.err.println("Something wrong was happened. Switch operation FAILED. Please try again");
    }
  }

}
