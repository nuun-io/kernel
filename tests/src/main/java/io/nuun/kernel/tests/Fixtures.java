package io.nuun.kernel.tests;

import io.nuun.kernel.tests.ut.fixtures.GivenWhenThenInternal;
import io.nuun.kernel.tests.ut.fixtures.dslparts.FixtureConfiguration;

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
