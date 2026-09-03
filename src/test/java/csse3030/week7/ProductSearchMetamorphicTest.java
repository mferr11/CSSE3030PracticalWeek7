package csse3030.week7;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.DoubleRange;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Metamorphic test for {@link ProductSearch}: no expected output is
 * hand-computed. Instead, a source query with filter {@code f} is compared
 * against a follow-up query with a strictly narrower filter
 * {@code and(f, g)} - the follow-up results must always be a subset of the
 * source results, regardless of the catalog.
 */
class ProductSearchMetamorphicTest {

    private final ProductSearch search = new ProductSearch();

    @Property
    void narrowingAFilterOnlyEverShrinksTheResultSet(
            @ForAll("catalogs") List<Product> catalog,
            @ForAll("categories") String category,
            @ForAll @DoubleRange(min = 0, max = 5) double minRating) {

        Predicate<Product> sourceFilter = p -> p.category().equals(category);
        Predicate<Product> narrowingFilter = p -> p.rating() >= minRating;

        List<Product> sourceResults = search.search(catalog, sourceFilter);
        List<Product> followUpResults =
                search.search(catalog, search.and(sourceFilter, narrowingFilter));

        assertTrue(
                sourceResults.containsAll(followUpResults),
                () -> "narrowing the filter must only ever remove results, but follow-up "
                        + followUpResults + " is not a subset of source " + sourceResults);
    }

    @Provide
    Arbitrary<List<Product>> catalogs() {
        Arbitrary<Product> products = Combinators.combine(
                Arbitraries.strings().withCharRange('a', 'z').ofMinLength(3).ofMaxLength(8),
                categories(),
                Arbitraries.doubles().between(0, 100),
                Arbitraries.doubles().between(0, 5)
        ).as(Product::new);
        return products.list().ofMinSize(0).ofMaxSize(20);
    }

    @Provide
    Arbitrary<String> categories() {
        return Arbitraries.of("electronics", "books", "toys", "clothing");
    }
}
