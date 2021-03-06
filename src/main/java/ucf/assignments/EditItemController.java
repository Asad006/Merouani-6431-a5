package ucf.assignments;
/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 first_name last_name
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

public class EditItemController implements Initializable {
    private SceneManager sceneManager;
    private ItemInventoryManager itemInventoryManager;
    private InventoryItemsController inventoryItemsController;
    private String serialNumber;

    @FXML
    private TextField valueEditTextField;

    @FXML
    private TextField serialNumberEditTextField;

    @FXML
    private TextField nameTextEditField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TextField valueEditTextField = new TextField();
        TextField serialNumberEditTextField = new TextField();
        TextField nameEditTextField = new TextField();

        valueEditTextField.setText("");
        serialNumberEditTextField.setText("");
        nameEditTextField.setText("");
        System.out.println("test");

    }

    public EditItemController(ItemInventoryManager itemInventoryManager, SceneManager sceneManager, InventoryItemsController inventoryItemsController) {
        this.itemInventoryManager = itemInventoryManager;
        this.sceneManager = sceneManager;
        this.inventoryItemsController = inventoryItemsController;
    }

    public EditItemController() {

    }

    @FXML
    private void cancelButtonClicked(ActionEvent event) {
        Stage stage = (Stage) valueEditTextField.getScene().getWindow();
        stage.close();

        valueEditTextField.clear();
        serialNumberEditTextField.clear();
        nameTextEditField.clear();
    }

    @FXML
    private void loadDataEditClicked(ActionEvent event) {
        // get the index of the selected row else show error message
        //load data by calling manager

        InventoryItem item = inventoryItemsController.getSelectedItem();
        if (item == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Error");
            alert.setHeaderText("The Selection of the row to edit is required.\n");
            alert.setContentText("Select the row to edit and try again. ");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    Stage stage = (Stage) valueEditTextField.getScene().getWindow();
                    stage.close();

                }
            });

        } else {
            System.out.println(item.itemName.toString());
            valueEditTextField.setText(item.getItemValue());
            serialNumberEditTextField.setText(item.getItemSerialNumber());
            nameTextEditField.setText(item.getItemName());

            serialNumber = serialNumberEditTextField.getText();
        }

    }

    @FXML
    private void saveEditButtonClicked(ActionEvent event) {
        // check if value is numeric value is no show error message
        // parse the string to double and apply rounding to  it
        // check if name valid if not show error message
        //check serial number is unique and has 8 characters id not show error message
        //add date to data in the manager
        if (itemInventoryManager.isNameIsValid(nameTextEditField.getText())) {
            if (itemInventoryManager.isNumericValue(valueEditTextField.getText().substring(1))) {
                Double valueNumber = Double.parseDouble(valueEditTextField.getText().substring(1));
                BigDecimal valueBigDecimal = new BigDecimal(valueNumber);
                BigDecimal valueDisplayMoney = valueBigDecimal.setScale(2, RoundingMode.HALF_EVEN);

                int index = inventoryItemsController.getSelectedIndex();

                if ((itemInventoryManager.isSerialNumberUnique(serialNumberEditTextField.getText()) ||
                        serialNumberEditTextField.getText().equals(serialNumber)) &&
                        itemInventoryManager.isSerialNumberIsValid(serialNumberEditTextField.getText())) {

                    InventoryItem editedItem = new InventoryItem("$" + valueDisplayMoney.toString(),
                            serialNumberEditTextField.getText(), nameTextEditField.getText());
                    itemInventoryManager.editItem(editedItem, index);

                    inventoryItemsController.updateTableView();

                    Stage stage = (Stage) valueEditTextField.getScene().getWindow();
                    stage.close();

                    valueEditTextField.clear();
                    serialNumberEditTextField.clear();
                    nameTextEditField.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Entry Error");
                    alert.setHeaderText("Unique serial number and must contains 10 (digital or letter) characters are required.\n");
                    alert.setContentText("The Item added could contains existing serial number or less 10 chars.");
                    alert.showAndWait();

                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Entry Error");
                alert.setHeaderText("Value Number format is required.\n");
                alert.setContentText("The value of the Item is a number. ");
                alert.showAndWait();

            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Entry Error");
            alert.setHeaderText("the must have 2 to 256 characters in length \n");
            alert.setContentText("Please. Try again. ");
            alert.showAndWait();
        }

    }
}
