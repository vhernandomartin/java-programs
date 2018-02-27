import java.io.*;

public class AQJmsSingleQueueItem implements Serializable {

	private int     _no;
	private String  _title;
	private String  _text;

  /** Constructor that creates a message item
  */
  public AQJmsSingleQueueItem(int no, String title, String text) {
    _no     = no;
    _title  = title;
    _text   = text;
  }

  public int getNo() { return _no; }
  public String getTitle() { return _title; }
  public String getText() { return _text; }

} // End of class AQJmsSingleQueueItem
