import lombok.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Pruebas {

    @Data @AllArgsConstructor
    class Product {
        String name;
        String code;
    }
    @Test
    public void test () {
        Product p1 = new Product("n1", "c1");
        Product p2 = new Product("n2", "c2");
        List<Product> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        String s1 = list.toString();
        System.out.println(s1);
        List<String> products = new ArrayList<>();
        products.add(p1.toString());
        products.add(p2.toString());
        String s2 = products.toString();
        System.out.println(products.toString() + "h1");
    }
}
