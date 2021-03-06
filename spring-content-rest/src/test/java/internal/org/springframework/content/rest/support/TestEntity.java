package internal.org.springframework.content.rest.support;

import lombok.Getter;
import lombok.Setter;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.content.commons.annotations.OriginalFileName;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
public class TestEntity implements ContentEntity {
	private @Id @GeneratedValue Long id;
	private String name;
	private @ContentId UUID contentId;
	private @ContentLength Long len;
	private @MimeType String mimeType;
	private @OriginalFileName String originalFileName;
}
