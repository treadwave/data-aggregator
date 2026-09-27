package com.data_aggregator.util;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

public class ClasspathScanner {
    public <T> List<T> findImplementations(String packageName, Class<T> type) {
        List<T> implementations = new ArrayList<>();

        for (String className : findClassNames(packageName)) {
            Class<?> clazz = loadClass(className);

            if (!type.isAssignableFrom(clazz) || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }

            try {
                implementations.add(type.cast(clazz.getDeclaredConstructor().newInstance()));
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Cannot create class: " + clazz.getName(), e);
            }
        }

        return implementations;
    }

    private List<String> findClassNames(String packageName) {
        List<String> classNames = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();

                if ("file".equals(resource.getProtocol())) {
                    classNames.addAll(findClassNamesInDirectory(Path.of(resource.toURI()), packageName));
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new IllegalStateException("Cannot scan package: " + packageName, e);
        }

        classNames.sort(Comparator.naturalOrder());
        return classNames;
    }

    private List<String> findClassNamesInDirectory(Path root, String packageName) throws IOException {
        try (var paths = Files.walk(root)) {
            return paths.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".class")).filter(path -> !path.getFileName()
                .toString()
                .contains("$"))
                .map(path -> toClassName(root, path, packageName))
                .toList();
        }
    }

    private String toClassName(Path root, Path classFile, String packageName) {
        String relativeName = root.relativize(classFile).toString()
            .replace(classFile.getFileSystem().getSeparator(), ".")
            .replaceAll("\\.class$", "");

        return packageName + "." + relativeName;
    }

    private Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot load class: " + className, e);
        }
    }
}
