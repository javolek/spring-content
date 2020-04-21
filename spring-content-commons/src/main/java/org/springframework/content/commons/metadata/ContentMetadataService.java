package org.springframework.content.commons.metadata;

import java.lang.annotation.Annotation;

public interface ContentMetadataService {

    Object get(Object entity, Class<? extends Annotation> annotation);

}
