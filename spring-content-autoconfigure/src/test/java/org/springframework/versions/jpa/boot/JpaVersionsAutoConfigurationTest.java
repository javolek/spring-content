package org.springframework.versions.jpa.boot;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jSpringRunner;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.support.TestEntityVersioned;
import org.springframework.versions.LockingAndVersioningRepository;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(Ginkgo4jSpringRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class JpaVersionsAutoConfigurationTest {

    private AnnotationConfigApplicationContext context;

    {
        Describe("JpaVersionsAutoConfiguration", () -> {
            Context("given an application context with a LockingAndVersioningRepository", () -> {
                BeforeEach(() -> {
                    context = new AnnotationConfigApplicationContext();
                    context.register(StarterTestConfig.class);
                    context.refresh();
                });

                It("should include the repository bean", () -> {
                    MatcherAssert.assertThat(context, is(not(nullValue())));
                    MatcherAssert.assertThat(context.getBean(TestEntityRepository.class), is(not(nullValue())));
                });
            });
        });
    }

    @Test
    public void test() {
    }

    @Configuration
    @PropertySource("classpath:default.properties")
    @EnableAutoConfiguration
    @EnableJpaRepositories( considerNestedRepositories = true,
                            basePackages = {"org.springframework.versions.jpa.boot",
                                            "org.springframework.versions"})
    @EntityScan(basePackages="org.springframework.support")
    public static class StarterTestConfig {
    }

    public interface TestEntityRepository extends CrudRepository<TestEntityVersioned, Long>, LockingAndVersioningRepository<TestEntityVersioned, Long> {}
}
