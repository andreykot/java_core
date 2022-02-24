package search.cli;

import search.core.Search;
import search.core.SearchType;

import java.util.Scanner;
import java.util.stream.Stream;

public class Menu {
    Scanner scanner;
    Search search;

    final int findStatus = 1;
    final int printAllStatus = 2;
    final int exitStatus = 0;

    public Menu(Scanner scanner, Search search) {
        this.scanner = scanner;
        this.search = search;
    }

    public void run() {
        int currentStatus = -1;
        while (currentStatus != exitStatus) {
            printMenu();
            currentStatus = Integer.parseInt(scanner.nextLine());

            switch (currentStatus) {
                case findStatus:
                    findPerson();
                    break;
                case printAllStatus:
                    printAll();
                    break;
                case exitStatus:
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Incorrect option! Try again.");
                    break;
            }

        }
    }

    private void findPerson() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        SearchType searchType = SearchType.valueOf(scanner.nextLine());
        search.setType(searchType);

        System.out.println("Enter a name or email to search all suitable people.");
        String pivot = scanner.nextLine();

        String[] result = search.find(pivot);
        if (result.length > 0) {
            Stream.of(result).forEach(System.out::println);
        } else {
            System.out.println("No matching people found.");
        }
    }

    private void printAll() {
        System.out.println("=== List of people ===");
        Stream.of(search.getAll()).forEach(System.out::println);
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== Menu ===");
        System.out.println("1. Find a person");
        System.out.println("2. Print all people");
        System.out.println("0. Exit");
        System.out.println();
    }
}
