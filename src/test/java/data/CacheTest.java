package data;

import com.next.engine.data.Cache;
import org.junit.jupiter.api.Test;

public class CacheTest {

    @Test
    public void test() {
//        Cache<String, Integer> cache = new Cache<>(new Integer[8]);
//        cache.put(0);
//        cache.put(1);
//        cache.put(2);
//        cache.put(3);
//        cache.put(4);
//        cache.put(5);
//        cache.put(6);
//        cache.put(7);
//
//        int v = cache.get(2);
//        int w = cache.get(6);
//        int x = cache.get(7);
//
//        int y = cache.get(0);
//
//        cache.put(8);
//        cache.put(9);
//        cache.put(10);
//        cache.put(11);
//
//        System.out.println(v);
//        System.out.println(w);

        Cache<String, String> cache = new Cache<>();
        String a = cache.getOrInsertIfNotCached("a", () -> "a");
        String b = cache.getOrInsertIfNotCached("b", () -> "b");
        String c = cache.getOrInsertIfNotCached("c", () -> "c");
        String d = cache.getOrInsertIfNotCached("a", () -> "a");
        String e = cache.getOrInsertIfNotCached("b", () -> "b");

        IO.println("fim do teste :D");
    }

    @Test
    public void bitWiseTest() {
        int a = 7;      // 0111
        int b = 3;      // 0011
        int n = a & b;  // 0011 = 3
        IO.println(n);
    }
}
