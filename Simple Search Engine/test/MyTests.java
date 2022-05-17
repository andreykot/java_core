import org.junit.Test;
import org.junit.Assert;

import search.core.InvertedIndexSearch;


public class MyTests {
    String[] data = new String[] {
            "Donald Trump d.trump@yandex.com",
            "Novak Djokovic n.djokovic@gmail.com",
            "Valerii Leontiyev legenda@gmail.com",
            "Tsar Nikolai nikolai@tsar.org",
            "Valerii Meladze legenda@gmail.com"
    };

    @Test
    public void TestInvertedIndexSearchStrategyAny() {

        InvertedIndexSearch search = new InvertedIndexSearch(data);

        String[] resAll1 = search.findAny("Valerii Leontiyev");
        Assert.assertArrayEquals(
                resAll1
                , new String[] {"Valerii Leontiyev legenda@gmail.com", "Valerii Meladze legenda@gmail.com"}
        );

        String[] resAll2 = search.findAny("donald duck");
        Assert.assertArrayEquals(
                resAll2
                , new String[] {"Donald Trump d.trump@yandex.com"}
        );

        String[] resAll3 = search.findAny("legenda@gmail.com");
        Assert.assertArrayEquals(
                resAll3
                , new String[] {"Valerii Leontiyev legenda@gmail.com", "Valerii Meladze legenda@gmail.com"}
        );

        String[] resAll4 = search.findAny("Ponasenkov");
        Assert.assertArrayEquals(
                resAll4
                , new String[] {}
        );
    }

    @Test
    public void TestInvertedIndexSearchStrategyAll() {

        InvertedIndexSearch search = new InvertedIndexSearch(data);

        String[] resAll1 = search.findAll("Valerii Leontiyev");
        Assert.assertArrayEquals(
                resAll1
                , new String[] {"Valerii Leontiyev legenda@gmail.com"}
        );

        String[] resAll2 = search.findAll("donald duck");
        Assert.assertArrayEquals(
                resAll2
                , new String[] {}
        );

        String[] resAll3 = search.findAll("legenda@gmail.com");
        Assert.assertArrayEquals(
                resAll3
                , new String[] {"Valerii Leontiyev legenda@gmail.com", "Valerii Meladze legenda@gmail.com"}
        );

        String[] resAll4 = search.findAll("Ponasenkov");
        Assert.assertArrayEquals(
                resAll4
                , new String[] {}
        );
    }

    @Test
    public void TestInvertedIndexSearchStrategyNone() {

        InvertedIndexSearch search = new InvertedIndexSearch(data);

        String[] resAll1 = search.findDifferent("Valerii Leontiyev");
        Assert.assertArrayEquals(
                resAll1
                , new String[] {
                        "Donald Trump d.trump@yandex.com",
                        "Novak Djokovic n.djokovic@gmail.com",
                        "Tsar Nikolai nikolai@tsar.org"
                }
        );

        String[] resAll2 = search.findDifferent("donald duck");
        Assert.assertArrayEquals(
                resAll2
                , new String[] {
                        "Novak Djokovic n.djokovic@gmail.com",
                        "Valerii Leontiyev legenda@gmail.com",
                        "Tsar Nikolai nikolai@tsar.org",
                        "Valerii Meladze legenda@gmail.com"
                }
        );

        String[] resAll3 = search.findDifferent("legenda@gmail.com");
        Assert.assertArrayEquals(
                resAll3
                , new String[] {
                        "Donald Trump d.trump@yandex.com",
                        "Novak Djokovic n.djokovic@gmail.com",
                        "Tsar Nikolai nikolai@tsar.org"
                }
        );

        String[] resAll4 = search.findDifferent("Ponasenkov");
        Assert.assertArrayEquals(
                resAll4
                , new String[] {
                        "Donald Trump d.trump@yandex.com",
                        "Novak Djokovic n.djokovic@gmail.com",
                        "Valerii Leontiyev legenda@gmail.com",
                        "Tsar Nikolai nikolai@tsar.org",
                        "Valerii Meladze legenda@gmail.com"
                }
        );
    }

    @Test
    public void TestListAllPeople() {

        InvertedIndexSearch search = new InvertedIndexSearch(data);

        Assert.assertArrayEquals(
                search.getAll()
                , data
        );
    }

}
