package net.justgnat.mcfdoc.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Annotations {

    private final HashMap<String, List<Annotation>> map;

    /**
     * A container for all Annotations in a function
     */
    public Annotations() {
        map = new HashMap<>(4);
    }

    public void add(Annotation annotation) {
        List<Annotation> annotations = map.get(annotation.getName());
        if (annotations != null) {
            annotations.add(annotation);
        } else {
            List<Annotation> list = new LinkedList<>();
            list.add(annotation);
            map.put(annotation.getName(), list);
        }
    }

    public Collection<List<Annotation>> getAnnotations() {
        return map.values();
    }

    @Override
    public String toString() {
        return getAnnotations().toString();
    }

}
