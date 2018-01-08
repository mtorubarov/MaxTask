package maxTask.util;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import maxTask.model.*; 

/**
 * @author Mihir Patel , Maxim Torubarov 
 * Utility class providing capabilities for displaying new screens on the Front end
 * Mostly does so by creating a new Stage an scene within that stage and then finally switches stages  
 * Also ensure that if any user closes the entire application by closing any window with the "X", data is serialized and therefore it persists 
 */
public class ManageFrontend {

	/**
	 * private constructor for this class so no one can call it from outside  
	 */
	private ManageFrontend () { 
		
	}
	
	/**
	 * This method will open a new Screen on the frontend 
	 * @param ss - Name of the fxml file which is to be opened 
	 * @param e - Action event 
	 * @param title - The title you want to display on the new screen window 
	 * @param p - Parent base class for all nodes which have children in a scene graph  
	 * @throws IOException
	 */
	
	public static void DisplayScreen(String ss, ActionEvent e, String title, Parent p) throws IOException {
		System.out.println("we are displaying: "+ ss);
		Scene s = new Scene(p);
		//s.getStylesheets().add(PhotoAlbum.class.getResource("application.css").toExternalForm());;
		Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
		st.setScene(s);
		st.setTitle(title);
		st.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				if (ss.equalsIgnoreCase("ChecklistPage.fxml")) {
					//Admin admin = Admin.getInstance("admin");
					/*
					try {
						admin.SerializeAdmin(admin);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
			}
		});
		st.setResizable(false);
		st.show();
	}
	
	
}
