package kemBibl;

import kemBibl.model.BookModel;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//Класс отвечает за парсинг XML
public class ParsingXML {

    ArrayList<BookModel> bookModels = new ArrayList<>();

    String[] items;
    String[] IdBooks;
    String[] nameBook;
    String[] authorBook;
    String[] typeBook;
    String[] dateBook;


    public ArrayList<BookModel> ParseXML(String result){

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setValidating(false);
        DocumentBuilder builder;
        String error="";

        InputSource xml_source = new InputSource(new StringReader(result));
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xml_source); //Открытие файла для парсинга
            doc.getDocumentElement().normalize();
            //Получаем корневой элемент
            error=doc.getDocumentElement().getNodeName();


            // получаем узлы с именем notice
            // теперь XML полностью загружен в память
            // в виде объекта Document
            NodeList nodeList = doc.getElementsByTagName("notice"); //Список элементов с тегом notice
            items=new String[nodeList.getLength()];
            IdBooks=new String[nodeList.getLength()];

            nameBook=new String[nodeList.getLength()];
            authorBook=new String[nodeList.getLength()];
            typeBook=new String[nodeList.getLength()];
            dateBook=new String[nodeList.getLength()];


            for (int i =0; i< nodeList.getLength(); i++)
            {
                Node fieldNode = nodeList.item(i); //Получаем книгу
                NamedNodeMap attributes = fieldNode.getAttributes(); // Для каждой книги получаем список атрибутов
                // Получаем значение конкретного атрибута
                String AuthorAtr = attributes.getNamedItem("Auteur").getNodeValue();
                String TitleBook = attributes.getNamedItem("Titre").getNodeValue();
                String TypeBook = attributes.getNamedItem("Libelle").getNodeValue();
                String DateBook = attributes.getNamedItem("Date").getNodeValue();
                String Image="";
                //String Totalexmpl=attributes.getNamedItem("total").getNodeValue();
                String IdBook= attributes.getNamedItem("IdNotice").getNodeValue();
                items[i]="Название книги: "+ TitleBook+"\n\nАвтор: "+ AuthorAtr+"\n\nТип: "+TypeBook+"\n\nДата выпуска: "+DateBook;

                if(TypeBook.equals("Выпуск")){
                    Image="newspaper_icon_2";
                } else if (TypeBook.equals("Статья")){
                    Image="article_icon";
                } else if (TypeBook.equals("Электронный ресурс ")){
                    Image="disc_icon_3";
                } else if (TypeBook.equals("Видеозапись")){
                    Image="video_icon";
                } else if (TypeBook.equals("Звукозапись")){
                    Image="sound_icon";
                } else {
                    Image="book_icon";
                }
                if (AuthorAtr.equals("")){
                    AuthorAtr="(автор не указан)";
                }
                bookModels.add(new BookModel(IdBook, AuthorAtr, TitleBook, DateBook, TypeBook, Image,"","","","","",""));

                IdBooks[i]=IdBook;
                nameBook[i]=TitleBook;
                authorBook[i]=AuthorAtr;
                typeBook[i]=TypeBook;
                dateBook[i]=DateBook;


            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }
        //return items;
        return bookModels;
    }

    public String[] IDBooks(){

        return IdBooks;
    }

    public String[] NameBook(){

        return nameBook;
    }
    public String[] AuthorBook(){

        return authorBook;
    }
    public String[] TypeBook(){

        return typeBook;
    }
    public String[] DateBook(){

        return dateBook;
    }
}
