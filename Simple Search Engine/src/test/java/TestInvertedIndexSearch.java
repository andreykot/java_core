import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import core.InvertedIndexSearch;


public class TestInvertedIndexSearch {
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
        assertArrayEquals(
                resAll1
                , new String[] {"Valerii Leontiyev legenda@gmail.com", "Valerii Meladze legenda@gmail.com"}
        );

        String[] resAll2 = search.findAny("donald duck");
        assertArrayEquals(
                resAll2
                , new String[] {"Donald Trump d.trump@yandex.com"}
        );

        String[] resAll3 = search.findAny("legenda@gmail.com");
        assertArrayEquals(
                resAll3
                , new String[] {"Valerii Leontiyev legenda@gmail.com", "Valerii Meladze legenda@gmail.com"}
        );

        String[] resAll4 = search.findAny("Ponasenkov");
        assertArrayEquals(
                resAll4
                , new String[] {}
        );
    }

    @Test
    public void TestInvertedIndexSearchStrategyAll() {

        InvertedIndexSearch search = new InvertedIndexSearch(data);

        String[] resAll1 = search.findAll("Valerii Leontiyev");
        assertArrayEquals(
                resAll1
                , new String[] {"Valerii Leontiyev legenda@gmail.com"}
        );

        String[] resAll2 = search.findAll("donald duck");
        assertArrayEquals(
                resAll2
                , new String[] {}
        );

        String[] resAll3 = search.findAll("legenda@gmail.com");
        assertArrayEquals(
                resAll3
                , new String[] {"Valerii Leontiyev legenda@gmail.com", "Valerii Meladze legenda@gmail.com"}
        );

        String[] resAll4 = search.findAll("Ponasenkov");
        assertArrayEquals(
                resAll4
                , new String[] {}
        );
    }

    @Test
    public void TestInvertedIndexSearchStrategyNone() {

        InvertedIndexSearch search = new InvertedIndexSearch(data);

        String[] resAll1 = search.findDifferent("Valerii Leontiyev");
        assertArrayEquals(
                resAll1
                , new String[] {
                        "Donald Trump d.trump@yandex.com",
                        "Novak Djokovic n.djokovic@gmail.com",
                        "Tsar Nikolai nikolai@tsar.org"
                }
        );

        String[] resAll2 = search.findDifferent("donald duck");
        assertArrayEquals(
                resAll2
                , new String[] {
                        "Novak Djokovic n.djokovic@gmail.com",
                        "Valerii Leontiyev legenda@gmail.com",
                        "Tsar Nikolai nikolai@tsar.org",
                        "Valerii Meladze legenda@gmail.com"
                }
        );

        String[] resAll3 = search.findDifferent("legenda@gmail.com");
        assertArrayEquals(
                resAll3
                , new String[] {
                        "Donald Trump d.trump@yandex.com",
                        "Novak Djokovic n.djokovic@gmail.com",
                        "Tsar Nikolai nikolai@tsar.org"
                }
        );

        String[] resAll4 = search.findDifferent("Ponasenkov");
        assertArrayEquals(
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

        assertArrayEquals(
                search.getAll()
                , data
        );
    }

}
