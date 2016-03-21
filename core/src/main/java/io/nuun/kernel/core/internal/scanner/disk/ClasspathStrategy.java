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
package io.nuun.kernel.core.internal.scanner.disk;

public class ClasspathStrategy {
    private final Strategy strategy;
    private final boolean additional;

    public ClasspathStrategy() {
        this.strategy = Strategy.ALL;
        this.additional = true;
    }

    public ClasspathStrategy(String strategy, boolean additional) {
        if (strategy == null) {
            this.strategy = Strategy.ALL;
        }
        else {
            this.strategy = Strategy.valueOf(strategy.toUpperCase());
        }
        this.additional = additional;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public boolean isAdditional() {
        return additional;
    }

    public enum Strategy {
        ALL,
        SYSTEM,
        CLASSLOADER,
        NONE
    }
}
