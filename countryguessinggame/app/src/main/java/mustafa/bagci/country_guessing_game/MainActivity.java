package mustafa.bagci.country_guessing_game;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText guessEditText;
    private TextView scoreTextView;
    private TextView hintTextView;
    private TextView remainingAttemptsTextView;
    private TextView questionNumberTextView;

    private String[] countries = {
            "Turkey",
            "United States",
            "Russia",
            "United Kingdom",
            "Mexico",
            "Argentina",
            "Germany",
            "Japan",
            "Greece",
            "Egypt"
    };

    private String[][] hints = {
            {"783,562 km²", "Asia", "Turkish Lira", "Turkish", "Ankara"},
            {"9,834,000 km² ", "North America", "US Dollar", "English", "Washington"},
            {"17,125,191 km²", "Asia", "Russian Ruble", "Russian", "Moscow"},
            {"243,610 km²", "Europe", "British Pound", "English", "London"},
            {"1,964,375 km²", "North America", "Mexican Peso", "Spanish", "Mexico City"},
            {"2,791,810 km²", "South America", "Argentine Peso", "Spanish", "Buenos Aires"},
            {"357,592 km²", "Europe", "Euro", "German", "Berlin"},
            {"377,973 km²", "Asia", "Japanese Yen", "Japanese", "Tokyo"},
            {"131,957 km²", "Europe", "Euro", "Greek", "Athens"},
            {"1,010,000 km²", "Africa", "Egyptian Pound", "Arabic", "Cairo"}
    };

    private int currentCountryIndex = 0;
    private int currentHintIndex = 0;
    private int score = 0;
    private int remainingAttempts = 3;
    private int questionNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        guessEditText = findViewById(R.id.guessEditText);
        Button submitButton = findViewById(R.id.submitButton);
        scoreTextView = findViewById(R.id.scoreTextView);
        Button hintButton = findViewById(R.id.hintButton);
        hintTextView = findViewById(R.id.hint);
        remainingAttemptsTextView = findViewById(R.id.remainingRight);
        questionNumberTextView = findViewById(R.id.questionNumber);

        hintButton.setOnClickListener(v -> showHint());

        submitButton.setOnClickListener(v -> checkGuess());

        shuffleQuestionsAndHints();

        updateUI();
    }

    private void shuffleQuestionsAndHints() {
        List<Integer> questionIndices = new ArrayList<>();
        for (int i = 0; i < countries.length; i++) {
            questionIndices.add(i);
        }
        Collections.shuffle(questionIndices);

        String[] shuffledCountries = new String[countries.length];
        String[][] shuffledHints = new String[hints.length][hints[0].length];

        for (int i = 0; i < questionIndices.size(); i++) {
            int index = questionIndices.get(i);
            shuffledCountries[i] = countries[index];
            System.arraycopy(hints[index], 0, shuffledHints[i], 0, hints[i].length);
        }
        countries = shuffledCountries;
        hints = shuffledHints;
    }

    private void showHint() {

        if (currentHintIndex < hints[currentCountryIndex].length) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure?");
            builder.setMessage("You are about to get a hint! (Note: -2 points will be deducted each time you get a hint.)");


            builder.setPositiveButton("Yes", (dialog, which) -> {

                if (currentHintIndex < hints[currentCountryIndex].length) {
                    String hint = hints[currentCountryIndex][currentHintIndex];

                    if (hintTextView.length() > 0) {
                        hintTextView.append(", ");
                    }

                    hintTextView.append(hint);
                    score -= 2;
                    currentHintIndex++;
                    guessEditText.setEnabled(true);
                    updateUI();
                }
            });

            builder.setNegativeButton("No", (dialog, which) -> {
            });

            builder.show();
        }
    }


    private void checkGuess() {
        String userGuess = guessEditText.getText().toString().trim();
        String correctCountry = countries[currentCountryIndex];

        if (userGuess.equalsIgnoreCase(correctCountry)) {
            score += 10;
            currentCountryIndex++;
            currentHintIndex = 0;
            questionNumber++;
            remainingAttempts = 3;
            hintTextView.setText("");

            if (currentCountryIndex >= countries.length) {
            } else {
                guessEditText.setEnabled(false);
            }
        } else {
            score -= 2;
            remainingAttempts--;

            if (remainingAttempts <= 0) {
                currentHintIndex = 0;
                currentCountryIndex++;
                questionNumber++;
                remainingAttempts = 3;
                hintTextView.setText("");

                if (currentCountryIndex < countries.length) {
                } else {
                    questionNumber = 10;
                }
                guessEditText.setEnabled(false);
            }
        }
        updateUI();
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        guessEditText.setText("");
        scoreTextView.setText("Score: " + score);
        remainingAttemptsTextView.setText("Remaining Attempts: " + remainingAttempts);
        questionNumberTextView.setText("Question " + questionNumber + "/10");
    }
}
