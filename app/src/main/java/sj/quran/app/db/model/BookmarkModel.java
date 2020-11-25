package sj.quran.app.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BookmarkModel extends RealmObject {

    @PrimaryKey
    private int page;
    private long createdAt;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
