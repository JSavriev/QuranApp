package sj.quran.app.db;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import sj.quran.app.db.model.BookmarkModel;

public class DBHelper {

    private final Realm realm = Realm.getDefaultInstance();

    public static DBHelper get() {
        return new DBHelper();
    }

    public void saveBookmarkPage(int page) {
        try {
            realm.executeTransaction(realm -> {
                BookmarkModel bookmarkModel = new BookmarkModel();
                bookmarkModel.setPage(page);
                bookmarkModel.setCreatedAt(Calendar.getInstance().getTimeInMillis());
                realm.copyToRealm(bookmarkModel);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BookmarkModel getBookmarkByPage(int page) {
        return realm.where(BookmarkModel.class)
                .equalTo("page", page)
                .findFirst();
    }

    public void deleteBookmark(int page) {
        try {
            realm.executeTransaction(realm -> {
                RealmResults<BookmarkModel> pages =
                        realm.where(BookmarkModel.class)
                                .equalTo("page", page)
                                .findAll();
                pages.deleteAllFromRealm();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BookmarkModel> getBookmarkPages() {
        RealmResults<BookmarkModel> words = Realm.getDefaultInstance()
                .where(BookmarkModel.class)
                .findAll()
                .sort("createdAt", Sort.DESCENDING);
        return new ArrayList<>(words);
    }
}
