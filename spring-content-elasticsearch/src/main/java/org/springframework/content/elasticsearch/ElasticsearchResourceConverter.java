package org.springframework.content.elasticsearch;

import org.springframework.core.io.Resource;

@FunctionalInterface
public interface ElasticsearchResourceConverter {

    Resource convert(Resource resource);
}
