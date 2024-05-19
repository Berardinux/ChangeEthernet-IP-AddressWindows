import javax.swing.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeIP {

	public static final String TITLE = "IP Changer";
	public static final String ZERO = "10.1.3.5";
	public static final String ONE = "169.254.10.5";
	public static final String TWO = "192.168.1.1";
	public static final String THREE = "192.168.1.1";
	public static final String FOUR = "Set your own";
	public static final String FIVE = "DHCP";
	public static final String SUBNET = "255.255.255.0";
	public static final String INTERFACE_NAME = "Ethernet";

	public static void main(String[] args) {

		JFrame frame = new JFrame();
		String Ip = UserChoice(frame, "first");
		System.out.println(Ip);
		IpChange(Ip);
		frame.dispose();
	}

	public static void IpChange(String Ip) {
		String command = "netsh interface ip set address \"" + INTERFACE_NAME + "\" static " + Ip + " " + SUBNET;
		System.out.println(command);
		try {
			Runtime.getRuntime().exec(command);
			JOptionPane.showMessageDialog(null, "Invalid IP Address. Please enter a valid IP.", TITLE, JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			System.out.println("Error");
			JOptionPane.showMessageDialog(null, "Command did not work )-:", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public static String UserChoice(JFrame frame, String Value) {
		Object[] options = {ZERO ,ONE ,TWO ,THREE ,FOUR ,FIVE};
		String Ip = FIVE;
    int choice = JOptionPane.showOptionDialog(
    frame,"<html><h1><br><br>"+
			"############# Set Your IP #############"+
			"</br></br> <br><br>"+
			"-------------------------------------------------------------"+
			"</br></br></h1> <h2><br><br>"+
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
			"YourIP: 10.1.3.5 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; DobleIP: 10.1.3.1 <br></br>"+
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
			"YourIP: 169.254.10.5 &nbsp;&nbsp; MRCT_IP:169.254.10.0 <br></br>"+
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
			"YourIP: 192.168.1.1 ?? <br></br>"+
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
			"</br></br></h2> <h1><br><br>"+
			"-------------------------------------------------------------"+
			"</br></br></h1></html>",
      TITLE,
      JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE,
      null,
      options,
    	options[0] // Default button (Agree)
    );
		
		switch (choice) {
			case 0:
				Ip=ZERO;
				return Ip;
			case 1:
				Ip=ONE;
				return Ip;
			case 2:
				Ip=TWO;
				return Ip;
			case 3:
				Ip=THREE;
				return Ip;
			case 4:
				Ip = InputIP();
				return Ip;
			case 5:
				Ip=FIVE;
				return Ip;
			default:
				Ip="DHCP";
		}

		return Ip;
	}

	public static String InputIP() {
    String ip = "";
    boolean valid = false;

    while (!valid) {
      ip = JOptionPane.showInputDialog("Enter your IP Address:");
      // Check if the entered IP address is valid
      if (isValidIP(ip)) {
        valid = true;
      } else {
        JOptionPane.showMessageDialog(null, "Invalid IP Address. Please enter a valid IP.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    return ip;
  }

  // Method to validate IP address format
  private static boolean isValidIP(String ip) {
    // Regular expression pattern to validate IPv4 address
    String ipv4Pattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."+
										     "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."+
            				     "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."+
            		    		 "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    // Regular expression pattern to validate IPv6 address
    String ipv6Pattern = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";

    Pattern patternIPv4 = Pattern.compile(ipv4Pattern);
    Pattern patternIPv6 = Pattern.compile(ipv6Pattern);

    Matcher matcherIPv4 = patternIPv4.matcher(ip);
    Matcher matcherIPv6 = patternIPv6.matcher(ip);
    return matcherIPv4.matches() || matcherIPv6.matches();
  }
}