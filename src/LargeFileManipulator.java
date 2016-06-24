import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author avakanin
 *
 */
public class LargeFileManipulator implements Manipulator {
  private static final Logger log = Logger.getLogger(LargeFileManipulator.class.getName());
  private File file;
  
  public LargeFileManipulator(File file) {
    this.file = file;
  }
  
  /* (non-Javadoc)
   * @see Manipulator#switchTwoLines(int, int)
   */
  @Override
  public boolean switchTwoLines(int index1, int index2) throws IOException {
    if (--index1 < 0 || --index2 < 0) {
      log.log(Level.WARNING, "index1 < 1 or index2 < 1");
      throw new IllegalArgumentException();
    }
    
    File temp = File.createTempFile("temp", "tmp");
    String line1 = "";
    String line2 = "";
    String line;
    
    int max = Math.max(index1, index2);
    
    try (BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedReader br2 = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
      int counter = 0;
      while (counter <= max) {
        line = br.readLine();
        if(line == null) {
          throw new IndexOutOfBoundsException();
        }
        else if (counter == index1) {
          line1 = line;
        }
        else if (counter == index2) {
          line2 = line;
        }
        ++counter;
      }
      
      counter = 0;
      while ((line = br2.readLine()) != null) {
        if (counter == index1) {
          bw.write(line2);
        }
        else if (counter == index2) {
          bw.write(line1);
        }
        else {
          bw.write(line);
        }
        bw.newLine();
        bw.flush();
        ++counter;
      }
    }
    if (!FileValidator.validateFileContent(temp)) {
      System.err.println("After switching this file contains errors. Switch operation FAILED");
      return false;
    }
    Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return true;
  }

  /* (non-Javadoc)
   * @see Manipulator#switchNumber(int, int, int, int)
   */
  @Override
  public boolean switchNumber(int lIndex1, int chIndex1, int lIndex2, int chIndex2) throws IOException {
    if (--lIndex1 < 0 || --lIndex2 < 0 || --chIndex1 < 0 || --chIndex2 < 0) {
      log.log(Level.WARNING, "lIndex1 < 1, lIndex2 < 1, chIndex1 < 1 or chIndex2 < 1");
      throw new IllegalArgumentException();
    }
    
    File temp = File.createTempFile("temp", "tmp");
    String line1 = "";
    String line2 = "";
    String line;
    
    int max = Math.max(lIndex1, lIndex2);
    
    try (BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedReader br2 = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
      int counter = 0;
      while (counter <= max) {
        line = br.readLine();
        if(line == null) {
          throw new IndexOutOfBoundsException();
        }
        else if (counter == lIndex1) {
          line1 = line;
        }
        else if (counter == lIndex2) {
          line2 = line;
        }
        ++counter;
      }
      
      char ch = line1.charAt(chIndex1);
      line1 = line1.substring(0, chIndex1) + line2.charAt(chIndex2) + line1.substring(++chIndex1, line1.length());
      line2 = line2.substring(0, chIndex2) + ch + line2.substring(++chIndex2, line2.length());
      
      counter = 0;
      while ((line = br2.readLine()) != null) {
        if (counter == lIndex1) {
          bw.write(line1);
        }
        else if (counter == lIndex2) {
          bw.write(line2);
        }
        else {
          bw.write(line);
        }
        bw.newLine();
        bw.flush();
        ++counter;
      }
    }
    if (!FileValidator.validateFileContent(temp)) {
      System.err.println("After switching this file contains errors. Switch operation FAILED");
      return false;
    }
    Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return true;
  }

  /* (non-Javadoc)
   * @see Manipulator#insertNumber(int, int, int)
   */
  @Override
  public boolean insertNumber(int lIndex, int chIndex, int number) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "lIndex < 1 or chIndex < 1");
      throw new IllegalArgumentException();
    }
    
    File temp = File.createTempFile("temp", "tmp");
    String line;
    
    
    try (BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
      
      int counter = 0;
      while ((line = br.readLine()) != null) {
        if (counter == lIndex) {
          line = line.substring(0, chIndex) + number + line.substring(chIndex, line.length());
        }
        bw.write(line);
        bw.newLine();
        bw.flush();
        ++counter;
      }
    }
    if (!FileValidator.validateFileContent(temp)) {
      System.err.println("After inserting this file contains errors. Insert operation FAILED");
      return false;
    }
    Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return true;
  }

  /* (non-Javadoc)
   * @see Manipulator#readNumber(int, int)
   */
  @Override
  public char readNumber(int lIndex, int chIndex) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "lIndex < 1 or chIndex < 1");
      throw new IllegalArgumentException();
    }
    
    String line;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      int counter = 0;
      while ((line = br.readLine()) != null) {
        if (counter == lIndex) {
          return line.charAt(chIndex);
        }
        ++counter;
      }
    }
    return 0;
  }

  /* (non-Javadoc)
   * @see Manipulator#modifyNumber(int, int, int)
   */
  @Override
  public boolean modifyNumber(int lIndex, int chIndex, int number) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "lIndex < 1 or chIndex < 1");
      throw new IllegalArgumentException();
    }
    
    File temp = File.createTempFile("temp", "tmp");
    String line;
    
    
    try (BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
      
      int counter = 0;
      while ((line = br.readLine()) != null) {
        if (counter == lIndex) {
          line = line.substring(0, chIndex) + number + line.substring(++chIndex, line.length());
        }
        bw.write(line);
        bw.newLine();
        bw.flush();
        ++counter;
      }
    }
    if (!FileValidator.validateFileContent(temp)) {
      System.err.println("After modifying this file contains errors. Insert operation FAILED");
      return false;
    }
    Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return true;
  }

  /* (non-Javadoc)
   * @see Manipulator#removeNumber(int, int)
   */
  @Override
  public boolean removeNumber(int lIndex, int chIndex) throws IOException {
    if (--lIndex < 0 || --chIndex < 0) {
      log.log(Level.WARNING, "lIndex < 1 or chIndex < 1");
      throw new IllegalArgumentException();
    }
    
    File temp = File.createTempFile("temp", "tmp");
    String line;
    
    
    try (BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp))) {
      
      int counter = 0;
      while ((line = br.readLine()) != null) {
        if (counter == lIndex) {
          line = line.substring(0, chIndex) + line.substring(++chIndex, line.length());
        }
        bw.write(line);
        bw.newLine();
        bw.flush();
        ++counter;
      }
    }
    if (!FileValidator.validateFileContent(temp)) {
      System.err.println("After removing this file contains errors. Insert operation FAILED");
      return false;
    }
    Files.move(temp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    return true;
  }

}
