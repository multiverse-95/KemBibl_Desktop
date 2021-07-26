package kemBibl.model;

public class BookModel {
    //property basics
    private String idBook;
    private String author;
    private String title;
    private String date;
    private String type_book;
    private String image;
    private String del_Url;
    private String add_Url;
    private String date_start;
    private String date_finish;
    private String code_book;
    private String filial;



    //конструктор
    public BookModel(String idBook, String author, String title, String date, String type_book, String image, String del_Url, String add_Url, String date_start, String date_finish, String code_book, String filial){

        this.idBook = idBook;
        this.author = author;
        this.title = title;
        this.date = date;
        this.type_book = type_book;
        this.image = image;
        this.del_Url = del_Url;
        this.add_Url=add_Url;
        this.date_start=date_start;
        this.date_finish=date_finish;
        this.code_book=code_book;
        this.filial=filial;
    }

    //getters
    public String getIdBook() {return idBook;}
    public String getAuthor() {return author; }
    public String getTitle() {return title; }
    public String getDate() {return date; }
    public String getTypeBook() {return type_book; }
    public String getImage() { return image; }
    public String getDel_Url() { return del_Url; }
    public String getAdd_Url() { return add_Url; }
    public String getDate_start() { return date_start; }
    public String getDate_finish() { return date_finish; }
    public String getCode_book() { return code_book; }
    public String getFilial() { return filial; }
}
