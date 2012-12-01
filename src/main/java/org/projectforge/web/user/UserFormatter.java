/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2012 Kai Reinhard (k.reinhard@micromata.de)
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

package org.projectforge.web.user;

import org.projectforge.user.PFUserDO;
import org.projectforge.user.UserGroupCache;
import org.projectforge.web.HtmlHelper;

public class UserFormatter
{
  private UserGroupCache userGroupCache;

  public void setUserGroupCache(UserGroupCache userGroupCache)
  {
    this.userGroupCache = userGroupCache;
  }

  /**
   * Does not escape characters.
   * @param user (must not be initialized, the user will be get from the userGroupCache)
   * @return User's full name.
   * @see PFUserDO#getFullname()
   */
  public String formatUser(final PFUserDO user)
  {
    if (user == null) {
      return "";
    }
    return formatUser(user.getId());
  }

  /**
   * Does not escape characters.
   * @param userId
   * @return User's full name.
   * @see PFUserDO#getFullname()
   */
  public String formatUser(final Integer userId)
  {
    PFUserDO u = userGroupCache.getUser(userId);
    return u != null ? u.getFullname() : "";
  }

  /**
   * Escapes xml characters.
   */
  public String getFormattedUser(PFUserDO user)
  {
    if (user == null) {
      return "";
    }
    return HtmlHelper.escapeXml(user.getFullname());
  }

  public String getFormattedUser(Integer userId)
  {
    if (userId == null) {
      return "";
    }
    PFUserDO user = userGroupCache.getUser(userId);
    return getFormattedUser(user);
  }

  public void appendFormattedUser(StringBuffer buf, Integer userId)
  {
    PFUserDO user = userGroupCache.getUser(userId);
    appendFormattedUser(buf, user);
  }

  public void appendFormattedUser(StringBuffer buf, PFUserDO user)
  {
    buf.append(getFormattedUser(user));
  }
}
