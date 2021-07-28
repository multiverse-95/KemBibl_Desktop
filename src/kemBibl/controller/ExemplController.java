package kemBibl.controller;

import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import com.sun.jndi.toolkit.url.Uri;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import kemBibl.model.BookModel;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.*;
import java.awt.TextField;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExemplController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane Exemp_InfoAll;

    @FXML
    private StackPane ExemplContainer;

    @FXML
    private ImageView img_prev;

    @FXML
    private Label author_text;

    @FXML
    private Label info_text;

    @FXML
    private Label typeBook;

    @FXML
    private Label dateBook;

    @FXML
    private Button readBook_b;

    @FXML
    private WebView annotation_text;

    @FXML
    void initialize() {
        readBook_b.setOnMouseEntered(event_mouse -> {
            ((Node) event_mouse.getSource()).setCursor(Cursor.HAND);
        });

    }

    public static class BookExemplInfoTask extends Task<String> {
        private final String IdBook;


        public BookExemplInfoTask(String IdBook) {
            this.IdBook = IdBook;
        }
        @Override
        protected String call() throws Exception {
            String resultBook = getBookExemplInfo(IdBook);
            String BookInfo=ParsingBookInfo(resultBook);
            return BookInfo;
        }
    }

    public void openFilePdf(String urlBookPdf) throws Exception {

        readBook_b.setOnAction((ActionEvent event) -> {
            try {
                Desktop.getDesktop().browse(new URL(urlBookPdf).toURI());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

    }
public void ShowInfoBook(String IdBook, String AuthorBook, String TitleBook, String typeOfBook, String DateOfBook, Image ImageBook, String urlBookPdf){
    author_text.setText(AuthorBook);
    info_text.setText(TitleBook);
    typeBook.setText("Тип: "+typeOfBook);
    dateBook.setText("Дата выхода: "+DateOfBook);
    img_prev.setImage(ImageBook);

    System.out.println("URL PDF "+urlBookPdf);
    if (urlBookPdf.equals("")){
        readBook_b.setDisable(true);
    } else {
        try {
            openFilePdf(urlBookPdf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    ProgressIndicator pi = new ProgressIndicator();
    pi.setStyle("-fx-accent: blue");
    VBox box = new VBox(pi);
    box.setAlignment(Pos.CENTER);
    // Grey Background
    ExemplContainer.getChildren().add(box);
    ExemplContainer.setDisable(true);

    Task BookExemplInfoTask = new BookExemplInfoTask(IdBook);

    // После выполнения потока
    BookExemplInfoTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        @Override
        public void handle(WorkerStateEvent event) {
            box.setDisable(true);
            pi.setVisible(false);
            ExemplContainer.setDisable(false);

            String BookInfo= (String) BookExemplInfoTask.getValue();
            //System.out.println(BookInfo);
            annotation_text.getEngine().loadContent(BookInfo, "text/html");
        }
    });
    // Запуск потока
    Thread BookInfoThread = new Thread(BookExemplInfoTask);
    BookInfoThread.setDaemon(true);
    BookInfoThread.start();


}

public static String getBookExemplInfo(String IdBook){
    HttpClient httpclient = new DefaultHttpClient();

    HttpResponse response;
    String responseString = null;
    String statusline=null;
    String urlAddress="http://catalog.kembibl.ru/notices/getIsbdAjax/"+IdBook+"/default?IdNotice="+IdBook+"&source=default";
    HttpGet httpGet=new HttpGet(urlAddress);
    try {
        response = httpclient.execute(httpGet);
        StatusLine statusLine = response.getStatusLine();
        statusline=response.getStatusLine().toString();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            responseString = out.toString("UTF-8");
            out.close();
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    } catch (ClientProtocolException e) {
        //TODO Handle problems..
    } catch (IOException e) {
        //TODO Handle problems..
    }
    //return responseString;
    return responseString;
}

public static String ParsingBookInfo(String responseBook){
    Document doc = null;
    String descr="";

    doc = Jsoup.parse(responseBook, "UTF-8");
    Element form = doc.getElementById("ISBD");
    String htmlISBD=form.html();
    //Далее идет работа с регулярными выражениями
    //Поиск и замена ссылок
    String REGEX22 = "<a href=(\\w*\\s+\"|:?)(.*?)>";
    String INPUT22 = htmlISBD;
    String REPLACE22 = "";

    Pattern p22 = Pattern.compile(REGEX22);
    Matcher m22 = p22.matcher(INPUT22);   // get a matcher object
    INPUT22 = m22.replaceAll(REPLACE22);
    htmlISBD=INPUT22;
    htmlISBD=htmlISBD.replace("</a>", "");
    //Toast toast = Toast.makeText(ctx, htmlISBD, Toast.LENGTH_LONG);
    //toast.show();


    String Text_exempl=form.text();
    //String Text_descr=form.text();
    String Text_descr=htmlISBD;
    // Поиск и удаление лишней информации связанной с вертикальными и горизонтальными связями
    String INPUT0 = Text_descr;
    String REGEX0 = "\\ББК[\\s\\S]*";
    String REPLACE0 = "";

    Pattern p0 = Pattern.compile(REGEX0);
    Matcher m0 = p0.matcher(INPUT0);   // get a matcher object
    INPUT0 = m0.replaceAll(REPLACE0);
    Text_descr=INPUT0;

    //System.out.println(responseBook);
    return Text_descr;
}


}
