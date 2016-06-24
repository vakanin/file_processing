import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * @author avakanin
 *
 */
public class FileManipulator {
  private static final Logger log = Logger.getLogger(FileManipulator.class.getName());
  private File file;
  
  public FileManipulator(File file) {
    this.file = file;
  }
  
  /**
   * Switch entire line from the file with an entire other line. The user should specify two line indexes and the application should switch these lines.<br>
   * Example user input:<br>
   * <xmp> <first_line_index> <second_line_index></xmp> 
   * @param index1
   * @param index2
   * @throws IOException - when file cannot be read
   * @throws IndexOutOfBoundsException - lineNum1 or lineNum2 > lines in file<br>
   * @throws IllegalArgumentException - if lineNum1 or lineNum2 <= 0
   */
  public boolean switchTwoLines(int index1, int index2) throws IOException {
    if (index1 < 1 || index2 < 1) {
      log.log(Level.WARNING, "index1 < 1 or index2 < 1");
      throw new IllegalArgumentException();
    }
    ArrayList<String> lines = (ArrayList) Files.readAllLines(file.toPath());
    Collections.swap(lines, index1 - 1, index2 - 1);   // because we count lines from 1
    // validate all lines
    if (!validateLines(lines)) {
      return false;
    }
    writeLines(lines);
    return true;
  }
  
  
  /**
   * witch number at specific index in one line with a number with specific index from another line. The user should provide four indexes and the application should switch the number from the first line with the number from the second line. 
   * Example user input:<br>  
   * <xmp><first_line_index> <first_line_number_index> <second_line_index> <second_line_number_index></xmp>
   * @param lIndex1
   * @param chIndex1
   * @param lIndex2
   * @param chIndex2
   * @return
   * @throws IOException
   * @throws IndexOutOfBoundsException - lineNum1 or lineNum2 > lines in file<br>
   * @throws IllegalArgumentException - if lineNum1 or lineNum2 <= 0
   */
  public boolean switchNumber(int lIndex1, int chIndex1, int lIndex2, int chIndex2) throws IOException {
    if (--lIndex1 < 0 || --lIndex2 < 0 || --chIndex1 < 0 || --chIndex2 < 0) {
      log.log(Level.WARNING, "index1 < 1 or index2 < 1");
      throw new IllegalArgumentException();
    }
    
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath());
    StringBuilder line1 = new StringBuilder(lines.get(lIndex1));
    StringBuilder line2 = new StringBuilder(lines.get(lIndex2));
    char ch = line1.charAt(chIndex1);
    line1.setCharAt(chIndex1, line2.charAt(chIndex2));
    line2.setCharAt(chIndex2, ch);
    lines.set(lIndex1, line1.toString());
    lines.set(lIndex2, line2.toString());
    // validate all lines
    if (!validateLines(lines)) {
      return false;
    }
    writeLines(lines);
    return true;
  }
  
  public boolean insertNumber(int lIndex, int chIndex, int number) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "index1 < 1 or index2 < 1");
      throw new IllegalArgumentException();
    }
    
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath());
    String line1 = lines.get(lIndex);
    line1 = line1.substring(0, chIndex) + number + line1.substring(chIndex);
    lines.set(lIndex, line1);
 // validate all lines
    if (!validateLines(lines)) {
      return false;
    }
    writeLines(lines);
    return true;
  }
  
  /**
   * Validate line by line
   * @param lines
   * @return
   */
  private boolean validateLines(ArrayList<String> lines) {
    int count = 0;
    for (String line : lines) {
      ++count;
      String err = FileValidator.checkLine(line, count);
      if (!err.isEmpty()) {
        System.err.println("After switching this file contains errors. Switch operation FAILED");
        return false;
      }
    }
    return true;
  }
  
  /**
   * Write all lines from a given ArrayList to a file.
   * @param lines
   * @throws IOException
   */
  private void writeLines(ArrayList<String> lines) throws IOException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
      int size = lines.size();
      for (int i = 0; i < size; ++i) {
        String line = lines.get(i);
        bw.append(line);
        if (i != size - 1) {
          bw.append(Constants.NEW_LINE);
        }
      }
      bw.flush();
    }
  }
}
