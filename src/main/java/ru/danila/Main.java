package ru.danila;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Main {
    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee("Jack", "London", 24, Position.CHEF),
                new Employee("Simon", "Berry", 24, Position.WORKER),
                new Employee("Sam", "Musk", 28, Position.MANAGER),
                new Employee("Hector", "Smith", 25, Position.WORKER),
                new Employee("John", "Jones", 25, Position.WORKER),
                new Employee("Gary", "Cooper", 25, Position.DEVOPS),
                new Employee("James", "Klein", 30, Position.WORKER),
                new Employee("Robert", "Lew", 34, Position.MANAGER),
                new Employee("Kristian", "Faust", 36, Position.WORKER),
                new Employee("Ruby", "Lewis", 36, Position.WORKER),
                new Employee("Alex", "Dunn", 33, Position.DEVOPS),
                new Employee("Tomas", "Petrov", 32, Position.WORKER),
                new Employee("Calvin", "Klein", 37, Position.WORKER)
        );
        log.info("Employees under 35");
        Stream<Employee> sortedEmployee = employees.stream().filter(employee -> employee.getAge() < 35);
        getEmployees(sortedEmployee);

        log.info("Employees above 26");
        Stream<Employee> filterEmployees = employees.stream().filter(employee -> employee.getAge() > 26);
        getEmployees(filterEmployees);

        log.info("Sorted by age");
        Stream<Employee> sortedByAge = employees.stream().sorted(Comparator.comparingInt(Employee::getAge));
        getEmployees(sortedByAge);

        log.info("Sorted by name");
        Stream<Employee> sortedByName = employees.stream().sorted(Comparator.comparing(Employee::getFirstName));
        getEmployees(sortedByName);

        log.info("Min age");
        Optional<Employee> min = employees.stream().min(Comparator.comparingInt(Employee::getAge));
        getEmployees(min.stream());

        log.info("Max age");
        Employee maxAge = employees.stream().max(Comparator.comparingInt(Employee::getAge)).get();
        getEmployees(List.of(maxAge).stream());

        double average = employees.stream().mapToInt(Employee::getAge).average().getAsDouble();
        log.info(String.format("Average age: %5s", average));

        int sum = employees.stream().mapToInt(Employee::getAge).sum();
        log.info(String.format("Sum of ages:%5d", sum));

        log.info("Employees which age above average");

        Stream<Employee> employeeStream = employees.stream().filter(employee -> employee.getAge() > average);
        Map<String, Integer> aboveAverage = employeeStream.collect(Collectors.toMap(Employee::getFirstName, Employee::getAge));
        log.info(aboveAverage.toString());

        log.info("most frequently repeated age");

        Map<Integer, Integer> mapOfAges = new HashMap<>();
        employees.stream().mapToInt(Employee::getAge)
                .forEach(age -> mapOfAges.put(age, (int) employees.stream().filter(emp -> emp.getAge() == age).count()));
        Map<Integer, ArrayList<String>> finallyMap = new HashMap<>();

        Map<Integer, Integer> mapOfMaxAges = new HashMap<>();
        mapOfAges.forEach((key, value) -> {
            if (value.equals(mapOfAges.values().stream().max(Comparator.naturalOrder()).get()))
                mapOfMaxAges.put(key, value);
        });

        int randomMostRecurringAge = mapOfMaxAges.keySet().stream().findAny().get();

        ArrayList<String> listOfNames = new ArrayList<>();
        employees.forEach(e -> {
            if (e.getAge() == randomMostRecurringAge)
                listOfNames.add(String.format("%s %s", e.getFirstName(), e.getLastName()));
        });
        finallyMap.put(randomMostRecurringAge, listOfNames);
        log.info(finallyMap.toString());

        log.info("Grouped by age 25");
        List<Employee> groupedByAge = employees.stream()
                .collect(Collectors.groupingBy(Employee::getAge)).get(25);
        getEmployees(groupedByAge.stream());

        log.info("Grouped by position Worker");
        List<Employee> groupedByPosition = employees.stream()
                .collect(Collectors.groupingBy(Employee::getPosition)).get(Position.WORKER);
        getEmployees(groupedByPosition.stream());
    }

    public static void getEmployees(Stream<Employee> stream) {
        List<Employee> objects = stream.collect(Collectors.toList());
        objects.forEach(employee -> log.info(String.format("%10d | %-15s %-10s age %d %10s",
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAge(),
                employee.getPosition().name())));
    }

}

class Employee {
    private int id;
    private String firstName;
    private String LastName;
    private int age;
    private Position position;

    public Employee(String firstName, String lastName, int age, Position position) {
        this.id = new Random().nextInt((1000) + 100);
        this.firstName = firstName;
        LastName = lastName;
        this.age = age;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", age=" + age +
                ", position=" + position +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}

enum Position {
    CHEF, WORKER, MANAGER, DEVOPS
}