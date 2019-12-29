package hr.fer.zemris.projekt.gui.util;

/**
 * Utility class used for resource loading.
 */
public class ResourceLoader {

    /**
     * Loads resource from provided <i>path</i> in regards to provided class <i>c</i>.
     *
     * @param c    class c.
     * @param path relative or absolute path of some resource.
     * @return system path of provided <i>path</i>.
     */
    public static String loadResource(Class<?> c, String path) {
        return c.getResource(path).toExternalForm();
    }

}
