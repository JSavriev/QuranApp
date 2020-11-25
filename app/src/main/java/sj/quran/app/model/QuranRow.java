package sj.quran.app.model;

public class QuranRow {

    public static final int NONE = 0;
    public static final int HEADER = 1;
    public static final int PAGE_BOOKMARK = 2;
    public static final int AYAH_BOOKMARK = 3;
    public static final int BOOKMARK_HEADER = 4;

    public int sura;
    public int ayah;
    public int page;
    public String text;
    public String metadata;
    public int rowType;
    public Integer imageResource;
    public Integer imageFilterColor;
    public Integer juzType;
    public String juzOverlayText;
    public long dateAddedInMillis;

    public static class Builder {
        private String text;
        private String metadata;
        private int sura;
        private int ayah;
        private int page;
        private int rowType = NONE;
        private Integer imageResource;
        private Integer juzType;
        private String juzOverlayText;
        private long dateAddedInMillis;
        private Integer imageFilterColor;

        public Builder withType(int type) {
            rowType = type;
            return this;
        }

        public Builder withText(String text) {
            this.text = text;
            return this;
        }

        public Builder withMetadata(String metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder withSura(int sura) {
            this.sura = sura;
            return this;
        }

        public Builder withPage(int page) {
            this.page = page;
            return this;
        }

        public Builder withImageResource(int resId) {
            imageResource = resId;
            return this;
        }

        public Builder withImageOverlayColor(int color) {
            imageFilterColor = color;
            return this;
        }

        public Builder withJuzType(int juzType) {
            this.juzType = juzType;
            return this;
        }

        public Builder withJuzOverlayText(String text) {
            juzOverlayText = text;
            return this;
        }

        public Builder withDate(long timeStamp) {
            dateAddedInMillis = timeStamp * 1000;
            return this;
        }

        public QuranRow build() {
            return new QuranRow(text, metadata, rowType, sura,
                    ayah, page, imageResource, imageFilterColor, juzType,
                    juzOverlayText, dateAddedInMillis);
        }
    }

    private QuranRow(String text, String metadata, int rowType,
                     int sura, int ayah, int page, Integer imageResource, Integer filterColor,
                     Integer juzType, String juzOverlayText,
                     long dateAddedInMillis) {
        this.text = text;
        this.rowType = rowType;
        this.sura = sura;
        this.ayah = ayah;
        this.page = page;
        this.metadata = metadata;
        this.imageResource = imageResource;
        this.imageFilterColor = filterColor;
        this.juzType = juzType;
        this.juzOverlayText = juzOverlayText;
        this.dateAddedInMillis = dateAddedInMillis;
    }

    public boolean isHeader() {
        return rowType == HEADER || rowType == BOOKMARK_HEADER;
    }

    public boolean isBookmarkHeader() {
        return rowType == BOOKMARK_HEADER;
    }

    public boolean isBookmark() {
        return rowType == PAGE_BOOKMARK || rowType == AYAH_BOOKMARK;
    }

    public boolean isAyahBookmark() {
        return rowType == AYAH_BOOKMARK;
    }
}
