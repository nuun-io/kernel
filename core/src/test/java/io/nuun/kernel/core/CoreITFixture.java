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
package io.nuun.kernel.core;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.nuun.kernel.api.Kernel;
import io.nuun.kernel.api.Plugin;
import io.nuun.kernel.api.config.KernelConfiguration;

import static io.nuun.kernel.core.NuunCore.createKernel;
import static io.nuun.kernel.core.NuunCore.newKernelConfiguration;

/**
 * @author epo.jemba{@literal @}kametic.com
 */
public class CoreITFixture extends AbstractFixture<Kernel> {

	private Kernel kernel = null;

	private String[] kernelParams = new String[0];
	private Module[] modules = new Module[0];
	private Plugin[] plugins = new Plugin[0];
    boolean spiActivated = true;

	public CoreITFixture() {
	}

	@Override
	protected Kernel createUnitUnderTest() {
        
		if (kernel == null) {
			
			KernelConfiguration configuration = newKernelConfiguration()
					.params(kernelParams)
					.withoutSpiPluginsLoader()
					.plugins(plugins)
					.plugins(createInternalPluginFromModules());
			
	        kernel = createKernel(configuration);
	        
	        if(!spiActivated) {
                configuration.withoutSpiPluginsLoader();
            }
			///////////////
			
			kernel.init();
			kernel.start();
		}

		return kernel;
	}

	static class InternalModule extends AbstractModule {
		private Module[] internalModules;

		public InternalModule(Module[] modules) {
			internalModules = modules;
		}

		@Override
		protected void configure() {
			if (internalModules != null) {
				for (Module m : internalModules) {
					install(m);
				}
			}
		}
	}

	public static class InternalPlugin extends AbstractPlugin {
		private Module module;

		public InternalPlugin(Module module) {
			this.module = module;
		}

		@Override
		public String name() {
			return "fixture-internal-plugin";
		}

		@Override
		public Object nativeUnitModule() {
			return module;
		}
	}

	private Plugin[] createInternalPluginFromModules() {
		Plugin [] plugins = new Plugin[1];
		
		if (modules != null && modules.length > 0) {
			InternalPlugin plugin = new InternalPlugin(new InternalModule(modules));
			plugins[0] = plugin;
			
			return plugins;
		}
		
		return new Plugin[0];
	}

	@Override
	protected void after() {
		kernel.stop();
		super.after();
	}

	public static Builder createCoreFixture() {
		return new Builder();
	}

	public static class Builder {
		private String[] params = new String[0];
		private Module[] modules = new Module[0];
		private Plugin[] plugins = new Plugin[0];
		boolean spiActivated = true;

		
		public Builder withoutSpi()
		{
		    spiActivated = false;
		    return this;
		}
		
		public Builder withPlugins(Plugin... plugins)
		{
		    this.plugins = plugins;
		    return this;
		}
		
		public Builder withKernelParameters(String... params) {
			this.params = params;
			return this;
		}

		public Builder withModule(Module... modules) {
			this.modules = modules;
			return this;
		}

		public CoreITFixture build() {
			CoreITFixture cf = new CoreITFixture();
			
			if (params != null) {
				cf.kernelParams = params;
			}
			
			if (modules != null) {
				cf.modules = modules;
			}
			
			if (plugins != null) {
				cf.plugins = plugins;
			}
			
			cf.spiActivated = spiActivated;

			return cf;
		}
	}
}
