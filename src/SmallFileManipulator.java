import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * @author Alexander Vakanin
 *
 */
public class SmallFileManipulator implements Manipulator {
  private static final Logger log = Logger.getLogger(SmallFileManipulator.class.getName());
  private File file;
  
  public SmallFileManipulator(File file) {
    this.file = file;
  }
  
  /* (non-Javadoc)
   * @see Manipulator#switchTwoLines(int, int)
   */
  @Override
  public boolean switchTwoLines(int index1, int index2) throws IOException {
    if (index1 < 1 || index2 < 1) {
      log.log(Level.WARNING, "index1 < 1 or index2 < 1");
      throw new IllegalArgumentException();
    }
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    Collections.swap(lines, index1 - 1, index2 - 1);   // because we count lines from 1
    // validate all lines
    if (!validateLines(lines)) {
      System.err.println("After switching this file contains errors. Switch operation FAILED");
      return false;
    }
    writeLines(lines);
    return true;
  }
  
  
  /* (non-Javadoc)
   * @see Manipulator#switchTwoLines(int, int)
   */
  @Override
  public boolean switchNumber(int lIndex1, int chIndex1, int lIndex2, int chIndex2) throws IOException {
    if (--lIndex1 < 0 || --lIndex2 < 0 || --chIndex1 < 0 || --chIndex2 < 0) {
      log.log(Level.WARNING, "lIndex1 < 1, lIndex2 < 1, chIndex1 < 1 or chIndex2 < 1");
      throw new IllegalArgumentException();
    }
    
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    StringBuilder line1 = new StringBuilder(lines.get(lIndex1));
    StringBuilder line2 = new StringBuilder(lines.get(lIndex2));
    char ch = line1.charAt(chIndex1);
    line1.setCharAt(chIndex1, line2.charAt(chIndex2));
    line2.setCharAt(chIndex2, ch);
    lines.set(lIndex1, line1.toString());
    lines.set(lIndex2, line2.toString());
    // validate all lines
    if (!validateLines(lines)) {
      System.err.println("After switching this file contains errors. Switch operation FAILED");
      return false;
    }
    writeLines(lines);
    return true;
  }
  
  /* (non-Javadoc)
   * @see Manipulator#switchTwoLines(int, int)
   */
  @Override
  public boolean insertNumber(int lIndex, int chIndex, int number) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "lIndex < 1 or chIndex < 1");
      throw new IllegalArgumentException();
    }
    
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    String line = lines.get(lIndex);
    line = line.substring(0, chIndex) + number + line.substring(chIndex);
    lines.set(lIndex, line);
 // validate all lines
    if (!validateLines(lines)) {
      System.err.println("After inserting this file contains errors. Insert operation FAILED");
      return false;
    }
    writeLines(lines);
    return true;
  }
  
  /* (non-Javadoc)
   * @see Manipulator#switchTwoLines(int, int)
   */
  @Override
  public char readNumber(int lIndex, int chIndex) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "lIndex < 1 or chIndex < 1");
      throw new IllegalArgumentException();
    }
    
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    String line = lines.get(lIndex);
    return line.charAt(chIndex);
  }
  
  /* (non-Javadoc)
   * @see Manipulator#switchTwoLines(int, int)
   */
  @Override
  public boolean modifyNumber(int lIndex, int chIndex, int number) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "lIndex < 1 or chIndex < 1");
      throw new IllegalArgumentException();
    }
    
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    String line = lines.get(lIndex);
    line = line.substring(0, chIndex) + number + line.substring(chIndex + 1, line.length());
    lines.set(lIndex, line);
    // validate all lines
    if (!validateLines(lines)) {
      System.err.println("After modifying this file contains errors. Insert operation FAILED");
      return false;
    }
    writeLines(lines);
    return true;
  }
  
  /* (non-Javadoc)
   * @see Manipulator#switchTwoLines(int, int)
   */
  @Override
  public boolean removeNumber(int lIndex, int chIndex) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "lIndex < 1 or chIndex < 1");
      throw new IllegalArgumentException();
    }
    ArrayList<String> lines = (ArrayList<String>) Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    String line = lines.get(lIndex);
    line = line.substring(0, chIndex) + line.substring(chIndex + 1, line.length());
    lines.set(lIndex, line);
    // validate all lines
    if (!validateLines(lines)) {
      System.err.println("After removing this file contains errors. Insert operation FAILED");
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
