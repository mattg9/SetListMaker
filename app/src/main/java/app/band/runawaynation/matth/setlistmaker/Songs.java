package app.band.runawaynation.matth.setlistmaker;

import com.opencsv.bean.CsvBindByName;

class Songs {

        @CsvBindByName
        private final String songTitle;
        @CsvBindByName
        private final String bandName;
        @CsvBindByName
        private final String songLength;
        @CsvBindByName
        private final String songType;

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
