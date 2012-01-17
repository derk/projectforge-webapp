/////////////////////////////////////////////////////////////////////////////
//
// Project ProjectForge Community Edition
//         www.projectforge.org
//
// Copyright (C) 2001-2012 Kai Reinhard (k.reinhard@micromata.com)
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

package org.projectforge.plugins.banking;

import org.apache.log4j.Logger;
import org.apache.wicket.model.PropertyModel;
import org.projectforge.web.wicket.AbstractEditForm;
import org.projectforge.web.wicket.components.MaxLengthTextArea;
import org.projectforge.web.wicket.components.MaxLengthTextField;


public class BankAccountEditForm extends AbstractEditForm<BankAccountDO, BankAccountEditPage>
{
  private static final long serialVersionUID = -5486405450790659784L;

  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BankAccountEditForm.class);

  public BankAccountEditForm(BankAccountEditPage parentPage, BankAccountDO data)
  {
    super(parentPage, data);
    this.colspan = 2;
  }

  @Override
  protected void init()
  {
    super.init();
    add(new MaxLengthTextField("name", new PropertyModel<String>(data, "name")));
    add(new MaxLengthTextField("accountNumber", new PropertyModel<String>(data, "accountNumber")));
    add(new MaxLengthTextField("bank", new PropertyModel<String>(data, "bank")));
    add(new MaxLengthTextField("bankIdentificationCode", new PropertyModel<String>(data, "bankIdentificationCode")));
    add(new MaxLengthTextArea("description", new PropertyModel<String>(data, "description")));
  }

  @Override
  protected Logger getLogger()
  {
    return log;
  }
}
