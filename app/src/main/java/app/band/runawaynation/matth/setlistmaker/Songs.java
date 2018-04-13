package app.band.runawaynation.matth.setlistmaker;

import com.opencsv.bean.CsvBindByName;

public class Songs {

        @CsvBindByName
        private String songTitle;
        @CsvBindByName
        private String bandName;
        @CsvBindByName
        private int songLength;

        public Songs(String songTitle, String bandName, int songLength) {
                this.songTitle = songTitle;
                this.bandName = bandName;
                this.songLength = songLength;
        }

        // Getters!
        public String getSongTitle() {
            return songTitle;
        }
        public String getBandName() {
            return bandName;
        }
        public int getSongLength() {
            return songLength;
        }
}
