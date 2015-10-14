package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class provides a strategy for sorting plugins regarding their dependencies.
 */
class PluginSortStrategy {

    private final FacetRegistry facetRegistry;

    public PluginSortStrategy(FacetRegistry facetRegistry) {
        this.facetRegistry = facetRegistry;
    }

    public List<Plugin> sortPlugins(List<Plugin> unsortedPlugins) {
        Graph graph = new Graph(unsortedPlugins.size());
        Vertices vertices = new Vertices();

        // Add vertices
        for (short i = 0; i < unsortedPlugins.size(); i++) {
            char label = (char) i;
            vertices.addVertex(label, graph.addVertex(label), unsortedPlugins.get(i));
        }

        // add edges
        for (Plugin source : unsortedPlugins) {
            // based on required plugins
            for (Class<?> requiredClass : source.requiredPlugins()) {
                for (Class<?> dependencyClass : getCompleteDependencies(requiredClass)) {
                    graph.addEdge(vertices.getIndex(dependencyClass), vertices.getIndex(source.getClass()));
                }
            }
            // based on dependent plugins
            for (Class<?> dependentClass : source.dependentPlugins()) {
                for (Class<?> dependencyClass : getCompleteDependencies(dependentClass)) {
                    graph.addEdge(vertices.getIndex(source.getClass()), vertices.getIndex(dependencyClass)); // we inverse
                }
            }
        }

        // launch the algo
        char[] topologicalSort = graph.topologicalSort();
        if (topologicalSort == null) {
            throw new KernelException("Error when sorting plugins: either a Cycle in dependencies or another cause.");
        }

        ArrayList<Plugin> sorted = new ArrayList<Plugin>();
        for (Character label : topologicalSort) {
            sorted.add(vertices.getPlugin(label));
        }

        return sorted;
    }

    private List<Class<?>> getCompleteDependencies(Class<?> declaredDependency) {
        final List<Class<?>> dependencies = new ArrayList<Class<?>>();
        if (!Plugin.class.isAssignableFrom(declaredDependency)) {
            List<?> facets = facetRegistry.getFacets(declaredDependency);
            // TODO remove the need for this loop. Add a getFacetClasses in the FacetRegistry.
            for (Object facet : facets) {
                dependencies.add(facet.getClass());
            }
        } else {
            dependencies.add(declaredDependency);
        }
        return dependencies;
    }

    private static class Vertices {
        Map<Integer, Plugin> pluginsByIndex = new HashMap<Integer, Plugin>();
        Map<Character, Plugin> pluginsByLabel = new HashMap<Character, Plugin>();
        Map<Class<?>, Integer> indexByPluginClasses = new HashMap<Class<?>, Integer>();

        public void addVertex(char label, int index, Plugin plugin) {
            pluginsByLabel.put(label, plugin);
            pluginsByIndex.put(index, plugin);
            indexByPluginClasses.put(plugin.getClass(), index);
        }

        public int getIndex(Class<?> pluginClass) {
            return indexByPluginClasses.get(pluginClass);
        }

        public Plugin getPlugin(char label) {
            return pluginsByLabel.get(label);
        }
    }
}