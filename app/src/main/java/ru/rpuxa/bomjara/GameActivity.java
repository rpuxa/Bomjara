package ru.rpuxa.bomjara;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;

import Game.Action;
import Game.Chain;
import Game.Constants;
import Game.Create;
import Game.Location;
import Game.Player;
import Game.Settings;
import Game.Vip;

import static Game.Create.*;

public class GameActivity extends AppCompatActivity implements Constants,
        Action.ActionListener,Player.PlayerListener, Vip.VipListener {

    public static SaveAndLoad saveAndLoad;
    public static Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Action.listener = this;
        Player.listener = this;
        Vip.listener = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        updateInfo(Player.currentPlayer);
        updateActions(Player.currentPlayer);
        updateChains(Player.currentPlayer);
        updateRate();
        updateVipStore(Player.currentPlayer);
        setTips();
        setListeners();
        showBanner0();
        lastMassage = new Toast(this);
        loadBanner1();
        loadVipBanner();
        if (Player.currentPlayer.isCaught())
            caughtByPolice();
        if (Player.currentPlayer.isDead())
            dead();
    }

    private AdView banner0;
    private RewardedVideoAd banner1;
    private AdRequest adRequest = new AdRequest.Builder().build();

    private void showBanner0(){
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-9182384050264940~9510444896");
        banner0 = findViewById(R.id.adBanner0);
        AdRequest adRequest = new AdRequest.Builder().build();
        //"BF018E25FF993B133560D7A0B867333E"
        banner0.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                banner0.setVisibility(View.GONE);
            }
        });
        if (settings.isAdOn())
            banner0.loadAd(adRequest);
        else
            banner0.setVisibility(View.GONE);
    }

    private void loadBanner1(){
        banner1 = MobileAds.getRewardedVideoAdInstance(this);
        banner1.loadAd("ca-app-pub-9182384050264940/1237660054", adRequest);
    }

    private boolean showBanner1(){
        if (!checkNetworkConnection())
            return false;
        banner1.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {}
            @Override
            public void onRewardedVideoAdOpened() {

            }
            @Override
            public void onRewardedVideoStarted() {

            }
            @Override
            public void onRewardedVideoAdClosed() {
                loadBanner1();
            }
            @Override
            public void onRewarded(RewardItem rewardItem) {
                Player player = Player.currentPlayer;
                player.setDead(false);
                player.setCaught(false);
            }
            @Override
            public void onRewardedVideoAdLeftApplication() {
                loadBanner1();
            }
            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Player player = Player.currentPlayer;
                player.setDead(false);
                player.setCaught(false);
            }
        });
        if (banner1.isLoaded())
            banner1.show();
        else {
            loadBanner1();
            return false;
        }
        loadBanner1();
        return true;
    }

    private void setTips() {
        int visible = (settings.isTipsOn()) ? View.VISIBLE : View.GONE;
        @IdRes
        int[] tips = {R.id.tipEnergy,R.id.tipFood,R.id.tipFriend,R.id.tipHeath,
                R.id.tipHouse,R.id.tipJob,R.id.tipLoc,R.id.tipRate,R.id.tipTransport,
                R.id.tipVip};
        for (int id : tips)
            findViewById(id).setVisibility(visible);
    }

    private void setListeners(){
        EditText count = findViewById(R.id.rateCount);
        findViewById(R.id.foodButton).setOnClickListener(view -> showMenu(R.id.foodMenu));
        findViewById(R.id.healthButton).setOnClickListener(view -> showMenu(R.id.healthMenu));
        findViewById(R.id.energyButton).setOnClickListener(view -> showMenu(R.id.energyMenu));
        findViewById(R.id.locFriendButton).setOnClickListener(view -> showMenu(R.id.locFriendMenu));
        findViewById(R.id.infoButton).setOnClickListener(view -> showMenu(R.id.infoMenu));
        findViewById(R.id.houseTransportButton).setOnClickListener(view -> showMenu(R.id.houseTransportMenu));
        findViewById(R.id.jobButton).setOnClickListener(view -> showMenu(R.id.jobMenu));
        findViewById(R.id.rateButton).setOnClickListener(view -> {
            updateRate();
            showMenu(R.id.rateMenu);
        });
        findViewById(R.id.rateTranslate).setOnClickListener(view -> {
            try {
                translate(false, Long.parseLong(count.getText().toString()));
            } catch (NumberFormatException ignore) {}
        });
        findViewById(R.id.rateTranslateAll).setOnClickListener(view -> translate(true,0));
        ((Spinner)findViewById(R.id.rateFrom)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateRate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        ((Spinner)findViewById(R.id.rateTo)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateRate();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        ((Spinner)findViewById(R.id.rateTo)).setSelection(1);
        count.setOnKeyListener((a,b,c) -> {
            updateRate();
            return false;
        });
        findViewById(R.id.infoSave).setOnClickListener(view -> {
            if (saveAndLoad.save())
                showMassage("Сохранение прошло успешно!");
        });
        findViewById(R.id.infoToMenu).setOnClickListener(view -> backToMenu());
        findViewById(R.id.quitButton).setOnClickListener(view -> backToMenu());
        findViewById(R.id.vipButton).setOnClickListener(view -> showMenu(R.id.vipMenu));
        findViewById(R.id.vipAdd).setOnClickListener(view -> showVipBanner());
    }

    private void translate(boolean all ,long count){
        Player player = Player.currentPlayer;
        int[] currencies = getCurrencies();
        double[] rates = player.getRate();
        if (all)
            count = player.getMoney(currencies[0]);
        if (currencies[0] == currencies[1])
            showMassage("Выберите разные валюты!");
        else if (player.addMoney(-count,currencies[0],true)) {
            player.addMoney((long) ((double)count / rates[currencies[0]] * rates[currencies[1]]), currencies[1],true);
            if (count != 0) {
                showMassage("Обмен совершен!");
                updateInfo(player);
            }
        } else {
            showMassage("Не хватает средств!");
        }
    }

    private void updateRate(){
        try {
            double[] rates = Player.currentPlayer.getRate();
            int[] currencies = getCurrencies();
            long count = Long.parseLong(((EditText) findViewById(R.id.rateCount)).getText().toString());
            String text0 = String.valueOf((double) (Math.round(rates[currencies[1]] / rates[currencies[0]] * 10000)) / 10000),
                    text1 = String.valueOf(count),
                    text2 = (currencies[0] != currencies[1]) ? String.valueOf((long) ((double) count / rates[currencies[0]] * rates[currencies[1]])) : text1;
            ((TextView) findViewById(R.id.rateRate)).setText(text0);
            ((TextView) findViewById(R.id.rateFirstCurr)).setText(text1);
            ((TextView) findViewById(R.id.rateSecondCurr)).setText(text2);
        } catch (NumberFormatException ignore){
        }
    }

    private int[] getCurrencies(){
        Spinner[] spinners = {findViewById(R.id.rateFrom), findViewById(R.id.rateTo)};
        int currencies[] = new int[2];
        for (int i = 0; i < 2; i++)
            switch (spinners[i].getSelectedItem().toString()){
                case "Бутылки":
                    currencies[i] = bottle;
                    break;
                case "Рубли":
                    currencies[i] = rub;
                    break;
                case "Евро":
                    currencies[i] = euro;
                    break;
                case "Биткоины":
                    currencies[i] = bitcoin;
                    break;
            }
            return currencies;
    }

    @Override
    public void updateInfo(Player player) {
        String playerInfo[] = new String[]{
                player.getAge(),player.getLocation(),player.getTransport(),
                player.getHouse(),player.getFriend(),player.getBottles(),
                player.getRubles(),player.getEuros(),player.getBitcoins(),
                player.getRubles(),player.getEuros(),player.getBitcoins(),
                String.valueOf(player.getVipMoney()),
                String.valueOf((int)player.getFood()), String.valueOf((int)player.getHealth()), String.valueOf((int)player.getEnergy()),
                String.valueOf((int)player.getMaxIndicators()[food]), String.valueOf((int)player.getMaxIndicators()[health]), String.valueOf((int)player.getMaxIndicators()[energy])
        };

        @IdRes
        int[] ids = {
                R.id.info_age,R.id.info_location,R.id.info_transport,
                R.id.info_house,R.id.info_friend,R.id.info_bottles,
                R.id.info_rubles,R.id.info_euros,R.id.info_bitcoins,
                R.id.bar_rubles ,R.id.bar_euros ,R.id.bar_bitcoins,
                R.id.vipVipMoney,
                R.id.text_food, R.id.text_health, R.id.text_energy,
                R.id.text_food_max, R.id.text_health_max, R.id.text_energy_max
        };


        @IdRes
        int[] idsBars = {
                R.id.bar_food,R.id.bar_health,R.id.bar_energy
        };


        double[] bars = {
                player.getFood(),player.getHealth(),player.getEnergy()
        };

        double[] barsMax = player.getMaxIndicators();

        for (int i = 0; i < playerInfo.length; i++)
            ((TextView) findViewById(ids[i])).setText(playerInfo[i]);
        for (int i = 0; i < idsBars.length; i++) {
            ProgressBar bar = findViewById(idsBars[i]);
            bar.setProgress((int) bars[i]);
            bar.setMax((int) barsMax[i]);
        }
    }

    @Override
    public void updateActions(Player player) {
        ArrayList<Button>[][] menuButtons = new ArrayList[countMenus][2];
        final int legal = 0, illegal = 1;
        for (ArrayList<Button>[] b : menuButtons){
            b[0] = new ArrayList<>();
            b[1] = new ArrayList<>();
        }
        Location location = Create.locations[player.location];

        for (Action action : concatArray(location.getActions(),Create.jobs[player.friend].getActions())) {
            Button button = new Button(this);
            button.setOnClickListener(action);
            button.setText(action.getName());
            menuButtons[action.getMenu()][(action.isLegal()) ? legal : illegal].add(button);
        }
        addButtonsToList(findViewById(R.id.foodLegalList),menuButtons[food][legal]);
        addButtonsToList(findViewById(R.id.foodIllegalList),menuButtons[food][illegal]);
        addButtonsToList(findViewById(R.id.healthLegalList),menuButtons[health][legal]);
        addButtonsToList(findViewById(R.id.healthIllegalList),menuButtons[health][illegal]);
        addButtonsToList(findViewById(R.id.energyLegalList),menuButtons[energy][legal]);
        addButtonsToList(findViewById(R.id.energyIllegalList),menuButtons[energy][illegal]);
        addButtonsToList(findViewById(R.id.jobLegalList),menuButtons[jobs][legal]);
        addButtonsToList(findViewById(R.id.jobIllegalList),menuButtons[jobs][illegal]);

    }

    private void addButtonsToList(LinearLayout legalMenu, ArrayList<Button> actions) {
        legalMenu.removeAllViews();
        for (Button button : actions)
            legalMenu.addView(button);
    }

    @Override
    public void updateChains(Player player){
        Chain[][] chains = {locationChain,friends,houses,transport};
        int[] progress = {player.location,player.friend,player.house,player.transport};
        @IdRes
        int[] currentsIds = {R.id.currentLocation,R.id.currentFriend,R.id.currentHouse,R.id.currentTransport};
        @IdRes
        int[] idButtons = {R.id.locationButton,R.id.friendButton,R.id.houseButton,R.id.transportButton};
        String subnames[] = {"Перейти:  ","Подружиться:  ","Купить:  ", "Пересесть:  "};

        for (int i = 0; i < chains.length; i++) {
            String textButton;
            Chain nextChain = chains[0][0];
            boolean exception = false;
            try {
                nextChain = chains[i][progress[i]+1];
               textButton = subnames[i] + nextChain.getName() + " " + nextChain.getMoney();
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                textButton = "Недоступно";
                exception = true;
            }
            String textCurrent = chains[i][progress[i]].getName();
            ((TextView) findViewById(currentsIds[i])).setText(textCurrent);
            Button button = findViewById(idButtons[i]);
            button.setText(textButton);
            if (exception) {
                button.setOnClickListener(null);
                continue;
            }
            int finalI = i;
            Chain finalNextChain = nextChain;
            button.setOnClickListener(view -> finalNextChain.open(finalI));
        }
    }

    @IdRes
    private final int[] menus = {
            R.id.infoMenu, R.id.locFriendMenu,R.id.houseTransportMenu,
            R.id.foodMenu,R.id.healthMenu,R.id.energyMenu,R.id.jobMenu,
            R.id.rateMenu,R.id.vipMenu
    };

    void showMenu(@IdRes int id) {
        for (int menu : menus)
            findViewById(menu).setVisibility(View.GONE);
        findViewById(id).setVisibility(View.VISIBLE);
    }

    Toast lastMassage;

    @Override
    public void showMassage(String massage){
        showMassage(massage,false);
    }

    @Override
    public void showMassage(String massage, boolean isShort){
        lastMassage.cancel();
        lastMassage = Toast.makeText(this,massage,(isShort) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
        lastMassage.show();

    }

    private static Action[] concatArray(Action[] a, Action[] b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        Action[] r = new Action[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    @Override
    public void dead() {
        Player.currentPlayer.setDead(true);
        final boolean[] sure = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Бомж умер!")
                .setMessage("Полоса здоровья опустилась ниже 0!\n" +
                        "Вы моежете воскресить его, посмотрев рекламу\n" +
                        "или начать заного, потеряв прогресс")
                .setIcon(R.drawable.dead)
                .setCancelable(false)
                .setPositiveButton("Воскресить!", (dialog, id) -> {})
                .setNegativeButton("Начать заного", (dialog, id) -> {});
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            if (sure[0]) {
                Player.currentPlayer = Player.createPlayer();
                saveAndLoad.save();
                backToMenu();
                alert.dismiss();
            } else {
                showMassage("Вы точно уверены???");
                sure[0] = true;
            }
        });
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            if (!showBanner1()) {
                Player.currentPlayer.setDead(false);
                alert.dismiss();
            } else {
                new Thread(() -> {
                    while (Player.currentPlayer.isDead()){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    alert.dismiss();
                }).start();
            }
            Player.currentPlayer.setHealth(50);
            updateInfo(Player.currentPlayer);
        });
    }

    private void backToMenu(){
        saveAndLoad.save();
        Intent intObj = new Intent(this, MenuActivity.class);
        startActivity(intObj);
    }

    @Override
    public void caughtByPolice(){
        Player.currentPlayer.setCaught(true);
        long money = locations[Player.currentPlayer.location].getFine();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вас поймали!")
                .setMessage("Вас поймали менты!\n" +
                        "Вы можете посмотреть рекламу, чтобы вас отпустили\n" +
                        "или заплатить штраф (" + Action.moneyToStr(money, rub) + ")")
                .setIcon(R.drawable.prison)
                .setCancelable(false)
                .setPositiveButton("Посмотреть рекламу (бесплатно)!", (dialog, id) -> {})
                .setNegativeButton("Заплатить штраф", (dialog, id) -> {});

        AlertDialog alert = builder.create();
        alert.show();
        Button button = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
        button.setOnClickListener(v -> {
            if (!Player.currentPlayer.addMoney(-money,rub,true))
                Player.currentPlayer.setMoney(0,rub);
                alert.dismiss();
            updateInfo(Player.currentPlayer);
        });
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!showBanner1()) {
                Player.currentPlayer.setCaught(false);
                alert.dismiss();
            } else {
                new Thread(() -> {
                    while (Player.currentPlayer.isCaught()){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    alert.dismiss();
                }).start();
            }
        });
    }

    @Override
    public void onResume() {
        try {
            banner1.resume(this);
        } catch (NullPointerException ignore){
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        try {
        banner1.pause(this);
        } catch (NullPointerException ignore){
        }
        saveAndLoad.save();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        try {
        banner1.destroy(this);
        } catch (NullPointerException ignore){
        }
        super.onDestroy();
    }

    private boolean checkNetworkConnection(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void updateVipStore(Player player) {
        LinearLayout store = findViewById(R.id.vipList);
        store.removeAllViews();
        int num = 0;
        for (Vip vip : Create.vipStore){
            Button item = new Button(this);
            String text = vip.getName()+ " " + ((vip.isNotInfinity() && player.soldVipItems[num]) ? "(Продано)" : (vip.getCost() + " вал."));
            item.setText(text);
            item.setOnClickListener(vip);
            if (vip.isNotInfinity())
                num++;
            store.addView(item);
        }
    }

    private RewardedVideoAd vipBanner;

    private void loadVipBanner(){
        vipBanner = MobileAds.getRewardedVideoAdInstance(this);
        vipBanner.loadAd("ca-app-pub-9182384050264940/9188603588", adRequest);
    }

    private void showVipBanner() {
        if (!checkNetworkConnection())
            showMassage("Нет подключения к интернету!");
        else{
            vipBanner.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                }

                @Override
                public void onRewardedVideoAdOpened() {

                }

                @Override
                public void onRewardedVideoStarted() {

                }

                @Override
                public void onRewardedVideoAdClosed() {
                    loadVipBanner();
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    Player.currentPlayer.addVipMoney(3);
                    showMassage("Получайте награду!");
                    updateInfo(Player.currentPlayer);
                    updateVipStore(Player.currentPlayer);
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    loadVipBanner();
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    Player player = Player.currentPlayer;
                    player.setDead(false);
                    player.setCaught(false);
                }
            });
            if (vipBanner.isLoaded())
                vipBanner.show();
            else
                showMassage("Ошибка загрузки. Попробуйте чуть позже");
            loadVipBanner();
        }
    }
}

