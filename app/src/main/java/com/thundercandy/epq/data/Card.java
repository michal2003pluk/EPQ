package com.thundercandy.epq.data;

public class Card extends DataItem {

    private String definition;

    public Card(int id, String term, String definition) {
        super(id, term);
        this.definition = definition;
    }

    public String getTerm() {
        return super.getName();
    }

    public void setTerm(String term) {
        super.setName(term);
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public SessionCard toSessionCard() {
        return new SessionCard(this.getId(), this.getTerm(), this.getDefinition());
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + this.getId() +
                ", term='" + this.getTerm() +
                ", definition='" + definition +
                '}';
    }
}
