package io.nuun.kernel.tests;

import io.nuun.kernel.tests.internal.dsl.GivenWhenThenInternal;
import io.nuun.kernel.tests.ut.dsl.fixture.FixtureConfiguration;

/**
 * 
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class Fixtures
{
    
    private Fixtures()
    {
    }
    
    public static FixtureConfiguration newGivenWhenThenFixture()
    {
        return new GivenWhenThenInternal();
    }
}
