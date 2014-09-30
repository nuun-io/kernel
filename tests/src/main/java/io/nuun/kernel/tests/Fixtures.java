package io.nuun.kernel.tests;

import io.nuun.kernel.tests.ut.fixtures.FixtureConfiguration;
import io.nuun.kernel.tests.ut.fixtures.GivenWhenThenInternal;

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
