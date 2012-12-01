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

package org.projectforge.web.scripting;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.projectforge.scripting.ScriptParameter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


/**
 * For storing the user's last script calls. The parameters will be stored per script.
 * 
 * @author Kai Reinhard (k.reinhard@micromata.de)
 */
@XStreamAlias("ScriptCall")
public class ScriptCallData
{
  @XStreamAsAttribute
  private String scriptName;
  
  @XStreamImplicit(itemFieldName = "parameter")
  private List<ScriptParameter> scriptParameters;

  public ScriptCallData(final String name, final List<ScriptParameter> scriptParameters)
  {
    this.scriptName = name;
    this.scriptParameters = scriptParameters;
  }

  public String getScriptName()
  {
    return scriptName;
  }

  public void setScriptName(String scriptName)
  {
    this.scriptName = scriptName;
  }

  public List<ScriptParameter> getScriptParameter()
  {
    return scriptParameters;
  }

  public void setScriptParameter(List<ScriptParameter> scriptParameter)
  {
    this.scriptParameters = scriptParameter;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj instanceof ScriptCallData) {
      return StringUtils.equals(scriptName, ((ScriptCallData)obj).scriptName);
    }
    return false;
  }
}
