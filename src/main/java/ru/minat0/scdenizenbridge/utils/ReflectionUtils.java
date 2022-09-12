package ru.minat0.scdenizenbridge.utils;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import ru.minat0.scdenizenbridge.SCDenizenBridge;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Set<Path> getPathsIn(String path, Predicate<? super Path> filter) {
        Set<Path> files = new LinkedHashSet<>();
        String packagePath = path.replace(".", "/");

        try {
            URI uri = SCDenizenBridge.class.getProtectionDomain().getCodeSource().getLocation().toURI();
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

    public static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classes = new LinkedHashSet<>();

        Predicate<? super Path> filter = entry -> {
            String path = entry.getFileName().toString();
            return !path.contains("$") && path.endsWith(".class");
        };

        for (Path filesPath : getPathsIn(packageName, filter)) {
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

    @SuppressWarnings("unchecked")
    public static <T> Set<Class<? extends T>> getSubTypesOf(String packageName, Class<?> type) {
        return getClasses(packageName).stream().
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
}
