package maxTask.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * @author Mihir Patel 
 * @author Maxim Torubarov 
 * Utility class used for creating Alert boxes based on AlertType and the message to be displayed in the Alert Box
 * and returns them, so we can do different things with them on the receiving end like showandwait() or just show(). 
 */
public class Dialog {

	private Dialog(){
		
	}
	
	/**
	 * @param type Alert.AlertType type of dialog
	 * @param message message of dialog
	 * @param s name of the dialog
	 * @return Alert box
	 */
	public static Alert pop(Alert.AlertType type ,String message) {
		Alert alertBox = new Alert(type, message, ButtonType.OK, ButtonType.CANCEL);
		alertBox.setContentText(message);
		//alertBox.showAndWait();
		return alertBox;
	}

}
