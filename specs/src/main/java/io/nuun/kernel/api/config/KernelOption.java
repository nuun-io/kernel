/**
 * This file is part of Nuun IO Kernel Specs.
 *
 * Nuun IO Kernel Specs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nuun IO Kernel Specs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Nuun IO Kernel Specs.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.nuun.kernel.api.config;


import javax.annotation.Nullable;

public class KernelOption<T>
{
    private String name;
    private T value;

    public KernelOption(String name) {
        this.name = name;
    }

    public KernelOption(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public KernelOption(KernelOption<T> option, @Nullable T value) {
        this.name = option.name;
        this.value = value == null ? option.getValue() : value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}
