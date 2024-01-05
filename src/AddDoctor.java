import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddDoctor extends Application {

	Scene signUpScene;
	Stage stage;
	TextField txt_fName, txt_lName, txt_email;
	RadioButton rb_male, rb_female;
	ComboBox<Integer> cmb_date;
	ComboBox<String> cmb_month, cmb_year;
	PasswordField pass, rePass;
	ComboBox<String> cmb_specialization;

	public String getDoctorDetails() {
		String firstName = txt_fName.getText();
		String lastName = txt_lName.getText();
		String gender = rb_male.isSelected() ? "Male" : "Female";
		String dob = cmb_year.getValue() + "-" + (cmb_month.getSelectionModel().getSelectedIndex() + 1) + "-"
				+ cmb_date.getValue();
		String email = txt_email.getText();
		String password = pass.getText();
		String specialization = cmb_specialization.getValue();

		return firstName + " " + lastName + " " + gender + " " + dob + " " + email + " " + password + " "
				+ specialization;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.setTitle("Add-Doctors");

		Text mainHeading = new Text("ADD Doctor");
		mainHeading.setStyle("-fx-font-size: 30px");

		Label lbl_firstName = new Label("First Name");
		txt_fName = new TextField();
		txt_fName.setPromptText("Enter First Name");

		Label lbl_lastName = new Label("Last Name");
		txt_lName = new TextField();
		txt_lName.setPromptText("Enter Last Name");

		Label lbl_gender = new Label("Gender");
		ToggleGroup rb_group = new ToggleGroup();
		rb_female = new RadioButton("Female");
		rb_male = new RadioButton("Male");
		rb_female.setToggleGroup(rb_group);
		rb_male.setToggleGroup(rb_group);

		Label lbl_dob = new Label("Date Of Birth");

		cmb_date = new ComboBox<>();
		cmb_date.setPromptText("Date");
		for (int i = 1; i <= 31; i++) {
			cmb_date.getItems().add(i);
		}

		cmb_month = new ComboBox<>();
		cmb_month.setPromptText("Month");
		cmb_month.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December");

		cmb_year = new ComboBox<>();
		cmb_year.setPromptText("Year");
		for (int i = 1990; i <= 2023; i++) {
			cmb_year.getItems().add(Integer.toString(i));
		}

		Label lbl_email = new Label("Email");
		txt_email = new TextField();
		txt_email.setPromptText("Enter Email");

		Label lbl_password = new Label("Password");
		pass = new PasswordField();
		pass.setPromptText("Enter Password");

		Label lbl_specialization = new Label("Specialization");
		cmb_specialization = new ComboBox<>();
		cmb_specialization.getItems().addAll("Neurologist", "Physiotherapist", "Surgeon", "General Physician");
		cmb_specialization.setPromptText("Select Specialization");

		Button btn_signup = new Button();
		btn_signup.setText("ADD");
		btn_signup.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					if (validateFields()) {
//						String firstName = txt_fName.getText();
//						String lastName = txt_lName.getText();
//						String gender = rb_male.isSelected() ? "Male" : "Female";
//						String dob = cmb_year.getValue() + "-" + (cmb_month.getSelectionModel().getSelectedIndex() + 1)
//								+ "-" + cmb_date.getValue();
//						String email = txt_email.getText();
//						String password = pass.getText();
//						String specialization = cmb_specialization.getValue();
//
//						Database.insertDoctor(firstName, lastName, gender, dob, email, password, specialization);
						Database.insertIntoDb(getDoctorDetails(), "doctor");
						// Show a success message
						Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
						successAlert.setContentText("Doctor added successfully!");
						successAlert.showAndWait();
					} else {
						Alert errorAlert = new Alert(Alert.AlertType.ERROR);
						errorAlert.setContentText("Please fill out all the fields.");
						errorAlert.showAndWait();
					}
				} catch (SQLException e) {
					e.printStackTrace();
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
					errorAlert.setContentText(Database.getErrorMessage(e));
					errorAlert.showAndWait();
				} catch (Exception e) {
					e.printStackTrace();
					Alert errorAlert = new Alert(Alert.AlertType.ERROR);
					errorAlert.setContentText("Error occurred while adding the doctor. Please try again.");
					errorAlert.showAndWait();
				}
			}

			// Validate
			private boolean validateFields() {
				return !txt_fName.getText().isEmpty() && !txt_lName.getText().isEmpty()
						&& (rb_male.isSelected() || rb_female.isSelected()) && cmb_date.getValue() != null
						&& cmb_month.getValue() != null && cmb_year.getValue() != null && !txt_email.getText().isEmpty()
						&& !pass.getText().isEmpty() && cmb_specialization.getValue() != null;
			}
		});

		HBox gender = new HBox(20, rb_female, rb_male);
		gender.setAlignment(Pos.CENTER);
		HBox date = new HBox(20, cmb_date, cmb_month, cmb_year);
		date.setAlignment(Pos.CENTER);

		VBox layout = new VBox(20, mainHeading, lbl_firstName, txt_fName, lbl_lastName, txt_lName, lbl_gender, gender,
				lbl_dob, date, lbl_email, txt_email, lbl_password, pass, lbl_specialization, cmb_specialization,
				btn_signup);
		layout.setAlignment(Pos.CENTER);

		btn_signup.setPadding(new Insets(5, 40, 5, 40));
		btn_signup.setStyle("-fx-font-size: 30px;-fx-background-color: #D3D3D3;");
		DropShadow shade2 = new DropShadow();
		btn_signup.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btn_signup.setEffect(shade2);
			}
		});
		btn_signup.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btn_signup.setEffect(null);
			}
		});
		btn_signup.setCursor(Cursor.HAND);

		Button backBtn = new Button("Go Back");
		backBtn.setCursor(Cursor.HAND);
		HBox goBack = new HBox(backBtn);
		backBtn.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
		backBtn.setTextFill(Color.WHITE);
		backBtn.setStyle("-fx-background-color: blue; -fx-background-radius: 20px;");
		backBtn.setPadding(new Insets(0, 20, 0, 20));
		goBack.setMargin(backBtn, new Insets(5, 0, 0, 3));

		DropShadow shadowback = new DropShadow();
		backBtn.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				backBtn.setEffect(shadowback);
			}
		});
		backBtn.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				backBtn.setEffect(null);
			}
		});

		backBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				AdminDashboard adminDashboard = new AdminDashboard();
				try {
					adminDashboard.start(stage);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		layout.setAlignment(Pos.CENTER);
		VBox v = new VBox(10, goBack, layout);
		v.setMargin(layout, new Insets(150, 0, 0, 0));
		v.setStyle("-fx-background-color: #2B7490");

		signUpScene = new Scene(v, 1800, 980);
		stage.setResizable(false);
		stage.setScene(signUpScene);
		stage.show();

	}
}