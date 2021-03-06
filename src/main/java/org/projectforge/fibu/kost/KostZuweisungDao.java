/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2014 Kai Reinhard (k.reinhard@micromata.de)
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

package org.projectforge.fibu.kost;

import org.projectforge.access.OperationType;
import org.projectforge.core.BaseDao;
import org.projectforge.user.PFUserDO;
import org.projectforge.user.ProjectForgeGroup;

public class KostZuweisungDao extends BaseDao<KostZuweisungDO>
{
  private Kost1Dao kost1Dao;

  private Kost2Dao kost2Dao;

  public KostZuweisungDao()
  {
    super(KostZuweisungDO.class);
  }

  /**
   * User must member of group finance or controlling.
   * @see org.projectforge.core.BaseDao#hasSelectAccess()
   */
  @Override
  public boolean hasSelectAccess(final PFUserDO user, final boolean throwException)
  {
    return accessChecker.isUserMemberOfGroup(user, throwException, ProjectForgeGroup.FINANCE_GROUP, ProjectForgeGroup.CONTROLLING_GROUP);
  }

  /**
   * @see org.projectforge.core.BaseDao#hasSelectAccess(PFUserDO, org.projectforge.core.ExtendedBaseDO, boolean)
   * @see #hasSelectAccess(PFUserDO, boolean)
   */
  @Override
  public boolean hasSelectAccess(final PFUserDO user, final KostZuweisungDO obj, final boolean throwException)
  {
    return hasSelectAccess(user, throwException);
  }

  /**
   * User must member of group finance.
   * @see org.projectforge.core.BaseDao#hasAccess(Object, OperationType)
   */
  @Override
  public boolean hasAccess(final PFUserDO user, final KostZuweisungDO obj, final KostZuweisungDO oldObj, final OperationType operationType,
      final boolean throwException)
  {
    return accessChecker.isUserMemberOfGroup(user, throwException, ProjectForgeGroup.FINANCE_GROUP);
  }

  /**
   * @param kostZuweisung
   * @param kost1Id If null, then kost1 will be set to null;
   * @see BaseDao#getOrLoad(Integer)
   */
  public void setKost1(final KostZuweisungDO kostZuweisung, Integer kost1Id)
  {
    Kost1DO kost1 = kost1Dao.getOrLoad(kost1Id);
    kostZuweisung.setKost1(kost1);
  }

  /**
   * @param kostZuweisung
   * @param kost1Id If null, then kost2 will be set to null;
   * @see BaseDao#getOrLoad(Integer)
   */
  public void setKost2(final KostZuweisungDO kostZuweisung, Integer kost2Id)
  {
    Kost2DO kost2 = kost2Dao.getOrLoad(kost2Id);
    kostZuweisung.setKost2(kost2);
  }

  public void setKost1Dao(Kost1Dao kost1Dao)
  {
    this.kost1Dao = kost1Dao;
  }

  public void setKost2Dao(Kost2Dao kost2Dao)
  {
    this.kost2Dao = kost2Dao;
  }

  @Override
  public KostZuweisungDO newInstance()
  {
    return new KostZuweisungDO();
  }
}
