package Game;

import java.io.Serializable;
import java.util.Random;

import Game.Serialization.PlayerField;

public class Player implements Constants,Serializable {

    public static Player currentPlayer = createPlayer();

    @PlayerField
    public int age;
    @PlayerField
    public long bitcoins;
    @PlayerField
    public long bottles;
    @PlayerField
    public boolean caught;
    @PlayerField
    public boolean dead;
    @PlayerField
    public double efficiency = 1;
    @PlayerField
    public double energy;
    @PlayerField
    public long euros;
    @PlayerField
    public double food;
    @PlayerField
    public int friend;
    @PlayerField
    public double health;
    @PlayerField
    public int house;
    @PlayerField
    public int id;
    @PlayerField
    public int[] learning;
    @PlayerField
    public int location;
    @PlayerField
    public double[] maxIndicators;
    @PlayerField
    public long rubles;
    @PlayerField
    public boolean[] soldVipItems;
    @PlayerField
    public int transport;
    @PlayerField
    public long vipMoney;

    public Player(Object... fields) {
        this.age = (int) fields[0];
        this.bitcoins = (long) fields[1];
        this.bottles = (long) fields[2];
        this.caught = (boolean) fields[3];
        this.dead = (boolean) fields[4];
        this.efficiency = (double) fields[5];
        this.energy = (double) fields[6];
        this.euros = (long) fields[7];
        this.food = (double) fields[8];
        this.friend = (int) fields[9];
        this.health = (double) fields[10];
        this.house = (int) fields[11];
        this.id = (int) fields[12];
        this.learning = (int[]) fields[13];
        this.location = (int) fields[14];
        this.maxIndicators = (double[]) fields[15];
        this.rubles = (long) fields[16];
        this.soldVipItems = (boolean[]) fields[17];
        this.transport = (int) fields[18];
        this.vipMoney = (long) fields[19];
    }

    public static Player createPlayer() {
        return new Player(0,0L,0L,false,false,1.0,75.0,0L,75.0,0,75.0,0,new Random().nextInt(), new int[Create.study.length],0, new double[]{100,100,100},50L,new boolean[Create.vipStore.length],0,0L);
    }

    void addMaxIndicators(double count, int type){
        maxIndicators[type] += count;
    }

    public double[] getMaxIndicators() {
        return maxIndicators;
    }

    public long getVipMoney() {
        return vipMoney;
    }

    public void addVipMoney(long vipMoney) {
        this.vipMoney += vipMoney;
    }

    void addRandVipMoney(){
        if (new Random().nextInt(29) == 10){
            vipMoney++;
            Action.listener.showMassage(youFind);
        }
    }

    private double gauss(){
        Random rand = new Random();
        double gauss = 4;
        while (gauss > 3)
            gauss = rand.nextGaussian();
        gauss /= 3;
        return gauss + 1.5;
    }

    private double overflow(double count, double num, int type){
        double max = maxIndicators[type];
        if (count + num > max)
            return max;
        else if (count + num < 0)
            return 0;
        return count + num;
    }

    private double coefficient(double num, boolean positive, int type){
        double max = maxIndicators[type];
        num = num/max * 100;
        double percent = (num + 50)/100;
        return (positive) ? percent : 2 - percent;
    }

    long addFood(double count){
        double result = overflow(food, count * gauss(),Constants.food);
        long added = (long) (result - food);
        food = result;
        return added;
    }

    long addHealth(double count){
        double result = overflow(health, count * gauss() * coefficient(food, count > 0,Constants.food),Constants.health);
        long added = (long) (result - health);
        health = result;
        return added;
    }

    long addEnergy(double count){
        double result = overflow(energy,count * gauss() * coefficient(health, count > 0,Constants.health),Constants.energy);
        long added = (long) (result - energy);
        energy = result;
        return added;
    }

    public boolean addMoney(long money, int currency, boolean withoutCoefficient){
        if (!withoutCoefficient && money > 0)
            money = Math.round(money * coefficient(energy, true,Constants.energy) * efficiency);
        switch (currency) {
            case bottle:
                bottles += money;
                if (bottles < 0) {
                    bottles -= money;
                    return false;
                }
                break;
            case rub:
                rubles += money;
                if (rubles < 0) {
                    rubles -= money;
                    return false;
                }
                break;
            case euro:
                euros += money;
                if (euros < 0) {
                    euros -= money;
                    return false;
                }
                break;
            case bitcoin:
                bitcoins += money;
                if (bitcoins < 0) {
                    bitcoins -= money;
                    return false;
                }
                break;
        }
        return true;
    }
    
    public void setMoney(long money, int currency){
        switch (currency) {
            case bottle:
                bottles = money;
                break;
            case rub:
                rubles = money;
                break;
            case euro:
                euros = money;
                break;
            case bitcoin:
                bitcoins = money;
                break;
        }
    }

    void addAge(){
        age++;
    }

    public String getAge() {
        return (30 + age/365) + " " + ageSt + " " + age % 365 + " " + days;
    }

    public String getBottles() {
        return String.valueOf(bottles);
    }

    public String getRubles() {
        return String.valueOf(rubles);
    }

    public String getEuros() {
        return String.valueOf(euros);
    }

    public String getBitcoins() {
        return String.valueOf(bitcoins);
    }

    public String getFriend() {
        return Create.friends[friend].getName();
    }

    public String getLocation() {
        return Create.locationChains[location].getName();
    }

    public String getTransport() {
        return Create.transports[transport].getName();
    }

    public String getHouse() {
        return Create.houses[house].getName();
    }

    public double getFood() {
        return food;
    }

    public double getHealth() {
        return health;
    }

    public double getEnergy() {
        return energy;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isCaught() {
        return caught;
    }

    public void setCaught(boolean caught) {
        this.caught = caught;
    }

    private static final double bottlesToRubles = 1.0/1.5;
    private static final double rublesToRubles = 1;
    private static final double eurosToRubles = 1.0/70;
    private static final double bitcoinsToRubles = 1.0/2000;

    public double[] getRate(){
        Random rand = new Random(((long)(id) << 32) | age);
        double[] rates = {bottlesToRubles,rublesToRubles,eurosToRubles,bitcoinsToRubles};
        for (int i = 0; i < rates.length; i++)
            rates[i] *= 1 + 3 * (rand.nextDouble() -.5) / 10;

        return rates;
    }

    public long getMoney(int cur){
        switch (cur) {
            case bottle:
                return bottles;
            case rub:
                return rubles;
            case euro:
                return euros;
            case bitcoin:
                return bitcoins;
        }
        return 0;
    }

    public interface PlayerListener {
        void dead();
    }

    public static PlayerListener listener;

    void checkHealth() {
        if (health == 0)
            listener.dead();
    }

    public void setHealth(double health) {
        this.health = health;
    }
}
