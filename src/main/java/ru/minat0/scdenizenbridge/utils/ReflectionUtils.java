package ru.minat0.scdenizenbridge.utils;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import ru.minat0.scdenizenbridge.SCDenizenBridge;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    @SuppressWarnings("resource")
    public static Set<Path> getPathsIn(Class<?> clazz, String path, Predicate<? super Path> filter) {
        Set<Path> files = new LinkedHashSet<>();
        String packagePath = path.replace(".", "/");

        try {
            URI uri = clazz.getProtectionDomain().getCodeSource().getLocation().toURI();
            FileSystem fileSystem = FileSystems.newFileSystem(URI.create("jar:" + uri), Collections.emptyMap());
            files = Files.walk(fileSystem.getPath(packagePath)).
                    filter(Objects::nonNull).
                    filter(filter).
                    collect(Collectors.toSet());
            fileSystem.close();
        } catch (URISyntaxException | IOException ex) {
            SimpleClans.getInstance().getLogger().
                    log(Level.WARNING, "An error occurred while trying to load files: " + ex.getMessage(), ex);
        }

        return files;
    }

    public static Set<Class<?>> getClasses(Class<?> aClass, String packageName) {
        Set<Class<?>> classes = new LinkedHashSet<>();

        Predicate<? super Path> filter = entry -> {
            String path = entry.getFileName().toString();
            return !path.contains("$") && path.endsWith(".class");
        };

        for (Path filesPath : getPathsIn(aClass, packageName, filter)) {
            // Compatibility with different Java versions
            String path = filesPath.toString();
            if (path.charAt(0) == '/') {
                path = path.substring(1);
            }

            String fileName = path.replace("/", ".").split(".class")[0];

            try {
                Class<?> clazz = Class.forName(fileName);
                classes.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return classes;
    }

    public static <T> Set<Class<? extends T>> getSubTypesOf(String packageName, Class<?> type) {
        return getSubTypesOf(SCDenizenBridge.class, packageName, type);
    }

    @SuppressWarnings("unchecked")
    public static <T> Set<Class<? extends T>> getSubTypesOf(Class<?> clazz, String packageName, Class<?> type) {
        return getClasses(clazz, packageName).stream().
                filter(type::isAssignableFrom).
                map(aClass -> ((Class<? extends T>) aClass)).
                collect(Collectors.toSet());
    }

    public static <T> void instantiate(String packageName, Class<T> type, Consumer<T> consumer) {
        Set<Class<?>> classSet = ReflectionUtils.getSubTypesOf(packageName, type);
        for (Class<?> clazz : classSet) {
            try {
                T instance = (T) clazz.getConstructor().newInstance();
                consumer.accept(instance);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException ex) {
                SCDenizenBridge.getInstance().getLogger().severe("Class registration failure: " + ex.getMessage());
            }
        }
    }

    public static <T> T getStaticFieldValue(Class<?> clazz, String field, T obj) {
        try {
            Field declaredField = clazz.getDeclaredField(field);
            declaredField.setAccessible(true);
            return (T) declaredField.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
