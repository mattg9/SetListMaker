package app.band.runawaynation.matth.setlistmaker;

import com.opencsv.bean.CsvBindByName;

public class Songs {

        @CsvBindByName
        private String songTitle;
        @CsvBindByName
        private String bandName;
        @CsvBindByName
        private String songLength;
        @CsvBindByName
        private String songType;

        Songs(String songTitle, String bandName, String songLength, String songType) {
                this.songTitle = songTitle;
                this.bandName = bandName;
                this.songLength = songLength;
                this.songType = songType;
        }

        // Getters!
        public String getSongTitle() {
            return songTitle;
        }
        public String getBandName() {
            return bandName;
        }
        public String getSongLength() {
            return songLength;
        }
        public String getSongType() {
                return songType;
        }
}
