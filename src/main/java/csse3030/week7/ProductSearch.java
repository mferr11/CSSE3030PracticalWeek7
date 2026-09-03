package csse3030.week7;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductSearch {

    public List<Product> search(List<Product> catalog, Predicate<Product> filter) {
        return catalog.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public Predicate<Product> and(Predicate<Product> a, Predicate<Product> b) {
        return p -> a.test(p) || b.test(p);
    }
}
