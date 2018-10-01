package Game;

public class Create implements Constants {

    public static Chain[] locationChains;
    public static Chain[] friends;
    public static Chain[] transports;
    public static Chain[] houses;
    public static Location[] locations = new Location[10];
    public static Jobs[] jobs = new Jobs[10];
    public static Vip[] vipStore;
    public static Study[] study;

    static {
        createVipStore();
        createStudy();

        locationChains = new Chain[]{
                new Chain("Помойка на окраине", 0, 0, 0, 0, 0, 0, none),
                new Chain("Подъезд", 1, 1, 0, 0, -1, 0, none),
                new Chain("Жилой район", 2, 2, 1, 1, -1, 0, none),
                new Chain("Дача", 3, 3, 3, 0, -1, 0, none),
                new Chain("Квартира в микрорайоне", 4, 4, 4, 0, -1, 0, none),
                new Chain("Центр города", 5, 5, 5, 0, -1, 0, none),
                new Chain("Берег моря", 6, 6, 5, 0, -1, 0, none),
        };

        friends = new Chain[]{
                new Chain("Без кореша", 0, 0, 0, 0, 0, 0, none),
                new Chain("Сосед по подъезду Василий", 0, 0, 0, 1, -1, -60, bottle),
                new Chain("Гопник Валера", 2, 0, 0, 1, -1, -300, bottle),
                new Chain("Бомбила Семён", 3, 0, 0, 2, -1, -1000, rub),
                new Chain("Прораб Михалыч", 4, 0, 0, 2, 4, 50, euro),
                new Chain("Программист Слава", 5, 4, 0, 4, 5, 200, euro),
                new Chain("Трейдер Юля", 6, 5, 0, 5, 6, 500, euro),
                new Chain("Директор IT компании Эдвард", 6, 5, 0, 5, 7, 30, bitcoin),
        };

        transports = new Chain[]{
                new Chain("Без транспорта", 0, 0, 0, 0, -1, 0, none),
                new Chain("Самокат", 0, 0, 0, 0, 0, -500, rub),
                new Chain("Велосипед", 0, 1, 0, 1, 1, -3000, rub),
                new Chain("Старая копейка", 0, 1, 0, 1, 2, -10000, rub),
                new Chain("Девятка", 0, 2, 3, 2, 3, -50000, rub),
                new Chain("Старая Иномарка", 0, 2, 3, 2, 4, -3300, euro),
                new Chain("Иномарка среднего класса", 0, 2, 3, 2, none, -8000, euro),
                new Chain("Спорткар", 0, 0, 0, 0, none, -500, bitcoin)
        };

        houses = new Chain[]{
                new Chain("Помойка", 0, 0, 0, 0, 0, 0, none),
                new Chain("Палатка б/у", 0, 0, 0, 0, 0, -1000, rub),
                new Chain("Гараж улитка", 2, 1, 0, 1, 0, -9000, rub),
                new Chain("Сарай", 2, 1, 0, 1, 0, -40000, rub),
                new Chain("Однушка", 2, 1, 0, 1, 0, -4000, euro),
                new Chain("Двухкомнатная в центре города", 2, 1, 0, 1, 0, -9000, euro),
                new Chain("Дом на берегу моря", 7, 1, 0, 1, 0, -600, bitcoin),
        };


        locations[0] = new Location(500,
                //Food
                new Action("Жрать объедки с помойки", 0, rub, -10, -15, 25, food, true),
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
                new Action("Украсть Редбулл", 0, none, -10, 60, -5, energy, false)
        );

        jobs[0] = new Jobs(
                new Action("Пособирать бутылки", 25, bottle, -10, -20, -10, Constants.jobs, true),
                new Action("Пособирать монеты", 60, rub, -10, -20, -10, Constants.jobs, true),
                new Action("Украсть бабки у уличных музыкантов", 100, rub, -15, -30, -15, Constants.jobs, false)
        );

        locations[1] = new Location(3000,
                //Food
                new Action("Жрать объедки с помойки", 0, rub, -10, -15, 25, food, true),
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
                new Action("Бухнуть с гопниками", -50, rub, -5, 35, -5, energy, true),
                new Action("Купить палёный абсент", -150, rub, -5, 70, -5, energy, true),
                new Action("Украсть Редбулл", 0, none, -10, 50, -5, energy, false)
        );

        jobs[1] = new Jobs(
                new Action("Пойти с Василием за бутылками", 45, bottle, -5, -20, -10, Constants.jobs, true),
                new Action("Пособирать монеты из фонтана", 120, rub, -10, -20, -10, Constants.jobs, true),
                new Action("Собирать макулатуру на свалках", 190, rub, -5, -40, -20, Constants.jobs, true),
                new Action("Сдать люк на металл", 200, rub, -5, -20, -5, Constants.jobs, false)
        );

        jobs[2] = new Jobs(
                new Action("Пособирать монеты из фонтана", 120, rub, -5, -20, -10, Constants.jobs, true),
                new Action("Расклеивать листовки с Валерой", 250, rub, -10, -30, -20, Constants.jobs, true),
                new Action("Стырить магнитолу", 300, rub, -10, -35, -20, Constants.jobs, false),
                new Action("Отжать мобилку", 350, rub, -10, -45, -15, Constants.jobs, false)
        );

        locations[2] = new Location(6000,
                //Food
                new Action("Сварить гречку", -80, rub, -5, -5, 25, food, true),
                new Action("Сделать похлебку", -270, rub, -5, -5, 50, food, true),
                new Action("Купить птицу гриль", -450, rub, -5, -5, 80, food, true),
                new Action("Отнять еду у доставщика пиццы", 0, none, -5, -5, 70, food, false),
                //Med
                new Action("Делать физ. упражнения", 0, rub, 10, -15, -5, health, true),
                new Action("Купить пилюли", -100, rub, 30, -5, -5, health, true),
                new Action("Приготовить лечебный отвар", -250, rub, 70, -10, -5, health, true),
                new Action("Ограбить аптеку", 0, none, 80, -20, -5, health, false),
                //Energy
                new Action("Поспать в гараже", 0, none, -5, 15, -5, energy, true),
                new Action("Найти собутыльников за гаражами", -100, rub, -5, 35, -5, energy, true),
                new Action("Купить коньяк", -280, rub, -5, 70, -5, energy, true),
                new Action("Отжать бухло у собутыльников", 0, none, -10, 80, -5, energy, false)
        );

        jobs[3] = new Jobs(
                new Action("Мести дворы", 150, rub, -5, -10, -5, Constants.jobs, true),
                new Action("Подработать грузчиком", 300, rub, -5, -25, -5, Constants.jobs, true),
                new Action("Работать бомбилой", 470, rub, -10, -30, -15, Constants.jobs, true),
                new Action("Украсть сумочку", 500, rub, -10, -10, -10, Constants.jobs, false)
        );

        locations[3] = new Location(25000,
                //Food
                new Action("Сварить мясной бульон", -170, rub, -5, -5, 25, food, true),
                new Action("Пойти в магаз", -350, rub, -5, -5, 50, food, true),
                new Action("Заказать пиццу", -600, rub, -5, -5, 80, food, true),
                new Action("Ограбить ларек", 0, none, -5, -5, 70, food, false),
                //Med
                new Action("Купить лекарства", -180, rub, 30, -5, -5, health, true),
                new Action("Сходить в больницу", -470, rub, 80, -10, -5, health, true),
                new Action("Знакомый врач", -7, euro, 100, -10, -5, health, true),
                //Energy
                new Action("Купить водяры", -150, rub, -5, 35, -5, energy, true),
                new Action("Купить коньяк", -400, rub, -5, 50, -5, energy, true),
                new Action("Купить хорошего вина", -9, euro, -5, 100, -5, energy, true)
        );

        jobs[4] = new Jobs(
                new Action("Месить цемент", 300, rub, -5, -10, -5, Constants.jobs, true),
                new Action("Клеить обои", 550, rub, -5, -25, -5, Constants.jobs, true),
                new Action("Класть плитку", 650, rub, -5, -30, -10, Constants.jobs, true),
                new Action("Тырить вещи со стройки", 10, euro, -10, -10, -10, Constants.jobs, false)
        );

        locations[4] = new Location(50000,
                //Food
                new Action("Пойти в Ашан", -350, rub, -5, -5, 30, food, true),
                new Action("Заказать пиццу", -600, rub, -5, -5, 50, food, true),
                new Action("Пойти в ресторан", -15, euro, -5, -5, 80, food, true),
                //Med
                new Action("Гос больница", -500, rub, 40, -10, -5, health, true),
                new Action("Знакомый врач", -7, euro, 40, -10, -5, health, true),
                new Action("Частная больница", -15, euro, 80, -5, -5, health, true),
                //Energy
                new Action("Купить абсент", -400, rub, -5, 35, -5, energy, true),
                new Action("Купить коньяк", -10, euro, -5, 50, -5, energy, true),
                new Action("Купить дорогой виски", -15, euro, -5, 100, -5, energy, true)
        );

        jobs[5] = new Jobs(
                new Action("Работать в колл-центре", 600, rub, -5, -10, -5, Constants.jobs, true),
                new Action("Работать в службе поддержки", 10, euro, -5, -15, -5, Constants.jobs, true),
                new Action("Кодить сайты", 15, euro, -10, -20, -15, Constants.jobs, true),
                new Action("Написать вирус", 1, bitcoin, -15, -30, -30, Constants.jobs, false),
                new Action("Написать Симулятор бомжа", 30, euro, -15, -30, -30, Constants.jobs, false)
        );

        locations[5] = new Location(150000,
                //Food
                new Action("Сходить в ТЦ", -600, rub, -5, -5, 30, food, true),
                new Action("Сходить в шашлычную", -13, euro, -5, -5, 50, food, true),
                new Action("Пойти в ресторан", -20, euro, -5, -5, 80, food, true),
                //Med
                new Action("Вызвать врача", -800, rub, 25, -10, -5, health, true),
                new Action("Купить иностранные таблетки", -12, euro, 40, -10, -5, health, true),
                new Action("Частная клиника", -25, euro, 80, -5, -5, health, true),
                //Energy
                new Action("Сходить в спортзал", -700, rub, -5, 35, -5, energy, true),
                new Action("Сходить в фитнес клуб", -17, euro, -5, 50, -5, energy, true),
                new Action("Нанять тренера", -25, euro, -5, 100, -5, energy, true)
        );

        jobs[6] = new Jobs(
                new Action("Зарабатывать на бинарных опционах", 1000, rub, -5, -10, -5, Constants.jobs, true),
                new Action("Управление капиталом", 17, euro, -5, -15, -5, Constants.jobs, true),
                new Action("Продажа акций", 35, euro, -10, -200, -15, Constants.jobs, true),
                new Action("Махинации с курсами валют", 2, bitcoin, -15, -30, -30, Constants.jobs, false)
        );

        locations[6] = new Location(350000,
                //Food
                new Action("Сходить на рыбалку", -13, euro, -5, -5, 30, food, true),
                new Action("Приготовить черепаху", -30, euro, -5, -5, 50, food, true),
                new Action("Посетить ресторан на плаву", -40, euro, -5, -5, 80, food, true),
                //Med
                new Action("Лечение пиявками", -15, euro, 20, -10, -5, health, true),
                new Action("Частная клиника", -28, euro, 40, -10, -5, health, true),
                new Action("Иностранные врачи", -50, euro, 80, -5, -5, health, true),
                //Energy
                new Action("Грязевые ванны", -15, euro, -5, 35, -5, energy, true),
                new Action("Сходить в фитнес клуб", -30, euro, -5, 50, -5, energy, true),
                new Action("Нырять с аквалангом", -50, euro, -5, 100, -5, energy, true)
        );

        jobs[7] = new Jobs(
                new Action("Руководить в отделе безопасности", 25, euro, -5, -10, -5, Constants.jobs, true),
                new Action("Управление компанией", 30, euro, -5, -15, -5, Constants.jobs, true),
                new Action("Разработка ПО для иностранных коллег", 70, euro, -10, -20, -15, Constants.jobs, true),
                new Action("Взлом базы данных конкурентов", 3, bitcoin, -15, -30, -30, Constants.jobs, false)
        );
    }

    public static void createVipStore() {
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
                new Vip("+10% к эффективности работы", false, 15) {
                    @Override
                    void reward(Player player) {
                        player.efficiency += .1;
                    }
                }
        };
    }

    public static void createStudy() {
        study = new Study[]{
                new Study(0, "Езда на самокате", 0, rub, 10),
                new Study(1, "Езда на велосипеде", 200, rub, 30),
                new Study(2, "ПДД", 1000, rub, 100),
                new Study(3, "Строение авто", 100, euro, 200),
                new Study(4, "Строительство", 100, euro, 200),
                new Study(5, "Программирование", 300, euro, 200),
                new Study(6, "Торговле валютами", 600, euro, 200),
                new Study(7, "Управление персоналом", 40, bitcoin, 200),
        };
    }
}
