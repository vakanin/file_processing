import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexander Vakanin
 *
 */
public class FileValidator {
  private static final Logger log= Logger.getLogger( FileValidator.class.getName());
  
  private FileValidator() {
    throw new RuntimeException("This class cannot be instantiated");
  }

  /**
   * Checks if <code>file</code>'s format is equal to <code>expectedFormat</code>
   * @param file
   * @param expectedFormat
   * @return
   */
  public static boolean checkFormat(File file, String expectedFormat) {
    return file.getName().toLowerCase().endsWith(expectedFormat.toLowerCase());
  }
  
  /**
   * Validate file content for:<br>
   * - each number must not start with 0<br>
   * - the numbers in each line are separated by spaces or tabs<br>
   * - line must start with a number, but not space or tab<br>
   * @return
   */
  public static boolean validateFileContent(File file) {
    boolean success = true;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      int lineNumber = 0;
      while ((line = br.readLine()) != null) {
       ++lineNumber;
       String err = checkLine(line, lineNumber);
       if(err.length() > 0) {
         System.out.println(err);
         success = false;
       }
      }
    } catch (IOException e) {
      System.err.println("Something wrong was happened. File " + file.getAbsolutePath() + " cannot be opened for reading!");
      log.log(Level.SEVERE, e.toString(), e);
    }
    if (success) {
      System.out.println("VALIDATION SUCCESSFUL");
    }
    else {
      System.out.println("VALIDATION FAILED");
    }
    return success;
  }
  
  
  /**
   * Checks if:<br>
   * - each number must not start with 0<br>
   * - the numbers in line are separated by spaces or tabs<br>
   * - line must start with a number, but not space or tab<br>
   * @param line
   * @param lineNumber
   * @return empty string ("") if there is no errors or line is empty, else return String with information about errors <br>
   */
  public static String checkLine(String line, int lineNumber) {
    if (line.isEmpty()) {
      return line;
    }
    StringBuilder sb = new StringBuilder();
    boolean zeroFlag = false;                 // indicate if current character could be zero or not
    ++lineNumber;
    char[] chars = line.toCharArray();
    if (!Character.isDigit(chars[0]) || chars[0] == '0') {
      sb.append("number at line " + lineNumber + " starts with " + chars[0]);
    }

    int chNumber = 0;
    for (int i = 1; i < chars.length; i++) {
      char ch = chars[i];
      chNumber = i + 1;
      if (Character.isDigit(ch)) {
        if (!zeroFlag && ch == '0') {
          sb.append("line " + lineNumber + ", character " + chNumber + " is not allowed to " + ch);
        }
        zeroFlag = true;  // raise flag, because if ch is digit then next character can be '0'
      }
      else if (ch == ' ' || ch == '\t') {
        zeroFlag = false;   // because next number must not start with 0
      }
      else {
        sb.append("line " + lineNumber + ", character " + chNumber + " is not allowed to " + ch);
      }
    }
    
    return sb.toString();
  }
  

  
}
