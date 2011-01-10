/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2011 Kai Reinhard (k.reinhard@me.com)
//
// ProjectForge is dual-licensed.
//
// This community edition is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as published
// by the Free Software Foundation; version 3 of the License.
//
// This community edition is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
// Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, see http://www.gnu.org/licenses/.
//
/////////////////////////////////////////////////////////////////////////////

package org.projectforge.web.wicket.converter;

import java.util.Locale;

/**
 * Format month, e. g. "01" instead of "0".
 * @author Kai Reinhard (k.reinhard@micromata.de)
 */
public class MonthConverter extends IntegerConverter
{
  private static final long serialVersionUID = -1240952683261343298L;

  public MonthConverter()
  {
    super(2);
  }

  public String convertToString(Object value, Locale locale)
  {
    if (value == null) {
      return "";
    }
    return super.convertToString(((Integer) value) + 1, locale);
  }
  
  @Override
  public Integer convertToObject(String value, Locale locale)
  {
    final Integer result = super.convertToObject(value, locale);
    return result - 1;
  }
}
