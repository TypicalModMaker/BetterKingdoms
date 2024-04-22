package dev.isnow.betterkingdoms.reflection;

import com.google.common.collect.ImmutableSet;
import dev.isnow.betterkingdoms.BetterKingdoms;
import io.ebean.annotation.Platform;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@UtilityClass
public class ClassRegistrationManager {

    public void loadListeners(String packageName) {
        for (Class<?> clazz : getClassesInPackage(packageName)) {
            if (isListener(clazz)) {
                try {
                    Bukkit.getPluginManager().registerEvents((Listener) clazz.newInstance(), BetterKingdoms.getInstance());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    private boolean isListener(Class<?> clazz) {
        for (Class<?> interfaze : clazz.getInterfaces()) {
            if (interfaze == Listener.class) {
                return true;
            }
        }


        return false;
    }

    private Collection<Class<?>> getClassesInPackage(String packageName) {
        JarFile jarFile;
        Collection<Class<?>> classes = new ArrayList<>();
        CodeSource codeSource = BetterKingdoms.getInstance().getClass().getProtectionDomain().getCodeSource();
        URL resource = codeSource.getLocation();

        String relPath = packageName.replace('.', '/');
        String resPath = resource.getPath().replace("%20", " ");
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");

        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }

        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length()) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className != null) {
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }

        try {
            jarFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ImmutableSet.copyOf(classes);
    }
}