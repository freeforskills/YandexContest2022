import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Massive {
    public static Map<String,Integer> map = new HashMap<>();

    public static void main(String[] args) throws IOException {
        //String indata = "[1, 345, 345, 2, 3, [5, 5], 6, 6, [7, 8, 9, [10, 11]]]";
        //String indata = "[[[[[[1]]]], []]]";
        //String indata = "[0, 10, 2, 5, -999999999]";
        BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));
        checkMsv(bfr.readLine());
        System.out.println(calculateMap());
    }

    private static void checkMsv(String indata){
        // Если индата пуста или меньше 3 символов - выход
    if (indata==null || indata.length()<3) return;
    StringBuilder stb = new StringBuilder(indata);
        // Если индата не содержит по краям [] - выход
    if ((stb.charAt(0)!='[') || (stb.charAt(stb.length()-1)!=']')) return;
        // Убираем по карям [].
        stb.deleteCharAt(0);
        stb.deleteCharAt(stb.length()-1);
        // Ищем первую открытую [ fOS
        int fOS = stb.indexOf("[");
        // Ищем первую закрытую ] fZS
        int fZS = stb.indexOf("]");
        // Если скобок нет, то это строка -> отправляем ее в метод обработки строк
        if (fOS<0 & fZS<0) {
            for (int i =0; i<stb.length()-1; i++) if (stb.charAt(i)==',') stb.deleteCharAt(i);
            checkString(stb.toString());
            return;
        // Если косяк со скобками - выход
        } else if ((fOS>=0 & fZS<0) | (fOS<0 & fZS>=0)) return;
        // Тут получаем нечто такое 1, 2, 3, [5, 5], 6, [7, 8, 9, [10, 11]]
        // Начинаем искать железные "массивы" вида [1,2,3]
        for (int i = fOS; i<fZS;i++) {
            if (stb.charAt(i)=='[') fOS=i;
        }
        // Получены индексы железного массива fOS-fZS.
        // Отправляем этот массив в метод проверки массива.
        if (((fZS+1)-fOS>2)) checkMsv(stb.substring(fOS,fZS+1));
        // Убираем этот массив из данных.
        stb.delete(fOS,fZS+1);
        // Упаковываем обратно в [] и снова отправляем этот массив в метод проверки массива.
        if (stb.length()>0) checkMsv("["+stb+"]");
    }

    private static void checkString(String data){
        // Запускаем сканер
        Scanner scn = new Scanner(data);
        // Если в строке есть хоть что-то, запускается проверка на инт
        while (scn.hasNext()) {
            if (scn.hasNextInt()) {
            String key = String.valueOf(scn.nextInt());
            // Начинается проверка на вхождение в мап
                if (map.putIfAbsent(key,1)!=null) map.replace(key, ((map.get(key))+1));
            } else scn.next();
        }
    }

    private static String calculateMap(){
        if (map.isEmpty()) return null;
        int max = Collections.max(map.values());
        StringBuilder stb = new StringBuilder();
        List<Integer> list = new ArrayList<>();
        for (Map.Entry<String,Integer> set:map.entrySet()) {
            if (set.getValue()==max) list.add(Integer.valueOf(set.getKey()));
        }
        Collections.sort(list);
        for (Integer value:list) {
            stb.append(value).append(" ");
        }
        stb.deleteCharAt(stb.length()-1);
        return stb.toString();
    }
}
