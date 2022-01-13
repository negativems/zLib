package net.zargum.zlib.utils;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.*;

public final class GenericUtils {

    private GenericUtils() {
    }

    public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> clazz, String value) {
        Preconditions.checkNotNull((Object) clazz);
        if (value == null || value.isEmpty()) return Optional.empty();

        try {
            return Optional.of(Enum.valueOf(clazz, value));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
        return first != null ? first : Preconditions.checkNotNull(second);
    }

    public static <E> List<E> createList(Object object, Class<E> type, boolean ignoreNulls) {
        List<E> result = new ArrayList<>();

        if (object != null && object instanceof List) {
            for (Object value : (List<?>) object) {
                if (!ignoreNulls && value == null) {
                    result.add(null);
                    continue;
                }

                if (value != null) {
                    Class<?> clazz = value.getClass();
                    if (clazz != null) {
                        if (!type.isAssignableFrom(clazz)) throw new AssertionError("Cannot cast to list! Key " + value + " is not a " + type.getSimpleName());

                        result.add(type.cast(value));
                    }
                }
            }
        }

        return result;
    }

    public static <E> List<E> createList(Object object, Class<E> type) {
        return createList(object, type, true);
    }

    public static <E> Set<E> castSet(Object object, Class<E> type) {
        Set<E> result = new HashSet<>();

        if (object != null && object instanceof List) {
            for (Object value : (List<?>) object) {
                if (value != null) {
                    if (value.getClass() == null) continue;

                    if (!type.isAssignableFrom(value.getClass())) {
                        String simpleName = type.getSimpleName();
                        throw new AssertionError("Cannot cast to list! Key " + value + " is not a " + simpleName);
                    }

                    result.add(type.cast(value));
                }
            }
        }

        return result;
    }

    public static <K, V> Map<K, V> castMap(Object object, Class<K> keyClass, Class<V> valueClass) {
        Map<K, V> result = new HashMap<>();

        if (object != null && object instanceof Map) {
            Map<?, ?> input = (Map<?, ?>) object;
            String keyClassName = keyClass.getSimpleName();
            String valueClassName = valueClass.getSimpleName();

            for (Object key : input.keySet().toArray()) {
                if (key != null && !keyClass.isAssignableFrom(key.getClass())) throw new AssertionError("Cannot cast to HashMap: " + keyClassName + ", " + keyClassName + ". Value " + valueClassName + " is not a " + keyClassName);

                Object value = input.get(key);
                if (value != null && !valueClass.isAssignableFrom(value.getClass())) throw new AssertionError("Cannot cast to HashMap: " + valueClassName + ", " + valueClassName + ". Key " + key + " is not a " + valueClassName);

                result.put(keyClass.cast(key), valueClass.cast(value));
            }
        }

        return result;
    }
}