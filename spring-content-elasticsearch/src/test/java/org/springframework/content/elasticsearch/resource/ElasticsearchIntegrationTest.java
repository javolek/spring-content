package org.springframework.content.elasticsearch.resource;

import java.io.InputStream;
import java.time.Duration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jSpringRunner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.content.commons.renditions.Renderable;
import org.springframework.content.commons.repository.ContentStore;
import org.springframework.content.commons.search.Searchable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;

import static com.github.grantwest.eventually.EventuallyLambdaMatcher.eventuallyEval;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.FIt;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Ginkgo4jSpringRunner.class)
//@Ginkgo4jConfiguration(threads=1)
@ContextConfiguration(classes = {/*EmbeddedElasticConfig.class, */ElasticsearchConfig.class})
public class ElasticsearchIntegrationTest {

	@Autowired
	private DocumentRepository repo;

	@Autowired
	private DocumentContentStore store;

//	@Autowired
//	private EmbeddedElastic server;

	@Autowired
	private RestHighLevelClient client;

	private Document doc1, doc2;
	private String id1, id2 = null;

	{
		Describe("Elasticsearch Integration With Resource Converter", () -> {

			Context("given a configured resource converter", () -> {

				Context("given a documents", () -> {

					BeforeEach(() -> {
						doc1 = new Document();
						doc1.setTitle("doc 1");
						doc1.setAuthor("author@email.com");
						doc1.setMimeType("image/png");
						store.setContent(doc1, this.getClass().getResourceAsStream("/image.png"));
						doc1 = repo.save(doc1);
					});

//					AfterEach(() -> {
//						store.unsetContent(doc1);
//						repo.delete(doc1);
//					});

					It("should index the documents", () -> {
						assertThat(() -> store.search("wisdom"), eventuallyEval(
									hasItem(doc1.getContentId()),
								Duration.ofSeconds(10)));
					});

					Context("rendition", () -> {
						It("rendition", () -> {
							InputStream stream = store.getRendition(doc1, "text/plain");
							int i=0;
						});
					});
				});
			});
		});
	}

	@Test
	public void noop() {
	}

	public interface DocumentRepository extends CrudRepository<Document, Long> {
		//
	}

	public interface DocumentContentStore extends ContentStore<Document, String>, Searchable<String>, Renderable<Document> {
		//
	}

	@Entity
	@NoArgsConstructor
	@Getter
	@Setter
	public static class Document {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;

		@ContentId
		private String contentId;

		@MimeType
		private String mimeType;

		private String title;
		private String author;
	}
}