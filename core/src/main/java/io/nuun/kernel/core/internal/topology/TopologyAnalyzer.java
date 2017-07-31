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
package io.nuun.kernel.core.internal.topology;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import io.nuun.kernel.api.annotations.Topology;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.spi.topology.Binding;
import io.nuun.kernel.spi.topology.InterceptorBinding;
import io.nuun.kernel.spi.topology.LinkedBinding;
import io.nuun.kernel.spi.topology.ProviderBinding;
import io.nuun.kernel.spi.topology.TopologyDefinition;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

class TopologyAnalyzer
{
    private static final String CLASSPATH = "classpath:";

    private static final Logger logger    = LoggerFactory.getLogger(TopologyAnalyzer.class);

    private List<Binding>       bindings;
    private TopologyDefinition  topologyDefinition;

    public TopologyAnalyzer(TopologyDefinition topologyDefinition, List<Binding> bindings)
    {
        this.topologyDefinition = topologyDefinition;
        this.bindings = bindings;
    }

    public void analyze(Collection<Class<?>> topologiesClasses)
    {
        // Analyze Fields
        topologiesClasses.stream().peek(this::treatUnit).forEach(c -> asList(c.getDeclaredFields()).stream().forEach(this::treatMember));

        // Analyze Methods
        topologiesClasses.stream().forEach(c -> asList(c.getDeclaredMethods()).stream().forEach(this::treatMember));

    }

    void treatUnit(Class<?> unit)
    {
        if (!unit.isInterface())
        {
            throw new KernelException("Topology : %s must be an interface to be a valid topology.", unit.getName());
        }

        Topology topology = unit.getAnnotation(Topology.class);
        String[] propertySources = topology.configurationFiles();

        stream(propertySources).filter(Objects::nonNull).filter(this::nonEmpty).forEach(this::treatPropertySource);

    }

    private boolean nonEmpty(String candidate)
    {
        return candidate.length() > 0;
    }

    private void treatPropertySource(String propertySource)
    {
        try (final InputStream stream = inputStream(propertySource))
        {
            Properties properties = new Properties();
            properties.load(stream);
            properties.forEach(this::propertyToBinding);
        }
        catch (IOException e)
        {
            logger.warn("Error when reading properties file " + propertySource, e);
        }

    }

    InputStream inputStream(String propertySource) throws FileNotFoundException
    {
        InputStream is = null;

        if (propertySource.startsWith(CLASSPATH))
        {
            String path = propertySource.substring(CLASSPATH.length());
            is = this.getClass().getClassLoader().getResourceAsStream(path);
        }
        else
        {
            is = new FileInputStream(new File(propertySource));
        }

        return is;
    }

    void propertyToBinding(Object key, Object value)
    {
        bindings.add(new LinkedBinding(TypeLiteral.get(String.class), Names.named((String) key), value));
    }

    void treatMember(Member m)
    {
        // Instance Binding
        Optional<Binding> instanceBinding = topologyDefinition.instanceBinding(m);
        if (instanceBinding.isPresent())
        {
            bindings.add(instanceBinding.get());
        }

        // Linked Binding
        Optional<LinkedBinding> linkedBinding = topologyDefinition.linkedBinding(m);
        if (linkedBinding.isPresent())
        {
            bindings.add(linkedBinding.get());
        }

        // Provider Binding
        Optional<ProviderBinding> providerBinding = topologyDefinition.providerBinding(m);
        if (providerBinding.isPresent())
        {
            bindings.add(providerBinding.get());
        }

        // Interceptor Binding
        Optional<InterceptorBinding> interceptorBinding = topologyDefinition.interceptorBinding(m);
        if (interceptorBinding.isPresent())
        {
            bindings.add(interceptorBinding.get());
        }
    }

    boolean isNullable(Member member)
    {
        return false;
    }

}
