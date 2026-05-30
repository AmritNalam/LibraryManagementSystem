
package model;
public class Book {
 private final String isbn;
 private String title,author;
 private int publicationYear;
 private boolean available=true;
 public Book(String t,String a,String i,int y){title=t;author=a;isbn=i;publicationYear=y;}
 public String getTitle(){return title;} public String getAuthor(){return author;}
 public String getIsbn(){return isbn;} public int getPublicationYear(){return publicationYear;}
 public boolean isAvailable(){return available;} public void setAvailable(boolean a){available=a;}
}
