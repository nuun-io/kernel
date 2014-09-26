/**
 * Copyright (C) 2014 Kametic <epo.jemba@kametic.com> Licensed under the GNU LESSER GENERAL PUBLIC LICENSE,
 * Version 3, 29 June 2007; or any later version you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.gnu.org/licenses/lgpl-3.0.txt Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */
package io.nuun.kernel.api.plugin.request.annotations;

import static org.kametic.specifications.Specifications.and;
import static org.kametic.specifications.Specifications.or;

import org.kametic.specifications.FalseSpecification;
import org.kametic.specifications.TrueSpecification;

public enum Specs implements Payload
{
   kernel_param1("iamparam1"),

   interface_services( //
         and(new TrueSpecification(), new FalseSpecification()) //
   ), //

   implem_services( //
         or(new TrueSpecification(), new FalseSpecification()) //
   ); //

   private Object payload;

   private Specs(Object payload)
   {
      this.payload = payload;
   }

   @Override
   public Object payload()
   {
      return payload;
   }

}
