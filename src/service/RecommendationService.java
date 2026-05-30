package service; import model.*; import java.util.*;
public class RecommendationService{
    public List<Book> recommend(Patron p, Collection<Book> books)
    { Map<String,Integer> freq=new HashMap<>();
        Set<String> borrowed=new HashSet<>();
        for(Book b:p.getBorrowingHistory()){freq.put(b.getAuthor(),freq.getOrDefault(b.getAuthor(),0)+1);
            borrowed.add(b.getIsbn());}
        String fav=null;
        int best=0;
            for(var e:freq.entrySet()) if(e.getValue()>best){best=e.getValue();
                fav=e.getKey();} List<Book> ans=new ArrayList<>();
            if(fav==null) return ans; for(Book b:books)
                if(fav.equals(b.getAuthor())&&!borrowed.contains(b.getIsbn())) ans.add(b);
                return ans; }}
