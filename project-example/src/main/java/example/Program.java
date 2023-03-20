package example;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Program extends Application {
	
	/**
	 * Web engine instance
	 */
	private WebEngine webEngine;
	
	/**
	 * The main entry point for all JavaFX applications.
	 * The start method is called after the init method has returned,
	 * and after the system is ready for the application to begin running.
	 *
	 * <p>
	 * NOTE: This method is called on the JavaFX Application Thread.
	 * </p>
	 *
	 * @param primaryStage the primary stage for this application, onto which
	 *                     the application scene can be set.
	 *                     Applications may create other stages, if needed, but they will not be
	 *                     primary stages.
	 * @throws Exception if something goes wrong
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane pane = new BorderPane();
		HBox bottomPane = new HBox(5);
		Scene scene = new Scene(pane, 1000, 600);
		
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		
		// Add controls to the scene
		WebView webView = new WebView();
		
		webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);
		
		Button webButton = new Button("Load website");
		webButton.setOnAction(evt -> {
			webEngine.load("https://www.github.com/Ushiosan23/custom-jfx-plugin");
		});
		
		webEngine.getLoadWorker().stateProperty().addListener((obs, old, nVal) -> {
			if (nVal == Worker.State.SUCCEEDED) {
				pane.setBottom(null);
			} else {
				pane.setBottom(bottomPane);
			}
		});
		
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setPadding(new Insets(10));
		
		bottomPane.getChildren().add(webButton);
		pane.setCenter(webView);
		pane.setBottom(bottomPane);
		
		primaryStage.setOnCloseRequest(evt -> webEngine.load(null));
		primaryStage.show();
	}
	
}
