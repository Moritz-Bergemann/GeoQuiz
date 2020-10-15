package com.bignerdranch.android.geoquiz;

/**
 * Class representing a question in the quiz
 */
public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mUserCheated;

    public Question(int mTextResId, boolean mAnswerTrue) {
        this.mTextResId = mTextResId;
        this.mAnswerTrue = mAnswerTrue;
        this.mUserCheated = false;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isUserCheated() {
        return mUserCheated;
    }

    public void setUserCheated(boolean userCheated) {
        mUserCheated = userCheated;
    }

    @Override
    public String toString() {
        return (String.format("Question text ID: \'%d\', Answer: %b, Cheated: %b", mTextResId, mAnswerTrue, mUserCheated));
    }
}
