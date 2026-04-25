package net.justgnat.mcfdoc.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TagGroup {

    /*
    The key is the name of the tag type, and the value is a list of all tags given of that type.
     */
    private final HashMap<String, List<Tag>> map;

    public TagGroup() {
        map = new HashMap<>(4);
    }

    public void add(Tag tag) {
        List<Tag> tags = map.get(tag.getName());
        if (tags != null) {
            tags.add(tag);
        } else {
            List<Tag> list = new LinkedList<>();
            list.add(tag);
            map.put(tag.getName(), list);
        }
    }

    public Collection<List<Tag>> getTags() {
        return map.values();
    }

    @Override
    public String toString() {
        return getTags().toString();
    }

}
