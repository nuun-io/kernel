package io.nuun.kernel.core.internal;

import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.core.internal.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PluginSortStrategy {

    public List<Plugin> sortPlugins(List<Plugin> unsortedPlugins) {
        Graph graph = new Graph(unsortedPlugins.size());
        ArrayList<Plugin> sorted = new ArrayList<Plugin>();
        Map<Integer, Plugin> idxPlug = new HashMap<Integer, Plugin>();
        Map<Character, Plugin> charPlug = new HashMap<Character, Plugin>();
        Map<Plugin, Integer> plugIdx = new HashMap<Plugin, Integer>();
        Map<Class<? extends Plugin>, Integer> classPlugIdx = new HashMap<Class<? extends Plugin>, Integer>();

        // Add vertices
        for (short i = 0; i < unsortedPlugins.size(); i++) {
            char c = (char) i;
            Plugin unsortedPlugin = unsortedPlugins.get(i);
            Integer pluginIndex = graph.addVertex(c);

            charPlug.put(c, unsortedPlugin);
            idxPlug.put(pluginIndex, unsortedPlugin);
            plugIdx.put(unsortedPlugin, pluginIndex);
            classPlugIdx.put(unsortedPlugin.getClass(), pluginIndex);
        }

        // add edges
        for (Map.Entry<Integer, Plugin> entry : idxPlug.entrySet()) {
            Plugin source = entry.getValue();
            // based on required plugins
            for (Class<? extends Plugin> dependencyClass : source.requiredPlugins()) {
                int start = classPlugIdx.get(dependencyClass);
                int end = plugIdx.get(source);
                graph.addEdge(start, end);
            }
            // based on dependent plugins
            for (Class<? extends Plugin> dependencyClass : source.dependentPlugins()) {
                int start = plugIdx.get(source); // we inversed
                int end = classPlugIdx.get(dependencyClass);
                graph.addEdge(start, end);
            }
        }

        // launch the algo
        char[] topologicalSort = graph.topologicalSort();

        if (topologicalSort != null) {
            for (Character c : topologicalSort) {
                sorted.add(charPlug.get(c));
            }
        } else {
            throw new KernelException("Error when sorting plugins: either a Cycle in dependencies or another cause.");
        }

        return sorted;
    }
}