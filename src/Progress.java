import java.io.*;
import java.util.*;

public class Progress {
    public static void main(String[] args){

        String[] dataString = getMassiveStrings();
        List<Integer[]> listInts = getData(dataString);
        String out = operateData(listInts);
        System.out.println(out);
    }

    // Метод возвращает массив со строками, если все прошло хорошо.
    // Либо возвращает нул, если был косяк в считывании данных.
    private static String[] getMassiveStrings() {
        // Блок для использования ввода из файла.
        //try {
        //    BufferedReader bfr = new BufferedReader(new FileReader("input.txt"));
        //} catch (FileNotFoundException e) {
        //    throw new RuntimeException(e);
        //}
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        try {
            // Читаем первую строку на наличие инт.
            int numbOfProgress = Integer.parseInt(bfr.readLine());
            //Проверяем инт на условия
            if (numbOfProgress <1 || numbOfProgress > 100) throw new NumberFormatException();
            // Создаем массив стрингов длиной по количеству прогрессий
            String[] dataString = new String[numbOfProgress];
            // Запускаем цикл чтения строк
            for (int lines = 0; lines< numbOfProgress; lines++) {
                try {
                    // Начинаем парсить строки
                    String tempLine = bfr.readLine();
                    // Если строка нулл или < 1, бросаем ошибку рвем цикл.
                    if (tempLine==null || tempLine.isEmpty()) throw new IOException();
                    // обавляем строку в массив стрингов
                    dataString[lines] = tempLine;
                } catch (IOException e) {
                    System.out.println("Некорректные данные при считывании прогрессий. Метод getMassiveStrings.");
                    return null;
                }
            }
            // Возвращаем массив строк dataString.
            return dataString;
        } catch (NumberFormatException | IOException e) {
            System.out.println("Некорректные данные первой строки Data. Метод getMassiveStrings.");
        }
        return null;
    }

    // Метод получает массив со строками, проверяет строки на валидность и возвращает
    // лист с массивом интов, в противном случае возвращает нулл
    private static List<Integer[]> getData(String[] dataString){
        try {
            if (dataString==null) throw new IOException();
            // Создаем лист массивов с интами == команды и данные
            List<Integer[]> listOfInts = new ArrayList<>();
            // Запускается проверка всех строк стрингового массива
            for (String line : dataString) {
                Scanner scn = new Scanner(line);
                try {
                    // Читает строку на первый инт
                    // Тут может вывалиться исключение на не инт
                    if (!scn.hasNext() || !scn.hasNextInt()) throw new NumberFormatException();
                    int numberOfAction = scn.nextInt();
                    // Проверка инта 1<=numberOfAction<=3
                    if (numberOfAction < 1 || numberOfAction > 3) throw new NumberFormatException();
                    int numberOfInts = switch (numberOfAction) {
                        case 1 -> 3;
                        case 2 -> 1;
                        default -> 0;
                    };
                    // Создаем массив для интов.
                    Integer[] intsMsv = new Integer[numberOfInts + 1];
                    // Добавляем индекс команды
                    intsMsv[0] = numberOfAction;

                    // Начинаем наполнять массив интами.
                    for (int values = 0; values < numberOfInts; values++) {
                        // Если в строке дальше ничего нет, бросаем исключение.
                        if (!scn.hasNext() || !scn.hasNextInt()) throw new NumberFormatException();
                        int tempInt = scn.nextInt();
                        // Тут может вывалиться исключение т.к. не найдет инт
                        // Проверка значений
                        if (tempInt < -1000000000 || tempInt > 1000000000) throw new NumberFormatException();
                        intsMsv[values + 1] = tempInt;
                    }
                    // Тут на выходе получен массив с интами.
                    // Добавляем массив в лист
                    listOfInts.add(intsMsv);
                } catch (NumberFormatException e) {
                    System.out.println("Некорректные данные при считывании прогрессий. Метод getData, поверка строк.");
                    return null;
                }
            }
            return listOfInts;
        } catch (IOException e) {
            System.out.println("Некорректные данные при считывании прогрессий. Метод getData основной блок.");
            return null;
        }
    }

    // Метод производит операции со структурой и возвращает строку с полученными значениями
    private static String operateData(List<Integer[]> listOfIntMsv){
        Map<Integer, Integer[]> hashmap = new TreeMap<>();
        StringBuilder stb = new StringBuilder();

        if (listOfIntMsv==null) return null;
        for (Integer[] msv:listOfIntMsv) {
            switch (msv[0]) {
                case 1 -> stAdd(msv[1], msv[2], msv[3], hashmap);
                case 2 -> stDel(msv[1], hashmap);
                case 3 -> stb.append(stGet(hashmap)).append("\n");
            }
        }
        if (stb.length()>=1) stb.deleteCharAt(stb.length()-1);
        return stb.toString();
    }

    private static boolean stAdd(int valueA, int valueB, int iD, Map<Integer,Integer[]> hashmap){
        Integer[] value = {valueA,valueB};
        return (hashmap.putIfAbsent(iD, value) == null);
    }

    private static boolean stDel(int iD, Map<Integer,Integer[]> hashmap){
        return (hashmap.remove(iD)!=null);
    }

    // Метод ищет минимальное значение первого индекса массива из все данных мапа
    private static Integer stGet(Map<Integer,Integer[]> hashmap){
        if (hashmap.isEmpty()) return null;
        // Пробегаем по значениям и ищем минимальное
        int minValue=0;
        boolean minValueOk = false;
        for (Integer[] msv:hashmap.values()) {
            if (!minValueOk) {
                minValue = msv[0];
                minValueOk=true;
            } else {
                if (msv[0] < minValue) minValue=msv[0];
            }
        }
        // Тут получено минимальное значение
        // Создаем сет для минимумов.
        Set<Integer> idOfMin = new TreeSet<>();
        // Запускаем проход мапа
        for (Map.Entry<Integer, Integer[]> entry : hashmap.entrySet()) {
            int iD = entry.getKey();
            int msvA = entry.getValue()[0];
            // minValue
            //Запускаем фильтр минимумов и кладем iD с минимумами в сет
            if (msvA == minValue) idOfMin.add(iD);
        }
        // Тут получен сет iD с минимумами.
        // Получаем первый iD и находим его в мапе.
        if (idOfMin.iterator().hasNext()) {
            int firstMiniD = idOfMin.iterator().next();
            // Читаем масиив у iD
            Integer[] msv = hashmap.get(firstMiniD);
            int returnValue = msv[0];
            // Считаем сл элемент прогрессии.
            msv[0]=msv[0]+msv[1];
            //Кладем в мап новые данные
            hashmap.put(firstMiniD,msv);
            return returnValue;
        } else return null;
    }
}
