package net.bramp.ffmpeg.modelmapper;

import org.apache.commons.lang3.math.Fraction;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.util.*;
import java.util.function.Supplier;

public class MapperTestUtils {
    private static final Map<Class<?>, Supplier<?>> PRIMITIVE_TYPES_WITH_VALUES = new HashMap<>() {{
        this.put(Integer.class, () -> 42);
        this.put(int.class, () -> 42);
        this.put(Long.class, () -> 42L);
        this.put(long.class, () -> 42L);
        this.put(Float.class, () -> 42f);
        this.put(float.class, () -> 42f);
        this.put(Short.class, () -> (short) 42);
        this.put(short.class, () -> (short) 42);
        this.put(Character.class, () -> (char) 42);
        this.put(char.class, () -> (char) 42);
        this.put(Double.class, () -> 42d);
        this.put(double.class, () -> 42d);
        this.put(Boolean.class, () -> true);
        this.put(boolean.class, () -> true);
    }};

    public static Object getDefaultValue(Class<?> type) {
        if (type.isPrimitive()) {
            if (type.equals(boolean.class)) {
                return false;
            }

            return 0;
        } else {
            return null;
        }
    }

    public static Object getSpecialValue(Class<?> type) {
        if (type.equals(boolean.class)) {
            return true;
        }

        if (type.equals(String.class)) {
            return "field under test";
        }

        for (Map.Entry<Class<?>, Supplier<?>> primitiveClass : PRIMITIVE_TYPES_WITH_VALUES.entrySet()) {
            if (primitiveClass.getKey().equals(type)) {
                return primitiveClass.getValue().get();
            }
        }

        if (Enum.class.isAssignableFrom(type)) {
            return type.getEnumConstants()[0];
        }

        if (type.equals(Fraction.class)) {
            return Fraction.ONE;
        }

        if (type.equals(URI.class)) {
            return URI.create("https://github.com/bramp/ffmpeg-cli-wrapper");
        }

        if (List.class.isAssignableFrom(type)) {
            return Collections.emptyList();
        }

        throw new IllegalArgumentException("Unsupported default value for class " + type);
    }

    public static <T> T instantiateObjectWithDefaultValues(Class<T> target, Parameter...nonDefaultFields) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<T> constructor = (Constructor<T>) target.getConstructors()[0];
        Parameter[] parameters = constructor.getParameters();
        Object[] parameterValues = new Object[parameters.length];

        for(int i = 0; i < parameters.length; i++) {
            final Parameter parameter = parameters[i];

            if (Arrays.stream(nonDefaultFields).noneMatch(p -> p.equals(parameter))) {
                parameterValues[i] = MapperTestUtils.getDefaultValue(parameter.getType());
            } else {
                parameterValues[i] = MapperTestUtils.getSpecialValue(parameter.getType());
            }
        }

        return constructor.newInstance(parameterValues);
    }
}
