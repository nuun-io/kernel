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

import static org.assertj.core.api.Assertions.assertThat;
import io.nuun.kernel.api.annotations.Topology;
import io.nuun.kernel.core.KernelException;
import io.nuun.kernel.spi.topology.Binding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class TopologyAnalyzerTest
{

    TopologyAnalyzer underTest;

    @Before
    public void init()
    {
        List<Binding> bindings = new ArrayList<>();

        underTest = new TopologyAnalyzer(new TopologyDefinitionCore(), bindings);
    }

    @Topology
    static class Topo01
    {
        void bad(String zob)
        {
        }
    }

    @Test
    public void testTreatMember()
    {
        try
        {
            underTest.analyze(Arrays.asList(Topo01.class));

            Assertions.failBecauseExceptionWasNotThrown(KernelException.class);
        }
        catch (KernelException exception)
        {
            assertThat(exception.getMessage()).contains("must be an interface");
        }
        catch (Exception e)
        {
            Assertions.fail("KernelException was expected", e);
        }

    }

}
