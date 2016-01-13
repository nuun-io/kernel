package io.nuun.kernel.core.internal;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * @author Pierre THIROUIN (pierre.thirouin@ext.inetpsa.com)
 */
public class KernelConfigurationTest {

    private KernelConfigurationInternal underTest = new KernelConfigurationInternal();

    @Test
    public void testEmptyParamsNotNull() throws Exception {
        AliasMap params = underTest.kernelParams();
        assertThat(params).isNotNull();
    }

    @Test
    public void testNullParams() throws Exception {
        underTest.param("key1", null);
        underTest.param("key2", null);
        AliasMap params = underTest.kernelParams();
        assertThat(params.get("key1")).isNull();
        assertThat(params.get("key2")).isNull();
    }

    @Test
    public void testMissingParamValue() throws Exception {
        try {
            underTest.params("key1");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e).hasMessage("An even number of parameters was expected but found: " + Lists.newArrayList("key1").toString());
        }
    }

    @Test
    public void testParams() throws Exception {
        underTest.params("key1", "val1", "key2", "val2");
        AliasMap params = underTest.kernelParams();
        assertThat(params.get("key1")).isEqualTo("val1");
        assertThat(params.get("key2")).isEqualTo("val2");
    }

    @Test
    public void testParam() throws Exception {
        underTest.param("key1", "val1");
        underTest.param( "key2", "val2");
        AliasMap params = underTest.kernelParams();
        assertThat(params.get("key1")).isEqualTo("val1");
        assertThat(params.get("key2")).isEqualTo("val2");
    }

    @Test
    public void testParamAreNotReset() throws Exception {
        underTest.param("key1", "val1");
        AliasMap params = underTest.kernelParams();
        assertThat(params.get("key1")).isEqualTo("val1");

        underTest.param("key2", "val2");
        underTest.kernelParams();
        assertThat(params.get("key1")).isEqualTo("val1");
        assertThat(params.get("key2")).isEqualTo("val2");
    }
}
