#!/bin/sh
#
# This file is part of Nuun IO Kernel.
#
# Nuun IO Kernel is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Nuun IO Kernel is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with Nuun IO Kernel.  If not, see <http://www.gnu.org/licenses/>.
#


(
    mvn javadoc:aggregate
	cd target/site/apidocs
	git init
	git config user.name "Travis-CI"
	git config user.email "travis@seedstack.org"
	git add .
	git commit -m "Built for gh-pages of http://nuun-io.github.io/kernel"
	git push --force-with-lease --quiet "https://${GITHUB_TOKEN}@github.com/nuun-io/kernel" master:gh-pages > /dev/null 2>&1
)
