package csse3030.week7;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ProductSearchMetamorphicTest {

    @Provide
    Arbitrary<String> categories() {
        return Arbitraries.of("Electronics", "Home", "Toys");
    }

    @Provide
    Arbitrary<List<Product>> catalogs() {
        Arbitrary<String> names =
                Arbitraries.strings().alpha().ofMinLength(3).ofMaxLength(10);
        Arbitrary<Double> prices = Arbitraries.doubles().between(1.0, 500.0);
        Arbitrary<Double> ratings = Arbitraries.doubles().between(1.0, 5.0);
        Arbitrary<Product> products =
                Combinators.combine(names, categories(), prices, ratings)
                        .as(Product::new);
        return products.list().ofMaxSize(20);
    }

    // TODO (practical Part 4): add two @Property methods here --
    //   1. a permutation relation: reordering the catalog shouldn't change
    //      which products match a given filter.
    //   2. a subset/inclusion relation: narrowing a search filter with
    //      and(f, g) should only ever shrink the result set.
}
