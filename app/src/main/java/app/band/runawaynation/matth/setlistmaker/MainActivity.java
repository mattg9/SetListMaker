package app.band.runawaynation.matth.setlistmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private ArrayList<String[]> songs = new ArrayList<>();
    private ArrayList<List> sets = new ArrayList<>();
    final private String downloadFolder = "/storage/emulated/0/Download/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                setLengthProgress.setText(String.valueOf(progress));
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

        // setup buttons
        EditText playlist = findViewById(R.id.editTextFile);
        final String filename = playlist.getText().toString();
        Button readCSV = findViewById(R.id.buttonFile);
        readCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    songs.clear();
                    CSVReader reader = new CSVReader(new FileReader(downloadFolder + filename + ".csv"));
                    songs = (ArrayList<String[]>) reader.readAll();
                    Toast.makeText(MainActivity.this, R.string.successfulRead, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, R.string.failRead, Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button generateSets = findViewById(R.id.buttonGenerateSets);
        generateSets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sets.clear();
                // make a copy
                ArrayList<String[]> songsCopy = new ArrayList<>(songs.size());
                for (String[] s : songs) { songsCopy.add(s.clone()); }
                // MASTER LOOP
                int setCount = seekBarSetCount.getProgress();
                while (setCount > 0) {
                    int setLength = seekBarSetLength.getProgress();
                    List<String> newSet = new ArrayList<>();
                    while (setLength > 0 && songsCopy.size() > 1) {
                        // pick random song
                        Random rand = new Random();
                        int n = 1; // ignore headers!
                        if (songsCopy.size() > 1) n = rand.nextInt(songsCopy.size() - 1) + 1;
                        String[] element = songsCopy.get(n);
                        // is there time for it? time = 3rd column
                        int songLength = Integer.parseInt(element[2]);
                        if (setLength > songLength) {
                            setLength -= songLength;
                            newSet.add(element[0]);
                            songsCopy.remove(element);
                        }
                        songsCopy.remove(element);
                    }
                    sets.add(newSet);
                    setCount--;
                }

                StringBuilder outputText = new StringBuilder();
                for(int i = 0; i < sets.size(); i++) {
                    outputText.append("SET ").append(i + 1).append("\n");
                    List list = sets.get(i);
                    for (int j = 0; j < list.size(); j++) {
                        outputText.append(list.get(j)).append("\n");
                    }
                }
                finalText.setText(outputText.toString());
            }
        });
    }
}
