package balychev.oleh.blch.cardword.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

import balychev.oleh.blch.cardword.R;
import balychev.oleh.blch.cardword.database.sqlite.CardDatabaseController;
import balychev.oleh.blch.cardword.model.Word;

public class TestActivity extends AppCompatActivity {

    public static String generateRandomWord(){
        Random random = new Random();
        char[] word = new char[random.nextInt(8)+3];
        for(int j = 0; j < word.length; j++) {
            word[j] = (char)('a' + random.nextInt(26));
        }
        return new String(word);
    }

    public static String generateRandomTranslation(){
        Random random = new Random();
        char[] translation = new char[random.nextInt(8)+3];
        for(int j = 0; j < translation.length; j++) {
            translation[j] = (char)('а' + random.nextInt(26));
        }
        return new String(translation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        CardDatabaseController controller = new CardDatabaseController(this);

        for (int i =0; i < 1000; i++){
            controller.addTestWord(new Word(
                    generateRandomWord(),
                    generateRandomTranslation()));
        }

        controller.close();

    }
}
