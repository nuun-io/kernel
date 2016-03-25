/**
 * This file is part of Nuun IO Kernel Core.
 *
 * Nuun IO Kernel Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
    private final List<Plugin> plugins;
    private Graph graph;
    private Vertices vertices;

    public PluginSortStrategy(FacetRegistry facetRegistry, List<Plugin> unOrderedPlugins) {
        this.facetRegistry = facetRegistry;
        this.plugins = unOrderedPlugins;
        this.graph = new Graph(plugins.size());
        this.vertices = new Vertices();
    }

    public List<Plugin> sortPlugins() {
        addVertices();
        addEdges();
        return sortGraph();
    }

    private void addVertices() {
        for (short i = 0; i < plugins.size(); i++) {
            char label = (char) i;
            vertices.addVertex(label, graph.addVertex(label), plugins.get(i));
        }
    }

    private void addEdges() {
        for (Plugin source : plugins) {
            addEdgesForRequiredPlugins(source);
            addEdgesForDependentPlugins(source);
        }
    }

    private ArrayList<Plugin> sortGraph() {
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

    private void addEdgesForRequiredPlugins(Plugin source) {
        for (Class<?> requiredClass : source.requiredPlugins()) {
            for (Class<?> dependencyClass : getCompleteDependencies(requiredClass)) {
                graph.addEdge(vertices.getIndex(dependencyClass), vertices.getIndex(source.getClass()));
            }
        }
    }

    private void addEdgesForDependentPlugins(Plugin source) {
        for (Class<?> dependentClass : source.dependentPlugins()) {
            for (Class<?> dependencyClass : getCompleteDependencies(dependentClass)) {
                graph.addEdge(vertices.getIndex(source.getClass()), vertices.getIndex(dependencyClass)); // we inverse
            }
        }
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