package Game;

import ru.rpuxa.bomjara.GameActivity;

public class Create implements Constants {

    public static Chain[] locationChain = new Chain[10];
    public static Chain[] friends = new Chain[10];
    public static Chain[] transport = new Chain[10];
    public static Chain[] houses = new Chain[10];
    public static Location[] locations = new Location[10];
    public static Jobs[] jobs = new Jobs[10];
    public static Vip[] vipStore;

    public static VipItemListener listener0;
    static {
        //VIP STORE
        vipStore = new Vip[]{
                new Vip("+10 к макс. запасу еды", false, 9) {
                    @Override
                    void reward(Player player) {
                        player.addMaxIndicators(10, food);
                    }
                },
                new Vip("+10 к макс. запасу здоровья", false, 9) {
                    @Override
                    void reward(Player player) {
                        player.addMaxIndicators(10, health);
                    }
                },
                new Vip("+10 к макс. запасу бодрости", false, 9) {
                    @Override
                    void reward(Player player) {
                        player.addMaxIndicators(10, energy);
                    }
                },
                new Vip("Отключить баннер сверху",true, 29) {
                    @Override
                    void reward(Player player) {
                        player.soldVipItems[0] = true;
                        GameActivity.settings.setAdOn(false);
                        listener0.saveSettings();
                        Action.listener.showMassage("Перезайдите в игру, чтобы баннер убался.");
                    }
                }
        };
        //end


        locationChain = new Chain[]{
                new Chain("Помойка на окраине", 0, 0, 0, 0, 0, none),
                new Chain("Подъезд", 1, 1, 0, 0, 0, none),
                new Chain("Жилой район", 2, 2, 1, 1, 0, none)
        };

        friends = new Chain[]{
                new Chain("Без кореша", 0, 0, 0, 0, 0, none),
                new Chain("Сосед по подъезду Василий", 0, 0, 0, 1, -60, bottle),
                new Chain("Гопник Валера", 2, 0, 0, 1, -300, bottle),
                new Chain("Бомбила Семён", 3, 0, 0, 2, -1000, rub)
        };

        transport = new Chain[]{
                new Chain("Без транспорта", 0, 0, 0, 0, 0, none),
                new Chain("Самокат", 0, 0, 0, 0, -500, rub),
                new Chain("Велосипед", 1, 1, 0, 1, -3000, rub),
                new Chain("Старая копейка", 1, 1, 0, 1, -10000, rub),
        };

        houses = new Chain[]{
                new Chain("Помойка", 0, 0, 0, 0, 0, none),
                new Chain("Палатка б/у", 0, 0, 0, 0, -1000, rub),
                new Chain("Гараж улитка", 2, 1, 0, 1, -9000, rub)
        };


        locations[0] = new Location(500,
                //Food
                new Action("Жрать объетки с помойки", 0, rub, -10, -15, 25, food, true),
                new Action("Купить бутер", -40, rub, -5, -5, 30, food, true),
                new Action("Купить шаурму", -100, rub, -5, -5, 70, food, true),
                new Action("Отжать семки у голубей", 0, none, -5, -5, 40, food, false),
                //Med
                new Action("Пособирать травы", 0, rub, 10, -10, -5, health, true),
                new Action("Сходить к бабке", -70, rub, 60, -10, -5, health, true),
                new Action("Спереть с аптеки лекарства", 0, none, 50, -20, -5, health, false),
                //Energy
                new Action("Поспать", 0, none, -5, 15, -5, energy, true),
                new Action("Выпить палёнки", -45, rub, -5, 30, -5, energy, true),
                new Action("Купить пивас", -60, rub, 0, 30, -5, energy, true),
                new Action("Украсть РедБулл", 0, none, -10, 60, -5, energy, false)
        );

        jobs[0] = new Jobs(
                new Action("Пособирать бутылки", 25, bottle, -10, -20, -10, Constants.jobs, true),
                new Action("Пособирать монеты", 60, rub, -10, -20, -10, Constants.jobs, true),
                new Action("Украть бабки у уличных музыкантов", 100, rub, -15, -30, -15, Constants.jobs, false)
        );

        locations[1] = new Location(3000,
                //Food
                new Action("Жрать объетки с помойки", 0, rub, -10, -15, 25, food, true),
                new Action("Купить шаурму", -100, rub, -5, -5, 30, food, true),
                new Action("Купить шашлык", -250, rub, -5, -5, 80, food, true),
                new Action("Отнять еду у доставщика пиццы", 0, none, -5, -5, 60, food, false),
                //Med
                new Action("Пособирать травы", 0, rub, 10, -15, -5, health, true),
                new Action("Купить пилюли", -50, rub, 35, -5, -5, health, true),
                new Action("Найти в подъезде бывшего врача", -190, rub, 70, -10, -5, health, true),
                new Action("Украсть у деда лекарства", 0, none, 60, -20, -5, health, false),
                //Energy
                new Action("Поспать в палатке", 0, none, -5, 15, -5, energy, true),
                new Action("Бухнуть с гопниками",  -50, rub, -5, 35, -5, energy, true),
                new Action("Купить палёный абсент", -150, rub, -5, 70, -5, energy, true),
                new Action("Украсть РедБулл", 0, none, -10, 50, -5, energy, false)
        );

        jobs[1] = new Jobs(
                new Action("Пойти с Василием за бутылками", 45, bottle, -5, -20, -10, Constants.jobs, true),
                new Action("Пособирать монеты из фантана", 120, rub, -10, -20, -10, Constants.jobs, true),
                new Action("Собирать макулатуру на свалках", 190, rub, -5, -40, -20, Constants.jobs, true),
                new Action("Сдать люк на металл", 200, rub, -5, -20, -5, Constants.jobs, false)
        );

        jobs[2] = new Jobs(
                new Action("Пособирать монеты из фантана", 120, rub, -5, -20, -10, Constants.jobs, true),
                new Action("Рассклеивать листовки с Валерой", 250, rub, -10, -30, -20, Constants.jobs, true),
                new Action("Стырить магнитолу", 300, rub, -10, -35, -20, Constants.jobs, false),
                new Action("Отжать мобилку", 350, rub, -10, -45, -15, Constants.jobs, false)
        );

        locations[2] = new Location(6000,
                //Food
                new Action("Сварить гречку", -80, rub, -5, -5, 25, food, true),
                new Action("Сделать похлебку", -270, rub, -5, -5, 50, food, true),
                new Action("Купить птицу грилль", -450, rub, -5, -5, 80, food, true),
                new Action("Отнять еду у доставщика пиццы", 0, none, -5, -5, 70, food, false),
                //Med
                new Action("Делать физ. упражнения", 0, rub, 10, -15, -5, health, true),
                new Action("Купить пилюли", -100, rub, 30, -5, -5, health, true),
                new Action("Приготовить лечебный отвар", -250, rub, 70, -10, -5, health, true),
                new Action("Не придумал", 0, none, 80, -20, -5, health, false),
                //Energy
                new Action("Поспать в гараже", 0, none, -5, 15, -5, energy, true),
                new Action("Найти собутыльников за гаражами",  -100, rub, -5, 35, -5, energy, true),
                new Action("Купить коньяк", -280, rub, -5, 70, -5, energy, true),
                new Action("Не придумал", 0, none, -10, 80, -5, energy, false)
                );

        jobs[3] = new Jobs(
                new Action("Мести дворы", 150, rub, -5, -10, -5, Constants.jobs, true),
                new Action("Подработать грузчиком", 300, rub, -5, -25, -5, Constants.jobs, true),
                new Action("Работать бомбилой", 470, rub, -10, -30, -15, Constants.jobs, true),
                new Action("Украсть сумочку", 500, rub, -10, -10, -10, Constants.jobs, false)
        );
    }

    public interface VipItemListener{
        void saveSettings();
    }
}
