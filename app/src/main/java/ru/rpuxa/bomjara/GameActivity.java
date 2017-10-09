package ru.rpuxa.bomjara;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import java.util.Objects;

import Game.Action;
import Game.Chain;
import Game.Constants;
import Game.Create;
import Game.Location;
import Game.Player;
import Game.Serialization.SerializationProcess;
import Game.Settings;
import Game.Study;
import Game.Vip;

import static Game.Create.createStudy;
import static Game.Create.createVipStore;
import static Game.Create.friends;
import static Game.Create.houses;
import static Game.Create.locationChains;
import static Game.Create.locations;
import static Game.Create.transports;

public class GameActivity extends AppCompatActivity implements Constants,
        Action.ActionListener,Player.PlayerListener, Vip.VipListener,
        Study.StudyListener {

    private static final String LINK_ON_GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=ru.rpuxa.bomjara";
    public static SaveAndLoad saveAndLoad;
    public static Settings settings;
    public static GameActivity.SaveSettings saveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SerializationProcess.serialization(Player.currentPlayer);
        createVipStore();
        createStudy();
        Action.listener = this;
        Player.listener = this;
        Vip.listener = this;
        Study.listener = this;
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
        if (Player.currentPlayer.age == 0)
            showPrologueMenu();
        showStudy();
        if (Player.currentPlayer.age > 100 && settings.isShowAppRate())
            showRateApplication();
    }

    private void showRateApplication(){
        Dialog dialog = new Dialog(GameActivity.this);
        dialog.setTitle(rateApp);
        dialog.setContentView(R.layout.rate);
        dialog.show();
        ((RatingBar) dialog.findViewById(R.id.ratingBar)).setOnRatingBarChangeListener((v1,v2,v3) -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(LINK_ON_GOOGLE_PLAY));
            startActivity(browserIntent);
            settings.setShowAppRate(false);
            dialog.dismiss();
            saveSettings.saveSettings();
        });
        dialog.findViewById(R.id.later).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.never).setOnClickListener(v -> {
            settings.setShowAppRate(false);
            dialog.dismiss();
            saveSettings.saveSettings();
        });

    }

    Dialog prologueDialog;

    private void showPrologueMenu() {
        prologueDialog = new Dialog(GameActivity.this);
        prologueDialog.setTitle(prologueSt);
        prologueDialog.setContentView(R.layout.prologue);
        prologueDialog.show();
    }

    private static int prologueChosen = 0;

    public void startGameClickListener(View view){
        prologueDialog.dismiss();
        Player player = Player.createPlayer();
        switch (prologueChosen){
            case FIRST_PROLOGUE:
                player.setMoney(1000,rub);
                break;
            case SECOND_PROLOGUE:
                player.maxIndicators[1] = 120;
                break;

            case THIRD_PROLOGUE:
                player.learning[0] = 10;
                player.transport = 1;
                break;
        }
        player.age = 1;
        Player.currentPlayer = player;
        updateActions(player);
        updateChains(player);
        updateInfo(player);
        showStudy();
    }

    public void firstPrologueButtonClickListener(View view) {
        showPrologueN(FIRST_PROLOGUE,view);
        prologueChosen = FIRST_PROLOGUE;
    }

    public void secondPrologueButtonClickListener(View view) {
        showPrologueN(SECOND_PROLOGUE,view );
        prologueChosen = SECOND_PROLOGUE;
    }

    public void thirdPrologueButtonClickListener(View view) {
        showPrologueN(THIRD_PROLOGUE, view);
        prologueChosen = THIRD_PROLOGUE;
    }

    private static final int FIRST_PROLOGUE = 0;
    private static final int SECOND_PROLOGUE = 1;
    private static final int THIRD_PROLOGUE = 2;

    private void showPrologueN(int prologueIndex, View button) {
        @IdRes
        final int[] idsButtons = {
                R.id.firstPrologueButton,R.id.secondPrologueButton,
                R.id.thirdPrologueButton
        };

        @IdRes
                final int[] idsPrologues = {
                R.id.prologue0, R.id.prologue1, R.id.prologue2
        };

        LinearLayout.LayoutParams weight1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        LinearLayout.LayoutParams weight5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,5);


        for (int id : idsButtons)
            prologueDialog.findViewById(id).setLayoutParams(weight1);
        button.setLayoutParams(weight5);

        for (int id : idsPrologues)
            prologueDialog.findViewById(id).setVisibility(View.GONE);
        prologueDialog.findViewById(idsPrologues[prologueIndex]).setVisibility(View.VISIBLE);
    }

    private AdView banner0;
    private RewardedVideoAd banner1;
    private AdRequest adRequest = new AdRequest.Builder().build();

    private void showBanner0(){
        MobileAds.initialize(getApplicationContext(), appId);
        banner0 = findViewById(R.id.adBanner0);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner0.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                banner0.setVisibility(View.GONE);
            }
        });
        if (!Player.currentPlayer.soldVipItems[adFree])
            banner0.loadAd(adRequest);
        else
            banner0.setVisibility(View.GONE);
    }

    private void loadBanner1(){
        banner1 = MobileAds.getRewardedVideoAdInstance(this);
        banner1.loadAd(banner1Id, adRequest);
    }

    private boolean showBanner1(){
        if (!checkNetworkConnection()) {
            showMassage(noNetwork);
            return false;
        }
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
            showMassage(loading);
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
                R.id.tipVip,R.id.tipStudy};
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
                translate(false, Long.parseLong(count.getText().toString()),null);
            } catch (NumberFormatException ignore) {}
        });
        findViewById(R.id.rateTranslateAll).setOnClickListener(view -> translate(true,0,null));
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
                showMassage(saveSuccess);
        });
        findViewById(R.id.infoToMenu).setOnClickListener(view -> backToMenu());
        findViewById(R.id.quitButton).setOnClickListener(view -> backToMenu());
        findViewById(R.id.vipButton).setOnClickListener(view -> showMenu(R.id.vipMenu));
        findViewById(R.id.studyButton).setOnClickListener(view -> showMenu(R.id.studyMenu));
        findViewById(R.id.vipAdd).setOnClickListener(view -> showVipBanner());
    }

    private void translate(boolean all ,long count, int[] currencies){
        Player player = Player.currentPlayer;
        if (currencies == null)
            currencies = getCurrencies();
        double[] rates = player.getRate();
        if (all)
            count = player.getMoney(currencies[0]);
        if (currencies[0] == currencies[1])
            showMassage(choseAnotherCurr);
        else if (player.addMoney(-count,currencies[0],true)) {
            player.addMoney((long) ((double)count / rates[currencies[0]] * rates[currencies[1]]), currencies[1],true);
            if (count != 0) {
                showMassage(exchangeSuccess);
                updateInfo(player);
            }
        } else {
            showMassage(noMoney);
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
        final String[] stCurrencies = getResources().getStringArray(R.array.currency);
        Spinner[] spinners = {findViewById(R.id.rateFrom), findViewById(R.id.rateTo)};
        final int[] currId = {
                bottle,rub,euro,bitcoin
        };
        int currencies[] = new int[2];
        for (int i = 0; i < 2; i++) {
            String curr = spinners[i].getSelectedItem().toString();
                for (int j = 0; j < 4; j++)
                    if (Objects.equals(curr, stCurrencies[j]))
                        currencies[i] = currId[j];
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
                String.valueOf((int)player.getMaxIndicators()[food]), String.valueOf((int)player.getMaxIndicators()[health]), String.valueOf((int)player.getMaxIndicators()[energy]),
                String.valueOf((int)player.getVipMoney())
        };

        @IdRes
        int[] ids = {
                R.id.info_age,R.id.info_location,R.id.info_transport,
                R.id.info_house,R.id.info_friend,R.id.info_bottles,
                R.id.info_rubles,R.id.info_euros,R.id.info_bitcoins,
                R.id.bar_rubles ,R.id.bar_euros ,R.id.bar_bitcoins,
                R.id.vipVipMoney,
                R.id.text_food, R.id.text_health, R.id.text_energy,
                R.id.text_food_max, R.id.text_health_max, R.id.text_energy_max,
                R.id.info_vipMoney
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
        for (ArrayList<Button>[] b : menuButtons)
            for (int i = 0; i < 2; i++)
                b[i] = new ArrayList<>();

        Location location = Create.locations[player.location];

        for (Action action : concatArray(location.getActions(), Create.jobs[player.friend].getActions())) {
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
        Chain[][] chains = {locationChains,friends,houses, transports};
        int[] progress = {player.location,player.friend,player.house,player.transport};
        @IdRes
        int[] currentsIds = {R.id.currentLocation,R.id.currentFriend,R.id.currentHouse,R.id.currentTransport};
        @IdRes
        int[] idButtons = {R.id.locationButton,R.id.friendButton,R.id.houseButton,R.id.transportButton};
        String subNames[] = {goTo, makeFriend, buy, change};

        for (int i = 0; i < chains.length; i++) {
            String textButton;
            Chain nextChain = chains[0][0];
            boolean exception = false;
            try {
                nextChain = chains[i][progress[i]+1];
               textButton = subNames[i] + nextChain.getName() + " " + nextChain.getMoney();
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                textButton = unavailable;
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
            Chain finalNextChain = nextChain;
            int finalI = i;
            button.setOnClickListener(view -> finalNextChain.open(finalI));
        }
    }

    @IdRes
    private final int[] menus = {
            R.id.infoMenu, R.id.locFriendMenu,R.id.houseTransportMenu,
            R.id.foodMenu,R.id.healthMenu,R.id.energyMenu,R.id.jobMenu,
            R.id.rateMenu,R.id.vipMenu,R.id.studyMenu
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
        builder.setTitle(dead)
                .setMessage(deadMassage)
                .setIcon(R.drawable.dead)
                .setCancelable(false)
                .setPositiveButton(resurrect, (dialog, id) -> {})
                .setNegativeButton(newGame, (dialog, id) -> {});
        AlertDialog alert = builder.create();
        alert.show();
        try {
            Thread.sleep(700);
        } catch (InterruptedException ignored) {
        }
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            if (sure[0]) {
                Player.currentPlayer = Player.createPlayer();
                saveAndLoad.save();
                backToMenu();
                alert.dismiss();
            } else {
                showMassage(sureSt);
                sure[0] = true;
            }
        });
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            if (showBanner1()) {
                new Thread(() -> {
                    while (Player.currentPlayer.isDead()){
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ignored) {
                        }
                    }
                    alert.dismiss();
                }).start();
                Player.currentPlayer.setHealth(100);
                updateInfo(Player.currentPlayer);
            }
        });
    }

    private void backToMenu(){
        saveAndLoad.save();
        Intent intObj = new Intent(this, MenuActivity.class);
        startActivity(intObj);
    }

    @Override
    public void caughtByPolice() {
        Player.currentPlayer.setCaught(true);
        long money = locations[Player.currentPlayer.location].getFine();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(caught)
                .setMessage(caughtMassage + " (" + Action.moneyToStr(money, rub) + ")")
                .setIcon(R.drawable.prison)
                .setCancelable(false)
                .setPositiveButton(watchAd, (dialog, id) -> {
                })
                .setNegativeButton(payFine, (dialog, id) -> {
                });

        AlertDialog alert = builder.create();
        alert.show();
        try {
            Thread.sleep(700);
        } catch (InterruptedException ignored) {
        }
        Button button = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
        button.setOnClickListener(v -> {
            if (!Player.currentPlayer.addMoney(-money, rub, true)) {
                translate(true, 0, new int[]{euro, rub});
                if (!Player.currentPlayer.addMoney(-money, rub, true)) {
                    translate(true, 0, new int[]{bitcoin, rub});
                    if (!Player.currentPlayer.addMoney(-money, rub, true)) {
                        Player.currentPlayer.setMoney(0, rub);
                    }
                }
                showMassage(convertMassage);
            }
            alert.dismiss();
            Player.currentPlayer.setCaught(false);
            updateInfo(Player.currentPlayer);
        });
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (showBanner1()) {
                new Thread(() -> {
                    while (Player.currentPlayer.isCaught()) {
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
        if (banner1 != null)
            banner1.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (banner1 != null)
            banner1.pause(this);
        saveAndLoad.save();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (banner1 != null)
            banner1.destroy(this);
        super.onDestroy();
    }

    private boolean checkNetworkConnection() {
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
            String text = vip.getName()+ " " + ((vip.isNotInfinity() && player.soldVipItems[num]) ? sold : (vip.getCost() + diamonds));
            item.setText(text);
            item.setOnClickListener(vip);
            if (vip.isNotInfinity())
                num++;
            store.addView(item);
        }
    }

    private RewardedVideoAd vipBanner;

    private void loadVipBanner() {
        vipBanner = MobileAds.getRewardedVideoAdInstance(this);
        vipBanner.loadAd(vipBannerId, adRequest);
    }

    private void showVipBanner() {
        if (!checkNetworkConnection())
            showMassage(noNetwork);
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
                    showMassage(getReward);
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
                showMassage(loading);
            loadVipBanner();
        }
    }

    @Override
    public void showStudy() {
        LinearLayout list = findViewById(R.id.studyList);
        list.removeAllViews();
        Player player = Player.currentPlayer;
        Study[] study = Create.study;
        for (int i = 0; i < study.length; i++) {
            if (player.learning[i] == 0){
                String text = learning + study[i].getName() + " -" + Action.moneyToStr(study[i].getCost(),study[i].getCurrency());
                Button button = new Button(this);
                button.setText(text);
                button.setOnClickListener(study[i]);
                list.addView(button);
            } else if (player.learning[i] == study[i].getLength()){
                Button button = new Button(this);
                String text = study[i].getName() + studied;
                button.setText(text);
                list.addView(button);
            } else {
                TextView name = new TextView(this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,1);
                lp.gravity = Gravity.CENTER;
                name.setLayoutParams(lp);
                name.setText(study[i].getName());
                list.addView(name);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,0);
                LinearLayout column = new LinearLayout(this);
                column.setOrientation(LinearLayout.HORIZONTAL);
                ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,1);
                lp.gravity = Gravity.CENTER;
                bar.setLayoutParams(lp);
                bar.setMax(study[i].getLength());
                bar.setProgress(player.learning[i]);
                column.addView(bar);

                TextView textView = new TextView(this);
                textView.setText(String.valueOf(player.learning[i]));
                textView.setLayoutParams(params);
                column.addView(textView);

                textView = new TextView(this);
                textView.setText("/");
                textView.setLayoutParams(params);
                column.addView(textView);

                textView = new TextView(this);
                textView.setText(String.valueOf(study[i].getLength()));
                textView.setLayoutParams(params);
                column.addView(textView);

                Button button = new Button(this);
                button.setText(learn);
                button.setLayoutParams(params);
                button.setOnClickListener(study[i]);
                column.addView(button);
                list.addView(column);
            }
        }
    }

    interface SaveSettings{
        void saveSettings();
    }
}

