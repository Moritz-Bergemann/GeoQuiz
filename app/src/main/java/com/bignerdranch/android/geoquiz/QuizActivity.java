package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index"; //Key for saved index value in savedInstanceState
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView; //Contains text for current question

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_oceans, true),
    };
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        Log.v(TAG, Arrays.toString(mQuestionBank));

        setContentView(R.layout.activity_quiz);

        //Setting up question text
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view); //Gets text view object from layout
        updateQuestion();

        //OPTIONAL - makes clicking the question text move to the next question
//        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
//                updateQuestion();
//            }
//        });

        //Setting up buttons
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setting question text to that of new question
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mCurrentIndex - 1;
                if (mCurrentIndex < 0) {
                    mCurrentIndex = mQuestionBank.length - 1;
                }
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating/starting intent asking OS to start CheatActivity with this activity's context
                Intent i = CheatActivity.newIntent(QuizActivity.this, mQuestionBank[mCurrentIndex].isAnswerTrue());

                //Calling activity to get result (will be resolved in onActivityResult)
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        //Setting question to current question from saved instance state if it exists
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        updateQuestion();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");

        //Saving current index into key index value and putting this into savedInstanceState
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    /**
     * Updates question being displayed to question at current index of question array
     */
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId(); //Gets reference value of string for current question from resources
        mQuestionTextView.setText(question); //Sets current question to reference value of string
    }

    /**
     * Checks the user's input answer to a question against the actual answer and displays an
     * appropriate toast
     * @param userChoice The user's choice for the question answer
     */
    private void checkAnswer(boolean userChoice) {
        boolean actualAnswer = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if (mQuestionBank[mCurrentIndex].isUserCheated()) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userChoice == actualAnswer) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }


        //Displaying correct/incorrect toast
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //FIXME should this be here?

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHEAT) {
                if (data != null) {
                    //Getting CheatActivity class to interpret its own data & determine whether user
                    //  user cheated or not, putting this value into the current question's object
                    mQuestionBank[mCurrentIndex].setUserCheated(CheatActivity.wasAnswerShown(data));
                }
            }
        }
    }
}