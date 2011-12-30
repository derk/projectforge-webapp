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

package org.projectforge.plugins.marketing;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.projectforge.address.AddressDO;
import org.projectforge.common.DateHelper;
import org.projectforge.web.wicket.AbstractListForm;
import org.projectforge.web.wicket.DownloadUtils;
import org.projectforge.web.wicket.components.LabelValueChoiceRenderer;
import org.projectforge.web.wicket.components.SingleButtonPanel;

/**
 * The list formular for the list view (this example has no filter settings). See ToDoListPage for seeing how to use filter settings.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class AddressCampaignValueListForm extends AbstractListForm<AddressCampaignValueFilter, AddressCampaignValueListPage>
{
  private static final long serialVersionUID = 6190615904711764514L;

  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddressCampaignValueListForm.class);

  static final String ADDRESS_CAMPAIGN_VALUE_UNDEFINED = "-(null)-";

  @SpringBean(name = "addressCampaignDao")
  private AddressCampaignDao addressCampaignDao;

  @SpringBean(name = "addressCampaignValueExport")
  private AddressCampaignValueExport addressCampaignValueExport;

  private Integer addressCampaignId;

  @SuppressWarnings("unused")
  private String addressCampaignValue;

  private DropDownChoice<String> addressCampaignValueDropDownChoice;

  public AddressCampaignValueListForm(final AddressCampaignValueListPage parentPage)
  {
    super(parentPage);
  }

  @SuppressWarnings("serial")
  @Override
  protected void init()
  {
    super.init();
    this.addressCampaignId = searchFilter.getAddressCampaignId();
    this.addressCampaignValue = searchFilter.getAddressCampaignValue();
    final List<AddressCampaignDO> addressCampaignList = addressCampaignDao.getList(new AddressCampaignValueFilter());
    {
      final LabelValueChoiceRenderer<Integer> addressCampaignRenderer = new LabelValueChoiceRenderer<Integer>();
      for (final AddressCampaignDO addressCampaign : addressCampaignList) {
        addressCampaignRenderer.addValue(addressCampaign.getId(), addressCampaign.getTitle());
      }
      final DropDownChoice<Integer> addressCampaignChoice = new DropDownChoice<Integer>("addressCampaign", new PropertyModel<Integer>(this,
      "addressCampaignId"), addressCampaignRenderer.getValues(), addressCampaignRenderer) {
        @Override
        protected void onSelectionChanged(final Integer newSelection)
        {
          for (final AddressCampaignDO addressCampaign : addressCampaignList) {
            if (addressCampaign.getId().equals(addressCampaignId) == true) {
              searchFilter.setAddressCampaign(addressCampaign);
              final String oldValue = searchFilter.getAddressCampaignValue();
              // Is oldValue given and not "-(null)-"?
              if (oldValue != null && ADDRESS_CAMPAIGN_VALUE_UNDEFINED.equals(oldValue) == false) {
                // Check whether the campaign has the former selected value or not.
                boolean found = false;
                for (final String value : addressCampaign.getValuesArray()) {
                  if (oldValue.equals(value) == true) {
                    found = true;
                    break;
                  }
                }
                if (found == false) {
                  // Not found, therefore set the value to null:
                  searchFilter.setAddressCampaignValue(null);
                  addressCampaignValueDropDownChoice.modelChanged();
                }
              }
              break;
            }
          }
          refresh();
        }

        @Override
        protected boolean wantOnSelectionChangedNotifications()
        {
          return true;
        }
      };
      filterContainer.add(addressCampaignChoice);
    }
    {
      final LabelValueChoiceRenderer<String> choiceRenderer = getValueLabelValueChoiceRenderer();
      addressCampaignValueDropDownChoice = new DropDownChoice<String>("addressCampaignValue", new PropertyModel<String>(this,
      "addressCampaignValue"), choiceRenderer.getValues(), choiceRenderer) {
        @Override
        protected void onSelectionChanged(final String newSelection)
        {
          searchFilter.setAddressCampaignValue(newSelection);
          parentPage.refresh();
        }

        @Override
        protected boolean wantOnSelectionChangedNotifications()
        {
          return true;
        }

      };
      addressCampaignValueDropDownChoice.setNullValid(true);
      filterContainer.add(addressCampaignValueDropDownChoice);
    }
    filterContainer.add(new CheckBox("uptodate", new PropertyModel<Boolean>(getSearchFilter(), "uptodate")));
    filterContainer.add(new CheckBox("outdated", new PropertyModel<Boolean>(getSearchFilter(), "outdated")));
    filterContainer.add(new CheckBox("leaved", new PropertyModel<Boolean>(getSearchFilter(), "leaved")));

    filterContainer.add(new CheckBox("active", new PropertyModel<Boolean>(getSearchFilter(), "active")));
    filterContainer.add(new CheckBox("nonActive", new PropertyModel<Boolean>(getSearchFilter(), "nonActive")));
    filterContainer.add(new CheckBox("uninteresting", new PropertyModel<Boolean>(getSearchFilter(), "uninteresting")));
    filterContainer.add(new CheckBox("personaIngrata", new PropertyModel<Boolean>(getSearchFilter(), "personaIngrata")));
    filterContainer.add(new CheckBox("departed", new PropertyModel<Boolean>(getSearchFilter(), "departed")));

    // Radio choices:
    final RadioGroup<String> filterType = new RadioGroup<String>("filterType", new PropertyModel<String>(getSearchFilter(), "listType"));
    filterType.add(new Radio<String>("filter", new Model<String>("filter")).setOutputMarkupId(false));
    filterType.add(new Radio<String>("newest", new Model<String>("newest")));
    filterType.add(new Radio<String>("myFavorites", new Model<String>("myFavorites")));
    filterContainer.add(filterType);

    final Button exportButton = new Button("button", new Model<String>(getString("address.book.export"))) {
      @Override
      public final void onSubmit()
      {
        log.info("Exporting address list.");
        final List<AddressDO> list = parentPage.getList();
        final byte[] xls = addressCampaignValueExport.export(list, parentPage.personalAddressMap, parentPage.addressCampaignValueMap,
            getSearchFilter().getAddressCampaign() != null ? getSearchFilter().getAddressCampaign().getTitle() : "");
        if (xls == null || xls.length == 0) {
          addError("address.book.hasNoVCards");
          return;
        }
        final String filename = "ProjectForge-AddressCampaignValueExport_" + DateHelper.getDateAsFilenameSuffix(new Date()) + ".xls";
        DownloadUtils.setDownloadTarget(xls, filename);
      }
    };
    exportButton.setDefaultFormProcessing(false).add(new SimpleAttributeModifier("title", getString("address.book.export.tooltip")));
    final SingleButtonPanel exportPanel = new SingleButtonPanel(getNewActionButtonChildId(), exportButton);
    prependActionButton(exportPanel);
  }

  protected void refresh()
  {
    final LabelValueChoiceRenderer<String> choiceRenderer = getValueLabelValueChoiceRenderer();
    addressCampaignValueDropDownChoice.setChoiceRenderer(choiceRenderer);
    addressCampaignValueDropDownChoice.setChoices(choiceRenderer.getValues());
    parentPage.refresh();
  }

  private LabelValueChoiceRenderer<String> getValueLabelValueChoiceRenderer()
  {
    final LabelValueChoiceRenderer<String> choiceRenderer = new LabelValueChoiceRenderer<String>();
    if (searchFilter.getAddressCampaign() != null) {
      choiceRenderer.addValue(ADDRESS_CAMPAIGN_VALUE_UNDEFINED, "- " + getString("undefined") + " -");
      for (final String value : searchFilter.getAddressCampaign().getValuesArray()) {
        choiceRenderer.addValue(value, value);
      }
    }
    return choiceRenderer;
  }

  @Override
  protected boolean isFilterVisible()
  {
    return parentPage.isMassUpdateMode() == false;
  }

  @Override
  protected AddressCampaignValueFilter newSearchFilterInstance()
  {
    return new AddressCampaignValueFilter();
  }

  @Override
  protected Logger getLogger()
  {
    return log;
  }
}
