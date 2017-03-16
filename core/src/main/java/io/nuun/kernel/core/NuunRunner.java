package io.nuun.kernel.core;

import javax.inject.Singleton;

public class NuunRunner {
	
	
	
	private static Class<?> entrypointClass;

	public static NuunRunnerDsl entrypoint(Class<?> entrypointClass) {
		NuunRunner.entrypointClass = entrypointClass;
		
		return new NuunRunnerDsl(entrypointClass);
	}
	
	public static class NuunRunnerDsl {
	    
	    private Class<?> entrypointClass;

        public NuunRunnerDsl(Class<?> entrypointClass) {
            this.entrypointClass = entrypointClass;
	    }
	    
		public void execute (String[] args) {
		    
		}
	}
}

/* 
*/