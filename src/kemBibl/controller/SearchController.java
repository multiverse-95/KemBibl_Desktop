package kemBibl.controller;

import javafx.concurrent.Task;
import kemBibl.ParsingXML;
import kemBibl.model.BookModel;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SearchController {


    public static class SearchTask extends Task<ArrayList<BookModel>> {
        private final String author;
        private final String title;

        public SearchTask(String author, String title) {
            this.author = author;
            this.title = title;
        }
        @Override
        protected ArrayList<BookModel> call() throws Exception {
            String result= SearchBooks(author);
            ArrayList<BookModel> bookModels = new ArrayList<>();
            bookModels= ParseXML(result);

            return bookModels;
        }
    }

    public static String SearchBooks(String author){
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://catalog.kembibl.ru/SearchForms/simpleSearch");
        String reqS = "";
        // Добавляем параметры и их значения для запроса
        ArrayList<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("data[1][oper]", "AND"));
        urlParameters.add(new BasicNameValuePair("data[1][field]", "200_A"));
        urlParameters.add(new BasicNameValuePair("data[1][value]", ""));
        urlParameters.add(new BasicNameValuePair("data[2][oper]", "AND"));
        urlParameters.add(new BasicNameValuePair("data[2][field]", "200_F"));
        urlParameters.add(new BasicNameValuePair("data[2][value]", author));
        urlParameters.add(new BasicNameValuePair("data[3][oper]", "AND"));
        urlParameters.add(new BasicNameValuePair("data[3][field]", "SUJET"));
        urlParameters.add(new BasicNameValuePair("data[3][value]", ""));
        urlParameters.add(new BasicNameValuePair("data[between][210_D][start]", ""));
        urlParameters.add(new BasicNameValuePair("data[between][210_D][end]", ""));
        urlParameters.add(new BasicNameValuePair("data[exist][856_U]", "0"));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][all]", "1"));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][1]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][4]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][5]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][6]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][7]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][9]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][10]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][12]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][13]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][18]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][19]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][26]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][27]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][30]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][31]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][37]", ""));
        urlParameters.add(new BasicNameValuePair("data[Notice][Categ][43]", ""));
        urlParameters.add(new BasicNameValuePair("data[libs][default][all]", "1"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][48]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][32]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][33]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][42]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][36]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][41]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][6]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][7]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][29]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][35]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][44]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][12]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][39]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][14]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][40]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][11]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][51]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][45]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][8]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][43]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][49]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][13]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][4]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][47]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][10]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][34]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][38]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][37]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][46]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][50]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][16]", "0"));
        urlParameters.add(new BasicNameValuePair("data[libs][default][2]", "0"));

        //Устанавливаем параметры запроса в кодировку UTF-8
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpclient.execute(post);
            reqS = EntityUtils.toString(response.getEntity());


            //System.out.println(reqS);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return reqS;
    }

    public static ArrayList<BookModel> ParseXML(String result){
        String[] items;
        String[] IdBooks;

        String[] nameBook;
        String[] authorBook;
        String[] typeBook;
        String[] dateBook;

        ArrayList<BookModel> bookModels = new ArrayList<>();

        ParsingXML parsingXML=new ParsingXML();
        //items=parsingXML.ParseXML(result);
        bookModels =parsingXML.ParseXML(result);
        IdBooks=parsingXML.IDBooks();
        nameBook=parsingXML.NameBook();
        authorBook=parsingXML.AuthorBook();
        typeBook=parsingXML.TypeBook();
        dateBook=parsingXML.DateBook();
        //System.out.println("IdBook: "+bookModels.get(0).getIdBook());
        return bookModels;
    }
}
