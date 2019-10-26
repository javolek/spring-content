package org.springframework.content.elasticsearch.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import internal.org.springframework.content.commons.renditions.RenditionServiceImpl;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept;
import org.bytedeco.javacpp.tesseract;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.content.commons.renditions.RenditionProvider;
import org.springframework.content.commons.renditions.RenditionService;
import org.springframework.content.elasticsearch.EnableElasticsearchFulltextIndexing;
import org.springframework.content.jpa.config.EnableJpaStores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixReadMem;

@Configuration
@EnableElasticsearchFulltextIndexing
@EnableJpaRepositories(considerNestedRepositories = true)
@EnableJpaStores
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient client() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

    @Bean
    public ConversionService conversionService() {
        return new DefaultFormattingConversionService();
    }

    @Bean
    public RenditionProvider resourceConverter() {
        return new RenditionProvider() {

            @Override
            public String consumes() {
                return "image/png";
            }

            @Override
            public String[] produces() {
                return new String[] {"text/plain"};
            }

            @Override
            public InputStream convert(InputStream fromInputSource, String toMimeType) {
//                return new InputStreamResource(new ByteArrayInputStream("It was the best of times, the worst of times, it was the age of wisdom, it was the age of foolishness".getBytes()));

                tesseract.TessBaseAPI api = new tesseract.TessBaseAPI();

                // Initialize tesseract-ocr with English, without specifying tessdata path
                if (api.Init("/Users/warrep/workspace/tessdata", "eng") != 0) {
                    System.err.println("Could not initialize tesseract.");
                    System.exit(1);
                }

                // Open input image with leptonica library
                byte[] bytes = new byte[0];
                try {
                    bytes = IOUtils.toByteArray(fromInputSource);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                lept.PIX pix = pixReadMem(bytes, bytes.length);
                api.SetImage(pix);

                // Get OCR result
                BytePointer outText = api.GetUTF8Text();

                String txt = outText.getString();

                // Destroy used object and release memory
                api.End();
                outText.deallocate();
                pixDestroy(pix);

                return new ByteArrayInputStream(txt.getBytes());
            }
        };
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("org.springframework.content.elasticsearch");
        factory.setDataSource(dataSource());

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

    @Value("/org/springframework/content/jpa/schema-drop-hsqldb.sql")
    private Resource dropReopsitoryTables;

    @Value("/org/springframework/content/jpa/schema-hsqldb.sql")
    private Resource dataReopsitorySchema;

    @Bean
    DataSourceInitializer datasourceInitializer(DataSource dataSource) {
        ResourceDatabasePopulator databasePopulator =
                new ResourceDatabasePopulator();

        databasePopulator.addScript(dropReopsitoryTables);
        databasePopulator.addScript(dataReopsitorySchema);
        databasePopulator.setIgnoreFailedDrops(true);

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(databasePopulator);

        return initializer;
    }
}
