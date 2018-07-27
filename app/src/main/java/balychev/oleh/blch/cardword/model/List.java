package balychev.oleh.blch.cardword.model;

import java.util.ArrayList;

public class List {
    private String name;
    private String imageURL;
    private String level;
    private ArrayList<String> tags;
    private ArrayList<Word> words;

    public List(String name, String imageURL, String level, ArrayList<String> tags, ArrayList<Word> words) {
        this.name = name;
        this.imageURL = imageURL;
        this.level = level;
        this.tags = tags;
        this.words = words;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }
}
