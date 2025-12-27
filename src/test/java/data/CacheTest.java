package data;

import com.next.engine.data.Cache;
import org.junit.jupiter.api.Test;

public class CacheTest {

    @Test
    public void test() {
        Cache<String, String> cache = new Cache<>();
        String a = cache.getOrCache("a", () -> "a");
        String b = cache.getOrCache("b", () -> "b");
        String c = cache.getOrCache("c", () -> "c");
        String d = cache.getOrCache("a", () -> "a");
        String e = cache.getOrCache("b", () -> "b");

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
