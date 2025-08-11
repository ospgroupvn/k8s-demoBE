package com.osp.bttp.common.utils;

import com.osp.bttp.common.annotation.Require;
import com.osp.bttp.common.exception.InternalException;
import com.osp.bttp.dao.model.dto.db3.CreateOrUpdateAuctioneerRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtil {

    public static <T> List<String> verifyCondition(T object, Class<? extends Annotation> annotation) throws InternalException {
        List<String> invalidFields = new ArrayList<>();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                try {
                    field.setAccessible(true); // Allow access to private fields
                    Object value = field.get(object);

                    if (ObjectUtils.isEmpty(value)) {
                        invalidFields.add(field.getName());
                    }
                } catch (IllegalAccessException e) {

                    throw new InternalException("Error accessing field: " + field.getName(), e);
                }
            }
        }

        return invalidFields;
    }

    public static List<String> validateBasicInfoOfAuctioneer(CreateOrUpdateAuctioneerRequest request) throws InternalException {
        return ValidationUtil.verifyCondition(request, Require.class);
    }
}
