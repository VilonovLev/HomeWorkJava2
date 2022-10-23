package com.company;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class Main {
    static Logger logger;
    static FileHandler fileHandler;
    static int countFiles = 1;

    //Класс для 1 задачи
    public static class Student{
       static List<Student> students = new ArrayList<>();
        @SerializedName("фамилия")
       private final String lastName;
        @SerializedName("оценка")
       private final int assessment;
        @SerializedName("предмет")
       private final String subject;

       public Student(String lastName, int assessment , String subject ){
            this.lastName = lastName;
            this.assessment = assessment;
            this.subject = subject;
            students.add(this);
       }

        public static List<Student> getStudents() {
            return students;
        }

        public static void setStudents(List<Student> students) {
            Student.students = students;
        }

        @Override
        public String toString() {
            return "{" +
                    "lastName='" + lastName + '\'' +
                    ", assessment=" + assessment +
                    ", subject='" + subject + '\'' +
                    '}';
        }

        public String getLastName() {
            return lastName;
        }

        public int getAssessment() {
            return assessment;
        }

        public String getSubject() {
            return subject;
        }
    }

    public static void createJsonFile(String nameFile){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(Student.getStudents());
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(nameFile,false);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void swapElemets(int[] array, int indexFirst, int indexSecond){
        int temp = array[indexFirst];
        array[indexFirst] = array[indexSecond];
        array[indexSecond] = temp;
    }

    public static double input(String in) {
        System.out.print(in);
        return (new Scanner(System.in)).nextDouble();
    }

    //Задача 1 ч.1
    public static void searchRowJson(String json, String request){
        logger = Logger.getAnonymousLogger();
        String[] row  = json.replaceAll("[ {}\"]","").split(",");
        request = request.replaceAll("[=]",":");
        for (String attribute:row) {
            if(request.equals(attribute)){
                logger.info(json);
            }
        }
    }
    //Задача 1 ч.2
    public static void showJsonFile(String nameFile){
        Reader fileReader = null;
        try {
            fileReader = new FileReader(nameFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Type listType = new TypeToken<List<Student>>(){}.getType();
        List<Student> list = (new Gson().fromJson(fileReader,listType));
        Logger logger = Logger.getAnonymousLogger();

        for (Student student: list) {
            logger.info(String.format("Студент %s получил %s по предмету %s.",
                    student.getLastName(),
                    student.getAssessment(),
                    student.getSubject()));
        }
    }
    //Задача 2
    public static void sortAndLog(int[] array) {
        logger = Logger.getAnonymousLogger();
        try {
            fileHandler = new FileHandler("sortArray.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.info("initial array " + Arrays.toString(array));
            for (int i = 0; i < array.length; i++) {
                for (int j = 1; j < array.length - i; j++) {
                    if (array[j] < array[j - 1]) {
                        swapElemets(array,j-1,j);
                        logger.info(Arrays.toString(array));
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    //Задача 3
    public static void printExtensionFiles (File file){
        File[] files = file.listFiles();
        for (File obj : files) {
            String resultString = " Расширение файла: ";
            if (obj.isDirectory()) {
                printExtensionFiles(obj);
            } else {
                Matcher matcher = Pattern.compile("(\\w*?$)").matcher(obj.getName());
                matcher.find();
                resultString += matcher.group();
            }
            System.out.println(countFiles + resultString);
            ++countFiles;
        }
    }
    //Задача 4
    public static void calculator() {
        logger = Logger.getAnonymousLogger();
        try {
            fileHandler = new FileHandler("calculator.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            double x = input("Введите первое число: ");
            System.out.print("Введите операцию: ");
            String str = (new Scanner(System.in)).next();
            double y = input("Введите второе число: ");
            str = switch (str) {
                case "/" -> x + " / " + y + " = " + (x / y);
                case "*" -> x + " * " + y + " = " + (x * y);
                case "+" -> x + " + " + y + " = " + (x + y);
                case "-" -> x + " - " + y + " = " + (x - y);
                case "%" -> x + " % " + y + " = " + (x % y);
                default -> "Некорректный ввод!";
            };
            logger.info(str);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //Создание .json файла
        new Student("Иванов", 5, "Математика");
        new Student("Петрова", 4, "Информатика");
        new Student("Краснов", 5, "Физика");

        createJsonFile("students.json");

        //  Задание 1 ч.1
        String json = "{\"name\":\"Ivanov\", \"country\":\"Russia\", \"city\":\"Moscow\", \"age\":\"null\"}";
        String request = "name=Ivanov";
        searchRowJson(json,request);
        // Задача 1 ч.2
        showJsonFile("students.json");
        //  Задание 2 - Реализуйте алгоритм сортировки пузырьком числового массива,
        //  результат после каждой итерации запишите в лог-файл.
        int[] arrayInt = {1,9,45,29,0,4};
        sortAndLog(arrayInt);

        //  Задание 3 - Напишите метод, который определит тип (расширение) файлов из текущей папки
        //  и выведет в консоль результат вида
        printExtensionFiles(new File("").getAbsoluteFile());

        //  Задание 4 - К калькулятору из предыдущего дз добавить логирование.
        calculator();
    }
}
