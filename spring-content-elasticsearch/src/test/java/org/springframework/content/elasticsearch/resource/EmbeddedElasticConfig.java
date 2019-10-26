//package org.springframework.content.elasticsearch.resource;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//
//import javax.annotation.PreDestroy;
//
//import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
//
//import org.springframework.content.elasticsearch.ElasticsearchResourceConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.core.io.Resource;
//
//import static java.util.concurrent.TimeUnit.MINUTES;
//import static pl.allegro.tech.embeddedelasticsearch.PopularProperties.CLUSTER_NAME;
//
//@Configuration
//public class EmbeddedElasticConfig {
//
//	@Bean
//	public EmbeddedElastic embeddedElastic() throws IOException, InterruptedException {
//		return EmbeddedElastic.builder()
//				.withElasticVersion("6.4.3")
//				.withSetting(CLUSTER_NAME, "testCluster")
//				.withSetting("discovery.zen.ping_timeout", 0)
//				.withPlugin("ingest-attachment")
//				.withEsJavaOpts("-Xms128m -Xmx512m")
//				.withStartTimeout(1, MINUTES)
//				.build()
//				.start();
//	}
//
//	@PreDestroy
//	public void tearDown() throws IOException, InterruptedException {
//		embeddedElastic().stop();
//	}
//}
