package com.company;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainFormController {
    public TableView tblStudent;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colMail;
    public TableColumn colContact;
    public TableColumn colAddress;
    public TableColumn colNic;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtMail;
    public JFXTextField txtContact;
    public JFXTextField txtAddress;
    public JFXTextField txtNic;

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));

        loadAllStudent();
    }


    public void stIdOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM student WHERE student_id=? ", txtId.getText());
            if (result.next()) {
                txtName.setText(result.getString(2));
                txtMail.setText(result.getString(3));
                txtContact.setText(result.getString(4));
                txtAddress.setText(result.getString(5));
                txtNic.setText(result.getString(6));


            } else {
                new Alert(Alert.AlertType.WARNING, "Empty set").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void btnSave(ActionEvent actionEvent) {
        Student student = new Student(
                txtId.getText(),
                txtName.getText(),
                txtMail.getText(),
                txtContact.getText(),
                txtAddress.getText(),
                txtNic.getText()
        );
        try {
            if (CrudUtil.execute("INSERT INTO student VALUES (?,?,?,?,?,?)",
                    student.getId(), student.getName(), student.getEmail(), student.getContact(), student.getAddress(), student.getNic())) {
                new Alert(Alert.AlertType.CONFIRMATION, "SAVE").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.CONFIRMATION, "Empty Result").show();
        }
    }

    public void btnUpdate(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student student = new Student(
                txtId.getText(),
                txtName.getText(),
                txtMail.getText(),
                txtContact.getText(),
                txtAddress.getText(),
                txtNic.getText()
        );
        boolean isStUpdated = CrudUtil.execute("UPDATE student SET student_name=?, email=?, contact=?, address=?, NIC=? WHERE student_id=?",
                //student.getId(),
                student.getName(),
                student.getEmail(),
                student.getContact(),
                student.getAddress(),
                student.getNic(),
                student.getId());

        if (isStUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }

        loadAllStudent();
        clearOnAction();
    }

    public void btnRemove(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student emp = (Student) tblStudent.getSelectionModel().getSelectedItem();
        tblStudent.getItems().remove(emp);

        boolean isEmDeleted=CrudUtil.execute("DELETE FROM student WHERE student_id=? ",
                emp.getId()

        );
        if (isEmDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }
    private void loadAllStudent() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM student");
        ObservableList<Student> oblist = FXCollections.observableArrayList();
        while (resultSet.next()) {
            oblist.add(
                    new Student(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getString(6)
                    )
            );
        }
        tblStudent.setItems(oblist);
        tblStudent.refresh();
    }
    private void clearOnAction() {
        txtId.clear();
        txtName.clear();
        txtMail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNic.clear();
    }
}
