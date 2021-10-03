package com.skullzbones.novelnation.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.skullzbones.novelnation.MainActivity;
import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.POJO.Chapter;
import com.skullzbones.novelnation.Room.BookDao;

import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

public class API_CALL_LINKS {
    private static final int TIMEOUT_NETWORK_REQUESTS = 5000;
    private static final String TAG = "c/API_CALL_LINKS";
    public static String base = "https://boxnovel.com/";
    public static String novel = "https://boxnovel.com/novel/";

    public static String orderBy_key = "m_orderby";
    public static String order_Latest = "latest";
    public static String order_Alphabet = "alphabet";
    public static String order_Rating = "rating";
    public static String order_Trending = "trending";
    public static String order_Views = "views";
    public static String order_New = "new-manga";


    public static String SCRAPE_CSS_BOOK_CATE = "body > div.wrap > div > div > div > div > div > div > div.main-col.col-md-8.col-sm-8 > div.main-col-inner > div > div.c-page__content > div.tab-content-wrap > div > div > div > div > div > div";
    public static int BOOK_RATE = 23;
    public static int BOOK_RANK = 24;
    public static int BOOK_AUTHOR = 25;
    public static int BOOK_GENRE = 26;
    public static int BOOK_TYPE = 27;
    public static int BOOK_SUMMARY = 28;

    private static final String SCRAPE_TAGBOX_FROM_URL = "body > div.wrap > div > div > div > div.profile-manga.summary-layout-1 > div > div > div > div.tab-summary";
    private static final String SCRAPE_ID_FROM_URL = "body > div.wrap > div > div > div > div.profile-manga.summary-layout-1 > div > div > div > div.tab-summary > div.summary_content_wrap > div > div.post-content > div.post-rating > input";
    private static final String SCRAPE_CSS_BOOK_INNER_DETAILS = "body > div.wrap > div > div > div > div.profile-manga.summary-layout-1 > div > div > div > div.tab-summary > div.summary_content_wrap > div > div.post-content > div > div.summary-content";
    private static final String SCRAPE_CSS_BOOK_DETAILS = "body > div.wrap > div > div > div > div.profile-manga.summary-layout-1 > div > div > div > div.tab-summary > div.summary_content_wrap > div > div.post-content";
    private static final String SCRAPE_CSS_BOOK_SUMMARY = "body > div.wrap > div > div > div > div.c-page-content.style-1 > div > div > div > div.main-col.col-md-8.col-sm-8 > div.main-col-inner > div > div > div.description-summary";
    private static final String SCRAPE_CSS_BOOK_CHAPTER = "#manga-chapters-holder > div.page-content-listing.single-page > div > ul > li";
    private static final String SCRAPE_CSS_BOOK_CHAPTER_NEW_PROTO = "div.page-content-listing.single-page > div > ul > li";
    private static final String SCRAPE_SEARCH_QUERY_RESULTS = "body > div.wrap > div > div > div.c-page-content > div > div > div > div > div.main-col-inner > div > div.tab-content-wrap > div > div";
    private static final String SCRAPE_CSS_BOOK_CHAPTER_READ = "body > div.wrap > div > div > div > div > div > div > div > div > div.c-blog-post > div.entry-content > div > div > div.reading-content";
    private static final String SCRAPE_CSS_BOOK_NAVIGATION = "body > div.wrap > div > div > div > div > div > div > div > div > div.c-select-bottom > div > div.select-pagination > div";

    public static String KEY_SEARCH_MANGA = "s";
    public static String KEY_POSTYPE = "post_type";
    public static String VALUE_POSTYPE = "wp-manga";

    public static void parseCategoriesData(Context context, String type, CallBacks callBacks) {
        Ion.with(context)
                .load(API_CALL_LINKS.novel)
                .setTimeout(TIMEOUT_NETWORK_REQUESTS)
                .addQuery(API_CALL_LINKS.orderBy_key, type)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            Document doc = Jsoup.parse(result);
                            Elements divTag = doc.select(API_CALL_LINKS.SCRAPE_CSS_BOOK_CATE);
                            if (!divTag.hasText()) {
                                ErrorToastER.makeToast(context, ErrorToastER.TOAST_MESSAGE.TOAST_ERROR_LOADING_EXPLORE);
                                return;
                            }
                            ArrayList<Book> list = new ArrayList<>();
                            for (Element element : divTag) {
                                Element pageDetails = element.getElementsByClass("page-item-detail").first();
                                Element recur = pageDetails.getElementsByClass("item-thumb  c-image-hover").first();

                                String nuid = recur.attr("id");
                                int inte = Integer.parseInt(nuid.split("-")[2]);

                                Element imageHead = recur.getElementsByTag("a").first().select("img").first();
                                String link = imageHead.absUrl("src");

                                Element summary = pageDetails.getElementsByClass("item-summary").first();
                                Element front = summary.select("div.post-title.font-title > h3 > a").first();

                                String urlLink = front.attr("href");
                                String title = front.html();

                                Element rating = summary.select("div.meta-item.rating > div.post-total-rating > span.score.font-meta.total_votes").first();
                                String ratin = rating.html();

                                list.add(new Book(inte, title, getBookProgress(inte), urlLink, link, ratin, null));
                            }
                            callBacks.execute(list);
                        } catch (Exception exc) {
                            Toast.makeText(context, "Error? Try restarting app :(", Toast.LENGTH_SHORT).show();
                            exc.printStackTrace();
                        }
                    }
                });
    }

    private static String getBookProgress(int bookId) {
        int low = 0;
        int high = 0;

        Integer c = MainActivity.appDatabase.chapterDao().countChaptersTotal(bookId);
        if(c!=null){
            Integer r = MainActivity.appDatabase.chapterDao().countChaptersRead(bookId);
            if(r!=null){
                low = r;
                high = c;
            }
        }

        return low+"/"+high;
    }

    public static void parseBookData(Context context, Book book, CallBackBookItem callBacks) {
        Hashtable<Integer, String> data = new Hashtable<>();
        ArrayList<Chapter> chapters = new ArrayList<>();

        Utility.updateTimestamp(book.uid);

        Ion.with(context)
                .load(book.Url)
                .setTimeout(TIMEOUT_NETWORK_REQUESTS)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        chapters.addAll(MainActivity.appDatabase.chapterDao().getChaptersOfID(book.uid));
                        String latestStrin = null;
                        if(chapters.size()>0) {
                            latestStrin = chapters.get(0).chapter_link;
                        }
                        try {
                            Document doc = Jsoup.parse(result);
                            Elements sumdata = doc.select(API_CALL_LINKS.SCRAPE_CSS_BOOK_INNER_DETAILS);
                            data.put(BOOK_RATE, sumdata.get(0).text());
                            data.put(BOOK_RANK, sumdata.get(1).text());
                            data.put(BOOK_AUTHOR, sumdata.get(3).text());
                            data.put(BOOK_GENRE, sumdata.get(4).text());
                            data.put(BOOK_TYPE, sumdata.get(5).text());
                            Element summary = doc.select(API_CALL_LINKS.SCRAPE_CSS_BOOK_SUMMARY).first();
                            data.put(BOOK_SUMMARY, summary.text());

                            getAndParseChaptersAJAX(context, book, chapters, latestStrin, data, callBacks);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            callBacks.fetchBookData(data, chapters);
                        }
                    }

                });
    }

    private static void getAndParseChaptersAJAX(Context context, Book book, ArrayList<Chapter> chapters, String latestStrin, Hashtable<Integer, String> data, CallBackBookItem callBackBookItem) {
        Ion.with(context)
            .load("POST",book.Url + "ajax/chapters/")
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    try{
                        String response = "<html><body>" + result + "</body></html>";
                        Document doc = Jsoup.parse(response);
                        Elements chaps = doc.select(API_CALL_LINKS.SCRAPE_CSS_BOOK_CHAPTER_NEW_PROTO);
                        for (Element chap : chaps) {
                            String link = chap.getElementsByTag("a").first().attr("href");
                            String text = chap.getElementsByTag("a").text();
                            String rd = chap.getElementsByClass("chapter-release-date").text();
                            if (link.equals(latestStrin))
                                break;
                            Chapter c = new Chapter(book.uid, link, text, rd, null, false);
                            chapters.add(c);
                            MainActivity.appDatabase.chapterDao().insert(c);
                        }
                    } catch (Exception es){
                        es.printStackTrace();
                    }
                    Log.d(TAG, "I waz here");
                    Log.d(TAG, chapters.toString());
                    callBackBookItem.fetchBookData(data, chapters);
                }
            });
    }

    public static void parseChapterData(Context context, String url, CallBackChapterDataParse callBackChapterDataParse) {
        new Runnable() {
            @Override
            public void run() {
                Chapter chapter = MainActivity.appDatabase.chapterDao().getChapterFromLink(url);
                chapter.isRead = true;
                if(chapter.content!=null && !chapter.content.isEmpty()){
                    callBackChapterDataParse.execute(chapter);
                    return;
                }
                Ion.with(context)
                        .load(chapter.chapter_link)
                        .setTimeout(TIMEOUT_NETWORK_REQUESTS)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                try {
                                    Document doc = Jsoup.parse(result);
                                    Elements chapterData = doc.select(API_CALL_LINKS.SCRAPE_CSS_BOOK_CHAPTER_READ).first().getElementsByTag("p");
                                    String content = "";
                                    for(Element element: chapterData) {
                                        content += element.text() + "\n\n";
                                    }
                                    chapter.content = content;
                                    MainActivity.appDatabase.chapterDao().update(chapter);
                                    callBackChapterDataParse.execute(chapter);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                        });
            }
            }.run();

    }

    public static void getPrevBookUrl(int bookId, String url, CallBackPrev callBacks) {
        new Runnable() {
            @Override
            public void run() {
                String rLink = MainActivity.appDatabase.chapterDao().LeftLink(bookId,url);
                callBacks.execute(rLink);
            }
        }.run();
    }

    public static void getNextBookUrl(int bookId, String url, CallBackNext callBacks) {
        new Runnable() {
            @Override
            public void run() {
                String rLink = MainActivity.appDatabase.chapterDao().RightLink(bookId, url);
                callBacks.execute(rLink);
            }
        }.run();

    }

    public static void parseBookSearchData(Context context, String query, CallBackSearchData callBacks) {
        Hashtable<Integer, String> data = new Hashtable<>();
        ArrayList<Book> books = new ArrayList<>();

        Ion.with(context)
                .load(base)
                .addQuery(KEY_SEARCH_MANGA, query)
                .addQuery(KEY_POSTYPE, VALUE_POSTYPE)
                .setTimeout(TIMEOUT_NETWORK_REQUESTS)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        try {
                            Document doc = Jsoup.parse(result);
                            Elements pageData = doc.select(API_CALL_LINKS.SCRAPE_SEARCH_QUERY_RESULTS);
                            for (Element chap : pageData) {
                                Element imag = chap.select("div.col-4.col-12.col-md-2 > div > a").first();

                                String url = imag.attr("href");
                                String bookTitle = imag.attr("title");
                                String imageLink = imag.getElementsByTag("img").first().absUrl("src");

                                Element rat = chap.select("div.col-8.col-12.col-md-10 > div.tab-meta > div.meta-item.rating").first();
                                String rating = rat.text();
                                //int id = getUniqIDfromLink(context, url);
                                books.add(new Book(null,bookTitle,"0/0",url,imageLink,rating,null));
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        callBacks.execute(books);
                    }

                });
    }

    public static void getUniqIDfromLink(Context context, Book book, CallBackGetBookID callBackGetBookID) {
        new Runnable() {
            @Override
            public void run() {
                Integer did = MainActivity.appDatabase.bookDao().titletoId(book.bookTitle);
                if (did != null) {
                    callBackGetBookID.execute(did);
                    return;
                }
                Ion.with(context)
                        .load(book.Url)
                        .setTimeout(TIMEOUT_NETWORK_REQUESTS)
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                try {
                                    Document doc = Jsoup.parse(result);
                                    Element liD = doc.select(API_CALL_LINKS.SCRAPE_ID_FROM_URL).first();
                                    String idStrn = liD.attr("value");
                                    book.uid = Integer.valueOf(idStrn);
                                    MainActivity.appDatabase.bookDao().insert(book);
                                    callBackGetBookID.execute(Integer.valueOf(idStrn));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
            }
        }.run();
    }
}
