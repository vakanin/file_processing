import java.io.IOException;


/**
 * @author avakanin
 *
 */
public interface Manipulator {
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
  public boolean switchTwoLines(int index1, int index2) throws IOException;
  
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
  public boolean switchNumber(int lIndex1, int chIndex1, int lIndex2, int chIndex2) throws IOException;
  
  /**
   * Insert a number at a given position in the file. 
   * The user should provide the two indexes at which the number should be inserted and the value of that number. 
   * The number should be inserted in that position and the content of the row from that position to it’s end should be shifted with one index to the right.<br> 
   * Example user input:<br>
   * <xmp><line_index> <line_number_index> <number to be inserted></xmp>
   * @param lIndex
   * @param chIndex
   * @param number
   * @return
   * @throws IOException
   * @throws IllegalArgumentException
   * @throws IndexOutOfBoundsException
   */
  public boolean insertNumber(int lIndex, int chIndex, int number) throws IOException;
  
  /**
   * Read a number at a given position. The user should provide the two indexes of a number to be red. 
   * The application should print that number to the user. 
   * Example user input:<br> 
   * <xmp><line_index> <line_number_index></xmp>
   *  
   * @param lIndex
   * @param chIndex
   * @param number
   * @return
   * @throws IOException
   * @throws IllegalArgumentException
   * @throws IndexOutOfBoundsException
   */
  public char readNumber(int lIndex, int chIndex) throws IOException;
  
  /**
   * Modify a number at a given position. The user should provide the two indexes of the number to be modified and a value. 
   * The value given by the user should replace the existing value at that position. 
   * Example user input:<br>
   * <xmp><line_index> <line_number_index> <number to be set></xmp>
   *  
   * @param lIndex
   * @param chIndex
   * @param number
   * @return
   * @throws IOException
   * @throws IllegalArgumentException
   * @throws IndexOutOfBoundsException
   */
  public boolean modifyNumber(int lIndex, int chIndex, int number) throws IOException ;
  
  /**
   *  Remove a number at a given position. The user should provide the two indexes of the number to be removed. 
   *  All remaining numbers in that row should be moved with one index to the left. 
   *  Example user input:<br>
   *  <xmp><line_index> <line_number_index></xmp> 
   * @param lIndex
   * @param chIndex
   * @return
   * @throws IOException
   * @throws IllegalArgumentException
   * @throws IndexOutOfBoundsException
   */
  public boolean removeNumber(int lIndex, int chIndex) throws IOException;
  
}
