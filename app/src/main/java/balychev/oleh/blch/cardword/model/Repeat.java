package balychev.oleh.blch.cardword.model;

import java.sql.Date;

public class Repeat {
    private Card card;
    private String date;

    public Repeat(Card card, String date) {
        this.card = card;
        this.date = date;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Repeat{" +
                "card=" + card +
                ", date='" + date + '\'' +
                '}';
    }
}

