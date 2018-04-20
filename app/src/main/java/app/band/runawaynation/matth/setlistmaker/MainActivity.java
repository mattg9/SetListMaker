package app.band.runawaynation.matth.setlistmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Toast;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // the data
    private ArrayList<String[]> data = new ArrayList<>();
    private ArrayList<Songs> songs = new ArrayList<>();
    private ArrayList<List<String>> sets = new ArrayList<>();
    final private String downloadFolder = "/storage/emulated/0/Download/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // text field with output
        final TextView finalText = findViewById(R.id.finalText);

        // setup seek bars
        final SeekBar seekBarSetCount = findViewById(R.id.seekBarSetCount);
        final TextView setCountProgress = findViewById(R.id.setCount);
        setCountProgress.setText(String.valueOf(1));
        seekBarSetCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                setCountProgress.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        final SeekBar seekBarSetLength = findViewById(R.id.seekBarSetLength);
        final TextView setLengthProgress = findViewById(R.id.setLength);
        setLengthProgress.setText(String.valueOf(30));
        seekBarSetLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                setLengthProgress.setText(String.valueOf(progress*5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarSetCount.setProgress(1);
        seekBarSetLength.setProgress(30);

        final Button generateSets = findViewById(R.id.buttonGenerateSets);
        generateSets.setEnabled(false);

        // setup buttons
        EditText playlist = findViewById(R.id.editTextFile);
        final String filename = playlist.getText().toString();
        Button readCSV = findViewById(R.id.buttonFile);
        readCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    data.clear();
                    CSVReader reader = new CSVReader(new FileReader(downloadFolder + filename + ".csv"));
                    data = (ArrayList<String[]>) reader.readAll();
                    Toast.makeText(MainActivity.this, R.string.successfulRead, Toast.LENGTH_SHORT).show();
                    generateSets.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, R.string.failRead, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // TODO - no back to back long songs!
        generateSets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* INITIALIZATION */
                sets.clear(); songs.clear();
                String lastBand = "";
                int slowSongCount = 0, upbeatSongCount = 0;

                // create Songs from given data
                for (String [] s : data){
                    songs.add(new Songs(s[0], s[1], s[2], s[3]));
                }
                int setCount = Integer.parseInt(setCountProgress.getText().toString());
                int totalTime = Integer.parseInt(setLengthProgress.getText().toString());
                /* MAKE SETS */
                while (setCount > 0 && songs.size() > 1) {
                    int remainingTime = totalTime;
                    List<String> newSet = new ArrayList<>();
                    while (remainingTime > 0 && songs.size() > 1) {
                        // pick random song
                        Random rand = new Random();
                        int n = rand.nextInt(songs.size() - 1) + 1;
                        Songs songPicked = songs.get(n);
                        int songLength = Integer.parseInt(songPicked.getSongLength());
                        if (remainingTime > songLength) { // basic condition!
                            // no back to back bands
                            if (songPicked.getBandName().equals(lastBand)) continue;
                            float progress = (float) (totalTime - remainingTime) / totalTime;
                            // TRY not to start/end/interrupt with slow songs
                            if (!tooManySlow(songs)) {
                                if ((songPicked.getSongType().equals("Slow") && progress < 0.33) ||
                                        (songPicked.getSongType().equals("Slow") && progress > 0.75) ||
                                        (newSet.isEmpty() && songPicked.getSongType().equals("Slow")) ||
                                        (songPicked.getSongType().equals("Slow") && slowSongCount >= 2) ||
                                        (songPicked.getSongType().equals("Slow") && upbeatSongCount <= 2))
                                    continue;
                            }
                            // got one? okay let's carry on
                            remainingTime -= songLength;
                            newSet.add(songPicked.getSongType() + "\t\t\t\t" + songPicked.getSongTitle());
                            songs.remove(songPicked);
                            lastBand = songPicked.getBandName();
                            if (songPicked.getSongType().equals("Slow")) {
                                upbeatSongCount = 0;
                                slowSongCount++;
                            } else {
                                slowSongCount = 0;
                                upbeatSongCount++;
                            }
                        } else { break; } // populate a new set
                    }
                    sets.add(newSet);
                    setCount--;
                    // last set - still lots of time left?
                    if (setCount == 0 && remainingTime > 5)
                        Toast.makeText(MainActivity.this,
                                "Set time not matched.\nNot Enough Material!" ,
                                Toast.LENGTH_LONG).show();;
                }

                /* OUTPUT SETS */
                StringBuilder outputText = new StringBuilder();
                int outputSet = 1;
                for(List<String> set: sets) {
                    outputText.append("=== SET ").append(outputSet).append(" ===").append("\n");
                    for (String song : set) {
                        outputText.append(song).append("\n");
                    }
                    outputText.append("\n");
                    outputSet++;
                }
                if (outputSet <= Integer.parseInt(setCountProgress.getText().toString()))
                    Toast.makeText(MainActivity.this,
                            "Set count not matched.\nNot Enough Material!" ,
                            Toast.LENGTH_LONG).show();
                finalText.setText(outputText.toString());
                // DONE :)
            }
        });
    }

    private boolean tooManySlow(ArrayList<Songs> songs) {
        int slow = 0, upbeat = 0;
        for (Songs s : songs) {
            if (s.getSongType().equals("Slow")) {
                slow++;
            } else {
                upbeat++;
            }
        }
        return slow >= upbeat;
    }
}
