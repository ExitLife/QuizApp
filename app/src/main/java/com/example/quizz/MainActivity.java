package com.example.quizz;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Deklaracja zmiennych UI
    TextView questionTextView;
    TextView totalQuestionTextView;
    MaterialButton ansA, ansB, ansC, ansD, btn_submit;

    private String userName;
    private int colorWhite, colorYellow;
    private MaterialButton previouslyClickedButton = null;

    int score = 0;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";
    List<Integer> questionIndexes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja zmiennych UI
        totalQuestionTextView = findViewById(R.id.total_question);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_a);
        ansB = findViewById(R.id.ans_b);
        ansC = findViewById(R.id.ans_c);
        ansD = findViewById(R.id.ans_d);
        btn_submit = findViewById(R.id.btn_submit);

        // Pobieranie kolorów z zasobów
        colorWhite = getResources().getColor(R.color.white);
        colorYellow = getResources().getColor(R.color.yellow);

        // Ustawienie listenerów dla przycisków
        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        // Ustawienie tekstu dla totalQuestionTextView
        totalQuestionTextView.setText("Total question: 5");

        // Pobranie imienia użytkownika
        getUserName();
    }

    private void getUserName() {
        // Tworzenie dialogu do wprowadzenia imienia użytkownika
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        builder.setTitle("Enter Your Name")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    userName = input.getText().toString();
                    dialog.dismiss();
                    generateRandomQuestions(); // Generowanie losowych pytań
                    loadNewQuestion(); // Załadowanie pierwszego pytania
                })
                .setCancelable(false)
                .show();
    }

    private void generateRandomQuestions() {
        // Tworzenie listy indeksów pytań
        List<Integer> allIndexes = new ArrayList<>();
        for (int i = 0; i < QuestionAnswer.question.length; i++) {
            allIndexes.add(i);
        }
        Collections.shuffle(allIndexes); // Mieszanie listy indeksów
        for (int i = 0; i < 5; i++) {
            questionIndexes.add(allIndexes.get(i)); // Wybieranie 5 losowych pytań
        }
    }

    private void loadNewQuestion() {
        // Sprawdzenie, czy quiz się zakończył
        if (currentQuestionIndex == 5) {
            finishQuiz();
            return;
        }

        // Pobieranie indeksu aktualnego pytania
        int questionIndex = questionIndexes.get(currentQuestionIndex);
        // Ustawienie tekstu pytania i odpowiedzi
        questionTextView.setText(QuestionAnswer.question[questionIndex]);
        ansA.setText(QuestionAnswer.choices[questionIndex][0]);
        ansB.setText(QuestionAnswer.choices[questionIndex][1]);
        ansC.setText(QuestionAnswer.choices[questionIndex][2]);
        ansD.setText(QuestionAnswer.choices[questionIndex][3]);

        resetButtonBackgrounds(); // Resetowanie kolorów przycisków
        selectedAnswer = ""; // Resetowanie wybranej odpowiedzi
    }

    private void finishQuiz() {
        // Sprawdzenie, czy użytkownik zdał quiz
        String passStatus = (score >= 3) ? "Passed" : "Failed";
        // Wyświetlenie dialogu z wynikiem
        new AlertDialog.Builder(this)
                .setTitle(passStatus + ", " + userName) // Dodanie imienia użytkownika do tytułu
                .setMessage("Your Score is " + score + " Out of 5")
                .setPositiveButton("Restart", (dialog, which) -> restartQuiz())
                .setCancelable(false)
                .show();
    }

    private void restartQuiz() {
        // Restartowanie quizu
        score = 0;
        currentQuestionIndex = 0;
        questionIndexes.clear();
        generateRandomQuestions(); // Generowanie nowych losowych pytań
        loadNewQuestion(); // Załadowanie pierwszego pytania
    }

    private void resetButtonBackgrounds() {
        // Resetowanie kolorów przycisków odpowiedzi
        ansA.setBackgroundColor(colorWhite);
        ansB.setBackgroundColor(colorWhite);
        ansC.setBackgroundColor(colorWhite);
        ansD.setBackgroundColor(colorWhite);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof MaterialButton) {
            MaterialButton clickedButton = (MaterialButton) view;

            if (clickedButton.getId() == R.id.btn_submit) {
                // Obsługa kliknięcia przycisku submit
                if (!selectedAnswer.isEmpty()) {
                    int questionIndex = questionIndexes.get(currentQuestionIndex);
                    // Sprawdzenie, czy odpowiedź jest poprawna
                    if (selectedAnswer.equals(QuestionAnswer.correctAnswers[questionIndex])) {
                        score++;
                    }
                    currentQuestionIndex++;
                    loadNewQuestion(); // Załadowanie nowego pytania
                }
            } else {
                // Obsługa kliknięcia przycisków odpowiedzi
                if (previouslyClickedButton != null) {
                    previouslyClickedButton.setBackgroundColor(colorWhite); // Resetowanie koloru poprzedniego przycisku
                }

                selectedAnswer = clickedButton.getText().toString();
                clickedButton.setBackgroundColor(colorYellow); // Zaznaczenie wybranego przycisku

                previouslyClickedButton = clickedButton; // Aktualizacja referencji do wybranego przycisku
            }
        }
    }
}
