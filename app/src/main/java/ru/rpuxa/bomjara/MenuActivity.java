package ru.rpuxa.bomjara;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import Game.Player;
import Game.Settings;

public class MenuActivity extends AppCompatActivity implements SaveAndLoad, GameActivity.SaveSettings {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        GameActivity.saveSettings = this;
        GameActivity.saveAndLoad = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        findViewById(R.id.continueGame).setOnClickListener(view -> {
            if (GameActivity.saveAndLoad.load())
                startGame();
            else
                showMassage("Игра не найдена! (повреждена или удалена)");
        });
        findViewById(R.id.newGame).setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Новая игра")
                    .setMessage("Вы точно хотите начать новую игру?")
                    .setIcon(R.drawable.location)
                    .setCancelable(false)
                    .setPositiveButton("Да", (dialog, id) -> {
                        dialog.cancel();
                        Player.currentPlayer = Player.createPlayer();
                        GameActivity.saveAndLoad.save();
                        startGame();
                    })
                    .setNegativeButton("Нет", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        });
        findViewById(R.id.settings).setOnClickListener(view -> {
            LinearLayout menuSet = findViewById(R.id.menuSettings);
            if (menuSet.getVisibility() == View.VISIBLE)
                menuSet.setVisibility(View.GONE);
            else
                menuSet.setVisibility(View.VISIBLE);
        });
        findViewById(R.id.exit).setOnClickListener(view -> finishAffinity());
        CheckBox hints = findViewById(R.id.turnOnHints);

        if (load(SETTINGS))
            hints.setChecked(GameActivity.settings.isTipsOn());
        else {
            GameActivity.settings = new Settings();
            hints.setChecked(true);
        }
        hints.setOnCheckedChangeListener((b, isChecked) -> {
            GameActivity.settings.setTipsOn(isChecked);
            save(SETTINGS);
        });
    }

    private void startGame(){
        Intent intObj = new Intent(this, GameActivity.class);
        startActivity(intObj);
    }

    public void showMassage(String massage){
        Toast.makeText(this,massage,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean save(){
        return save(PLAYER);
    }

    @Override
    public boolean load(){
        return load(PLAYER);
    }

    private static final int PLAYER = 0;
    private static final int SETTINGS = 1;


    public boolean save(int type){
        String name = "";
        if (type == PLAYER) {
            name = "player";
        } else if (type == SETTINGS)
            name = "settings";
        File file = new File(this.getFilesDir(), name);
        try (ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file))){
            if (type == PLAYER)
                obj.writeObject(Player.currentPlayer);
            else if (type == SETTINGS)
                obj.writeObject(GameActivity.settings);
            obj.flush();
            return true;
        } catch (IOException e){
            return false;
        }
    }


    public boolean load(int type){
        String name = "";
        if (type == PLAYER) {
            name = "player";
        } else if (type == SETTINGS)
            name = "settings";
        File file = new File(this.getFilesDir(), name);
        try (ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file))){
            if (type == PLAYER)
                Player.currentPlayer = (Player) obj.readObject();
            else if (type == SETTINGS)
                GameActivity.settings = (Settings) obj.readObject();
                return true;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            return false;
        }
    }

    @Override
    public void saveSettings() {
        save(SETTINGS);
    }

}