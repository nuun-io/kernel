package io.nuun.kernel.tests;

import io.nuun.kernel.tests.internal.dsl.GivenWhenThenInternal;
import io.nuun.kernel.tests.ut.fixture.FixtureConfiguration;

/**
 * 
 * 
 * @author epo.jemba{@literal @}kametic.com
 * @author pierre.thirouin{@literal @}gmail.com
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
