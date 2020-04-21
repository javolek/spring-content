package internal.org.springframework.content.commons.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.content.commons.metadata.ContentMetadataService;
import org.springframework.content.commons.utils.BeanUtils;

public class ContentMetadataServiceImpl implements ContentMetadataService {

    private Map<Class<?>, Map<Class<? extends Annotation>, Object>> cache = new HashMap<>();


    @Override
    public Object get(Object entity, Class<? extends Annotation> annotation) {

        Field field = null;

//        Map<Class<? extends Annotation>, Object> info = cache.get(entity.getClass());
//        if (info != null) {
//            field = (Field) info.get(annotation);
//        }

        if (field == null) {
            field = BeanUtils.findFieldWithAnnotation(entity, annotation);

//            info = cache.get(entity.getClass());
//            if (info == null) {
//                info = new HashMap<Class<? extends Annotation>, Object>();
//                cache.put(entity.getClass(), info);
//            }
//            info.put(annotation, field);
        }

        return BeanUtils.getValue(entity, field);
    }
}
