package com.renan.seconds33.model;

/**
 * Created by Renan on 08/11/2015.
 */
public class Pontuacao {
    private int _id;
    private int score;

    public Pontuacao() {

    }

    public Pontuacao(int _id, int score) {
        this._id = _id;
        this.score = score;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
