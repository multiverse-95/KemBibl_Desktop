package kemBibl;

import com.jfoenix.controls.JFXTabPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import kemBibl.controller.ExemplController;
import kemBibl.controller.SearchController;
import kemBibl.model.BookModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class appController {

    @FXML
    private StackPane root;
    @FXML
    private JFXTabPane tabContainer;


    @FXML
    private Tab searchTab;

    @FXML
    private AnchorPane searchContainer;

    @FXML
    private ListView<BookModel> books_list;

    @FXML
    private TextField search_text;

    @FXML
    private Button search_button;

    @FXML
    private CheckBox onlyOnlineBooks_ch;

    @FXML
    private Tab userProfileTab;

    @FXML
    private AnchorPane userProfileContainer;

    @FXML
    private Tab settingsTab;

    @FXML
    private AnchorPane settingsContainer;

    @FXML
    private Tab logoutTab;

    private double tabWidth = 90.0;
    public static int lastSelectedTabIndex = 0;


    private final Image IMAGE_BOOK  = new Image("kemBibl/images/book_icon.png");

    private Image[] listOfImages = {IMAGE_BOOK};
    @FXML
    void initialize() {
        configureView();
        search_button.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });
        onlyOnlineBooks_ch.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

        search_button.setOnAction(event -> {
            SearchBooks();
        });
        search_text.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)  {
                SearchBooks(); // ?????????? ?????????????? ??????????????????????
            }
        });
    }

    public void SearchBooks(){
        books_list.setItems(null);

        String search=search_text.getText();
        String onlyOnlineBooks="";
        if (onlyOnlineBooks_ch.isSelected()){
            onlyOnlineBooks="1";
        }
        if (search_text.getText().isEmpty()){
            Alert alert =new Alert(Alert.AlertType.WARNING , "Test");
            alert.setTitle("????????????????????"); // ???????????????? ????????????????????????????
            alert.setHeaderText("?????????????? ????????????!"); // ?????????? ????????????????????????????
            alert.setContentText("?????????????? ???????????? ?? ???????? ????????????!");
            // ?????????? ?????????????????????????? ????????????????
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {

                }
            });
        } else {

            // ???????????? ?????????????????? ??????????????????
            ProgressIndicator pi = new ProgressIndicator();
            pi.setStyle("-fx-accent: blue");
            VBox box = new VBox(pi);
            box.setAlignment(Pos.CENTER);
            // Grey Background
            root.getChildren().add(box);

            searchContainer.setDisable(true);
            Task SearchTask = new SearchController.SearchTask(search,"", onlyOnlineBooks);

            // ?????????? ???????????????????? ????????????
            SearchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    // ???????????????? ?????????????????? ??????????????????
                    box.setDisable(true);
                    pi.setVisible(false);
                    searchContainer.setDisable(false);
                    ArrayList<BookModel> bookModels = (ArrayList<BookModel>) SearchTask.getValue();
                    System.out.println("Size of search "+bookModels.size());
                    if (bookModels.size()==0){
                        Alert alert =new Alert(Alert.AlertType.WARNING , "Test");
                        alert.setTitle("????????????????????"); // ???????????????? ????????????????????????????
                        alert.setHeaderText("???????????? ???? ??????????????!"); // ?????????? ????????????????????????????
                        alert.setContentText("???????????????????? ???????????????? ????????????!");
                        // ?????????? ?????????????????????????? ????????????????
                        alert.showAndWait().ifPresent(rs -> {
                            if (rs == ButtonType.OK) {

                            }
                        });
                    }

                    ObservableList<BookModel> data = FXCollections.observableArrayList();
                    data.addAll(bookModels);

                    books_list.setCellFactory(listView -> new CustomListCell());

                    books_list.setItems(data);
                    books_list.setOnMouseEntered(event_mouse -> {
                        ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
                    });
                    books_list.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                                BookModel bookModel = books_list.getSelectionModel().getSelectedItem();
                                String id_book=bookModel.getIdBook();
                                String author_book=bookModel.getAuthor();
                                String titlebook=bookModel.getTitle();
                                String typeBook=bookModel.getTypeBook();
                                String dateBook=bookModel.getDate();
                                String urlBookPdf=bookModel.getUrlBookPdf();

                                System.out.println("IdBook: "+id_book);
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/kemBibl/view/exempl_book.fxml"));
                                try {
                                    loader.load();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Parent root = loader.getRoot();
                                // ?????????? ?????????????? ?????????????????? ????????????????????
                                ExemplController exemplController = loader.getController();
                                exemplController.ShowInfoBook(id_book, author_book, titlebook , typeBook, dateBook, listOfImages[0], urlBookPdf);
                                // ?????????????? ???????? ????????????????????
                                Stage stage = new Stage();
                                stage.setTitle("???????????????????? ???? ??????????");
                                //stage.setResizable(false);
                                stage.setScene(new Scene(root));
                                stage.showAndWait();


                            }
                        }
                    });


                }
            });
            // ???????????? ????????????
            Thread SearchThread = new Thread(SearchTask);
            SearchThread.setDaemon(true);
            SearchThread.start();
        }

    }


    private class CustomListCell extends ListCell<BookModel> {
        private HBox content;
        private ImageView imageView = new ImageView();
        private Text author;
        private Text titleBook;

        public CustomListCell() {
            super();
            author = new Text();
            titleBook = new Text();
            VBox vBox = new VBox(author, titleBook);
            vBox.setSpacing(10);
            content = new HBox(imageView, vBox);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(BookModel item, boolean empty) {

            super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {

                    imageView.setImage(listOfImages[0]);
                    double imageWidth = 70.0;
                    imageView.setFitHeight(imageWidth);
                    imageView.setFitWidth(imageWidth);
                    author.setText(item.getAuthor());
                    author.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
                    titleBook.setText(item.getTitle());
                    titleBook.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
                    setGraphic(content);
                }

        }
    }

    private void configureView() {
        /// 3.
        tabContainer.setTabMinWidth(tabWidth);
        tabContainer.setTabMaxWidth(tabWidth);
        tabContainer.setTabMinHeight(tabWidth);
        tabContainer.setTabMaxHeight(tabWidth);
        tabContainer.setRotateGraphic(true);

        EventHandler<Event> replaceBackgroundColorHandler = event -> {
            lastSelectedTabIndex = tabContainer.getSelectionModel().getSelectedIndex();

            Tab currentTab = (Tab) event.getTarget();
            if (currentTab.isSelected()) {
                currentTab.setStyle("-fx-background-color: -fx-focus-color;");
            } else {
                currentTab.setStyle("-fx-background-color: -fx-accent;");
            }
        };

        EventHandler<Event> logoutHandler = event -> {
            Tab currentTab = (Tab) event.getTarget();
            if (currentTab.isSelected()) {
                tabContainer.getSelectionModel().select(lastSelectedTabIndex);

                // TODO: logout action

                // good place to show Dialog window with Yes / No question
                System.out.println("Logging out!");
                ButtonType yes_del = new ButtonType("????", ButtonBar.ButtonData.OK_DONE); // ???????????????? ???????????? ??????????????????????
                ButtonType no_del = new ButtonType("??????", ButtonBar.ButtonData.CANCEL_CLOSE); // ???????????????? ???????????? ????????????????
                Alert alert =new Alert(Alert.AlertType.CONFIRMATION , "Test", yes_del, no_del);
                alert.setTitle("?????????? ???? ????????????????????"); // ???????????????? ????????????????????????????
                alert.setHeaderText("?????????????????????? ?????????? ???? ????????????????????!"); // ?????????? ????????????????????????????
                alert.setContentText("???? ?????????????????????????? ???????????? ?????????? ???? ?????????????????????");
                // ?????????? ?????????????????????????? ????????????????
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == yes_del){
                        Platform.exit();
                        System.exit(0);
                    }
                });
            }
        };

        /// 4.

        configureTab(searchTab, "??????????\n????????", "kemBibl/images/search_book.png", searchContainer ,getClass().getResource("search.fxml"), replaceBackgroundColorHandler);
        configureTab(userProfileTab, "????????????\n??????????????", "kemBibl/images/user.png", userProfileContainer ,getClass().getResource("userprofile.fxml"), replaceBackgroundColorHandler);
        configureTab(settingsTab, "??????????????????", "kemBibl/images/settings.png", settingsContainer, getClass().getResource("settings.fxml"), replaceBackgroundColorHandler);
        configureTab(logoutTab, "??????????", "kemBibl/images/logout.png", null, null, logoutHandler);

        searchTab.setStyle("-fx-background-color: -fx-focus-color;");

    }

    private void configureTab(Tab tab, String title, String iconPath, AnchorPane containerPane, URL resourceURL, EventHandler<Event> onSelectionChangedEvent) {
        double imageWidth = 40.0;

        /// 5.
        ImageView imageView = new ImageView(new Image(iconPath));
        imageView.setFitHeight(imageWidth);
        imageView.setFitWidth(imageWidth);

        Label label = new Label(title);
        label.setMaxWidth(tabWidth - 20);
        label.setPadding(new Insets(5, 0, 0, 0));
        label.setStyle("-fx-text-fill: black; -fx-font-size: 10pt; -fx-font-weight: normal;");
        label.setTextAlignment(TextAlignment.CENTER);

        BorderPane tabPane = new BorderPane();
        tabPane.setRotate(90.0);
        tabPane.setMaxWidth(tabWidth);
        tabPane.setCenter(imageView);
        tabPane.setBottom(label);

        /// 6.
        tab.setText("");
        tab.setGraphic(tabPane);
        tab.setOnSelectionChanged(onSelectionChangedEvent);

        if (containerPane != null && resourceURL != null) {
            try {
                Parent contentView = FXMLLoader.load(resourceURL);
                containerPane.getChildren().add(contentView);
                AnchorPane.setTopAnchor(contentView, 0.0);
                AnchorPane.setBottomAnchor(contentView, 0.0);
                AnchorPane.setRightAnchor(contentView, 0.0);
                AnchorPane.setLeftAnchor(contentView, 0.0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
