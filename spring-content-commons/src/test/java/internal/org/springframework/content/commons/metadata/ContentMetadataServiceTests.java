package internal.org.springframework.content.commons.metadata;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;
import lombok.Getter;
import lombok.Setter;
import org.junit.runner.RunWith;

import org.springframework.content.commons.metadata.ContentMetadataService;
import org.springframework.data.annotation.Id;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.FIt;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;

@RunWith(Ginkgo4jRunner.class)
public class ContentMetadataServiceTests {

    private ContentMetadataService metadata = null;

    // args
    private TEntity entity = null;

    private Object result = null;

    {
        Describe("#get", () -> {

            JustBeforeEach(() -> {
                metadata = new ContentMetadataServiceImpl();
            });

            BeforeEach(() -> {
                entity = new TEntity();
                entity.id = 12345L;
            });

            It("should return the requested value", () -> {
                assertThat(metadata.get(entity, Id.class), is(12345L));
            });

            FIt("should use the cache when called more than once with the same class of object", () -> {
                long start = System.nanoTime();
                metadata.get(entity, Id.class);
                long finish = System.nanoTime();
                long timeElapsedFirst = finish - start;

                for (int i=0; i < 100; i++) {
                    start = System.nanoTime();
                    metadata.get(entity, Id.class);
                    finish = System.nanoTime();
                    long timeElapsedSecond = finish - start;

                    assertThat(timeElapsedSecond, is(lessThan(timeElapsedFirst)));
                    System.out.println(format("%s < %s", timeElapsedSecond, timeElapsedFirst));
                }
            });
        });
    }


//    @Getter
//    @Setter
    public static class TEntity {

        @Id
        public Long id;

    }
}
