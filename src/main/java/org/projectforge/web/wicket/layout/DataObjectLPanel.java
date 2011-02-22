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

package org.projectforge.web.wicket.layout;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.projectforge.common.BeanHelper;
import org.projectforge.common.DatePrecision;
import org.projectforge.core.I18nEnum;
import org.projectforge.web.HtmlHelper;
import org.projectforge.web.calendar.DateTimeFormatter;
import org.projectforge.web.mobile.ActionLinkPanel;
import org.projectforge.web.mobile.ActionLinkType;
import org.projectforge.web.wicket.AbstractSelectPanel;
import org.projectforge.web.wicket.ImageDef;
import org.projectforge.web.wicket.WebConstants;
import org.projectforge.web.wicket.components.DatePanel;
import org.projectforge.web.wicket.components.DateTimePanel;

/**
 * This panel contains one or more field sets and is a convenient class for rendering data objects as forms or read-only views for different
 * target devices (such as deskop computers or mobile devices). <br/>
 * Every field set contains group panels for grouping data object fields.
 * @author Kai Reinhard (k.reinhard@micromata.de)
 * 
 */
public class DataObjectLPanel extends Panel
{
  private static final long serialVersionUID = 6302571306175282690L;

  private LayoutContext layoutContext;

  private RepeatingView fieldSetRepeater;

  private FieldSetLPanel fieldSetPanel;

  private GroupLPanel groupPanel;

  private LabelValueTableLPanel labelValueTablePanel;

  /**
   * Creates and add a new FieldSetLPanel (for normal or mobile version) depending on the layout context. If a previous field set panel
   * exist then it would be tagged as invisible, if that field set doesn't contain any children.
   * @param id
   * @param heading
   */
  public FieldSetLPanel newFieldSetPanel(final String heading)
  {
    closeFieldSetPanel();
    final String id = fieldSetRepeater.newChildId();
    if (layoutContext.isMobile() == true) {
      fieldSetPanel = new FieldSetMobileLPanel(id, heading);
    } else {
      fieldSetPanel = new FieldSetLPanel(id, heading);
    }
    fieldSetRepeater.add(fieldSetPanel);
    return fieldSetPanel;
  }

  public GroupLPanel newGroupPanel()
  {
    return newGroupPanel(null);
  }

  public GroupLPanel newGroupPanel(final String heading)
  {
    closeGroupPanel();
    ensureFieldSetPanel();
    final String id = fieldSetPanel.newChildId();
    if (layoutContext.isMobile() == true) {
      groupPanel = new GroupMobileLPanel(id, heading);
    } else {
      groupPanel = new GroupLPanel(id, heading);
    }
    fieldSetPanel.add(groupPanel);
    return groupPanel;
  }

  public LabelValueTableLPanel newLabelValueTablePanel()
  {
    closeLabelValueTablePanel();
    ensureGroupPanel();
    final String id = groupPanel.newChildId();
    labelValueTablePanel = new LabelValueTableLPanel(id);
    groupPanel.add(labelValueTablePanel);
    return labelValueTablePanel;
  }

  private DataObjectLPanel closeFieldSetPanel()
  {
    closeGroupPanel();
    if (fieldSetPanel != null && fieldSetPanel.hasChildren() == false) {
      fieldSetPanel.setVisible(false);
    }
    fieldSetPanel = null;
    return this;
  }

  private DataObjectLPanel closeGroupPanel()
  {
    closeLabelValueTablePanel();
    if (groupPanel != null && groupPanel.hasChildren() == false) {
      groupPanel.setVisible(false);
    }
    groupPanel = null;
    return this;
  }

  private DataObjectLPanel closeLabelValueTablePanel()
  {
    if (labelValueTablePanel != null && labelValueTablePanel.hasChildren() == false) {
      labelValueTablePanel.setVisible(false);
    }
    labelValueTablePanel = null;
    return this;
  }

  @Override
  protected void onBeforeRender()
  {
    super.onBeforeRender();
    closeFieldSetPanel();
  }

  public LabelLPanel addLabel(final String label, final LayoutLength labelLength)
  {
    return addLabel(label, labelLength, null);
  }

  public LabelLPanel addLabel(final String label, final LayoutLength labelLength, final LayoutAlignment alignment)
  {
    ensureGroupPanel();
    final LabelLPanel labelPanel;
    labelPanel = new LabelLPanel(groupPanel.newChildId(), labelLength, label);
    groupPanel.add(labelPanel);
    if (alignment != null) {
      labelPanel.setAlignment(alignment);
    }
    return labelPanel;
  }

  public LabelLPanel addHelpLabel(final String label, final LayoutLength length)
  {
    ensureGroupPanel();
    final LabelLPanel labelPanel;
    labelPanel = new LabelLPanel(groupPanel.newChildId(), length, label);
    labelPanel.getClassModifierComponent().add(WebConstants.HELP_CLASS);
    groupPanel.add(labelPanel);
    return labelPanel;
  }

  public RepeatingViewLPanel addRepeater(final LayoutLength length)
  {
    ensureGroupPanel();
    final RepeatingViewLPanel repeatingViewPanel;
    repeatingViewPanel = new RepeatingViewLPanel(groupPanel.newChildId(), length);
    groupPanel.add(repeatingViewPanel);
    return repeatingViewPanel;
  }

  /**
   * @deprecated Use {@link #addTextField(PanelContext)}
   */
  public IField addTextField(final Object data, final String property, final String label, final LayoutLength labelLength,
      final LayoutLength valueLength)
  {
    final PanelContext ctx = new PanelContext(data, property, valueLength, label, labelLength);
    addTextField(ctx);
    return ctx.getValueField();
  }

  /**
   * @deprecated Use {@link #addTextField(PanelContext)}
   */
  public IField addTextField(final Object data, final String property, final String label, final LayoutLength labelLength,
      final LayoutLength valueLength, final boolean newLineBetweenLabelAndTextfield)
  {
    return addTextField(data, property, label, labelLength, valueLength, null, newLineBetweenLabelAndTextfield);
  }

  /**
   * @deprecated Use {@link #addTextField(PanelContext)}
   */
  public IField addTextField(final Object data, final String property, final String label, final LayoutLength labelLength,
      final LayoutLength valueLength, final FieldType fieldType, final boolean newLineBetweenLabelAndTextField)
  {
    ensureGroupPanel();
    IField field;
    if (layoutContext.isMobileReadonly() == true) {
      field = addReadonlyTextField(data, property, label, labelLength, valueLength, fieldType, newLineBetweenLabelAndTextField);
    } else {
      field = groupPanel.addTextField(data, property, label, labelLength, valueLength, fieldType, newLineBetweenLabelAndTextField);
    }
    return field;
  }

  /**
   * @deprecated Use {@link #addTextField(PanelContext)}
   */
  public IField addTextField(final Object data, final String property, final LayoutLength valueLength)
  {
    final IField field = groupPanel.addTextField(data, property, valueLength);
    return field;
  }

  /**
   * @deprecated Use {@link #addTextField(PanelContext)}
   */
  public IField addTextField(final String label, final Object data, final String property, final LayoutLength valueLength)
  {
    final TextFieldLPanel field = groupPanel.addTextField(data, property, valueLength);
    field.getTextField().setLabel(new Model<String>(label));
    return field;
  }

  public IField addReadonlyTextField(final Object data, final String property, final String label, final LayoutLength labelLength,
      final LayoutLength valueLength)
  {
    return addReadonlyTextField(data, property, label, labelLength, valueLength, null, false);
  }

  public IField addReadonlyTextField(final Object data, final String property, final String label, final LayoutLength labelLength,
      final LayoutLength valueLength, final FieldType fieldType, final boolean newLineBetweenLabelAndTextField)
  {
    ensureGroupPanel();
    final Object value = BeanHelper.getNestedProperty(data, property);
    if (isBlank(value) == true) {
      return new DummyField();
    }
    return addReadonlyTextField(String.valueOf(value), label, labelLength, valueLength, fieldType, newLineBetweenLabelAndTextField);
  }

  public IField addReadonlyTextField(final String value, final String label, final LayoutLength labelLength, final LayoutLength valueLength)
  {
    return addReadonlyTextField(value, label, labelLength, valueLength, null, false);
  }

  public IField addReadonlyTextField(final String value, final String label, final LayoutLength labelLength,
      final LayoutLength valueLength, final FieldType fieldType, final boolean newLineBetweenLabelAndTextField)
  {
    ensureGroupPanel();
    IField field;
    if (isBlank(value) == true) {
      field = new DummyField();
    } else {
      ensureLabelValueTablePanel();
      final String wicketId;
      if (newLineBetweenLabelAndTextField == true) {
        newLabelValueTablePanel();
        wicketId = LabelValueTableLPanel.WICKET_ID_LABEL;
      } else {
        wicketId = LabelValueTableLPanel.WICKET_ID_VALUE;
      }
      if (fieldType == FieldType.E_MAIL) {
        field = new ActionLinkPanel(wicketId, ActionLinkType.MAIL, value);
      } else if (fieldType == FieldType.PHONE_NO) {
        field = new ActionLinkPanel(wicketId, ActionLinkType.CALL, value);
      } else if (fieldType == FieldType.MOBILE_PHONE_NO) {
        field = new ActionLinkPanel(wicketId, ActionLinkType.CALL_AND_SMS, value);
      } else if (fieldType == FieldType.WEB_PAGE) {
        field = new ActionLinkPanel(wicketId, ActionLinkType.EXTERNAL_URL, value);
      } else {
        field = new LabelLPanel(wicketId, labelLength, value);
      }
      labelValueTablePanel.add(label, (WebMarkupContainer) field, newLineBetweenLabelAndTextField);
    }
    return field;
  }

  /**
   * @deprecated Use {@link #addTextArea(PanelContext)}
   */
  public IField addTextArea(final Object data, final String property, final String label, final LayoutLength labelLength,
      final LayoutLength valueLength, final boolean newLineBetweenLabelAndTextarea)
  {
    return addTextArea(new PanelContext(data, property, valueLength, label, labelLength)
        .setBreakBetweenLabelAndField(newLineBetweenLabelAndTextarea));
  }

  /**
   * @return the created field or a dummy IField if the field is e. g. empty in read-only mode.
   */
  public IField addTextArea(final PanelContext ctx)
  {
    ensureGroupPanel();
    IField field;
    if (layoutContext.isMobileReadonly() == true) {
      final String wicketId;
      if (ctx.isBreakBetweenLabelAndField() == true) {
        newLabelValueTablePanel();
        wicketId = LabelValueTableLPanel.WICKET_ID_LABEL;
      } else {
        wicketId = LabelValueTableLPanel.WICKET_ID_VALUE;
      }
      final Object value = BeanHelper.getNestedProperty(ctx.getData(), ctx.getProperty());
      if (isBlank(value) == true) {
        return new DummyField();
      }
      final String displayValue;
      if (value instanceof String) {
        displayValue = HtmlHelper.formatText((String) value, true);
      } else {
        displayValue = HtmlHelper.formatText(String.valueOf(value), true);
      }
      ensureLabelValueTablePanel();
      final LabelLPanel labelPanel = new LabelLPanel(wicketId, ctx.getValueLength(), displayValue);
      labelPanel.getWrappedComponent().setEscapeModelStrings(false);
      field = labelValueTablePanel.add(ctx.getLabel(), labelPanel, ctx.isBreakBetweenLabelAndField());
    } else {
      field = groupPanel.addTextArea(ctx);
    }
    return field;
  }

  /**
   * @deprecated Use {@link #addTextField(PanelContext)}
   */
  @SuppressWarnings("serial")
  public IField addDropDownChoice(final Object data, final String property, final String label, final LayoutLength labelLength,
      final DropDownChoice< ? > dropDownChoice, final LayoutLength valueLength)
  {
    ensureGroupPanel();
    IField field;
    if (layoutContext.isMobileReadonly() == true) {
      final Object value = BeanHelper.getNestedProperty(data, property);
      if (isBlank(value) == true) {
        return new DummyField();
      }
      ensureLabelValueTablePanel();
      final String displayValue;
      if (value instanceof I18nEnum) {
        displayValue = getString(((I18nEnum) value).getI18nKey());
      } else {
        displayValue = String.valueOf(value);
      }
      field = new LabelLPanel(LabelValueTableLPanel.WICKET_ID_VALUE, labelLength, displayValue);
      field = labelValueTablePanel.add(label, (WebMarkupContainer) field);
    } else {
      field = new DropDownChoiceLPanel(groupPanel.newChildId(), valueLength, dropDownChoice);
      groupPanel.add(new LabelLPanel(groupPanel.newChildId(), labelLength, label, (AbstractLPanel) field, true));
      ((DropDownChoiceLPanel) field).getDropDownChoice().setLabel(new Model<String>() {
        @Override
        public String getObject()
        {
          return label;
        }
      });
      groupPanel.add(field);
    }
    return field;
  }

  /**
   * @deprecated Use {@link #addDropDownChoice(DropDownChoice, PanelContext)}
   */
  @SuppressWarnings("serial")
  public IField addDropDownChoice(final Object data, final String property, final String label, final DropDownChoice< ? > dropDownChoice,
      final LayoutLength valueLength)
  {
    ensureGroupPanel();
    IField field;
    if (layoutContext.isMobileReadonly() == true) {
      final Object value = BeanHelper.getNestedProperty(data, property);
      if (isBlank(value) == true) {
        return new DummyField();
      }
      ensureLabelValueTablePanel();
      final String displayValue;
      if (value instanceof I18nEnum) {
        displayValue = getString(((I18nEnum) value).getI18nKey());
      } else {
        displayValue = String.valueOf(value);
      }
      field = labelValueTablePanel.add("", displayValue);
    } else {
      field = new DropDownChoiceLPanel(groupPanel.newChildId(), valueLength, dropDownChoice);
      if (dropDownChoice != null) {
        dropDownChoice.setLabel(new Model<String>() {
          @Override
          public String getObject()
          {
            return label;
          }
        });
      }
      groupPanel.add(field);
    }
    return field;
  }

  /**
   * If the value is type of I18Enum then the localized string is shown in read-only mode.
   * @param ctx
   * @param dropDownChoice value field to add.
   */
  public IField addDropDownChoice(final DropDownChoice< ? > dropDownChoice, final PanelContext ctx)
  {
    ensureGroupPanel();
    IField field;
    LabelLPanel labelPanel = null;
    if (layoutContext.isMobileReadonly() == true) {
      final Object value = BeanHelper.getNestedProperty(ctx.getData(), ctx.getProperty());
      if (isBlank(value) == true) {
        return new DummyField();
      }
      ensureLabelValueTablePanel();
      final String displayValue;
      if (value instanceof I18nEnum) {
        displayValue = getString(((I18nEnum) value).getI18nKey());
      } else {
        displayValue = String.valueOf(value);
      }
      labelPanel = new LabelLPanel(LabelValueTableLPanel.WICKET_ID_VALUE, ctx.getLabelLength(), displayValue);
      field = labelValueTablePanel.add(ctx.getLabel(), (WebMarkupContainer) labelPanel);
    } else {
      field = new DropDownChoiceLPanel(groupPanel.newChildId(), ctx.getValueLength(), dropDownChoice);
      if (ctx.getLabelLength() != null) {
        labelPanel = new LabelLPanel(groupPanel.newChildId(), ctx.getLabelLength(), ctx.getLabel(), (AbstractLPanel) field, true);
        groupPanel.add(labelPanel);
      }
      if (ctx.getLabel() != null) {
        ((DropDownChoiceLPanel) field).getDropDownChoice().setLabel(new Model<String>(ctx.getLabel()));
      }
      groupPanel.add(field);
    }
    ctx.internalSetValueField(field);
    ctx.internalSetLabelPanel(labelPanel);
    return field;
  }

  public IField addListMultipleChoice(final ListMultipleChoice< ? > listMultipleChoice, final PanelContext ctx)
  {
    ensureGroupPanel();
    IField field;
    LabelLPanel labelPanel = null;
    field = new ListMultipleChoiceLPanel(groupPanel.newChildId(), ctx.getValueLength(), listMultipleChoice);
    if (ctx.getLabelLength() != null) {
      labelPanel = new LabelLPanel(groupPanel.newChildId(), ctx.getLabelLength(), ctx.getLabel(), (AbstractLPanel) field, true);
      groupPanel.add(labelPanel);
    }
    if (ctx.getLabel() != null) {
      listMultipleChoice.setLabel(new Model<String>(ctx.getLabel()));
    }
    if (ctx.isBreakBetweenLabelAndField() == true) {
      ((ListMultipleChoiceLPanel) field).setBreakBefore();
    }
    if (ctx.getCssStyle() != null) {
      listMultipleChoice.add(new SimpleAttributeModifier("style", ctx.getCssStyle()));
    }
    groupPanel.add(field);
    ctx.internalSetValueField(field);
    ctx.internalSetLabelPanel(labelPanel);
    return field;
  }

  public IField addDateFieldPanel(final Object data, final String property, final String label, final LayoutLength labelLength,
      final DatePrecision precision, final LayoutLength valueLength)
  {
    ensureGroupPanel();
    IField field;
    final Object value = BeanHelper.getNestedProperty(data, property);
    if (isBlank(value) == true) {
      field = new DummyField();
    } else {
      ensureLabelValueTablePanel();
      final String displayValue;
      if (precision == DatePrecision.DAY) {
        displayValue = DateTimeFormatter.instance().getFormattedDate(value);
      } else {
        displayValue = DateTimeFormatter.instance().getFormattedDateTime((Date) value);
      }
      field = new LabelLPanel(LabelValueTableLPanel.WICKET_ID_VALUE, labelLength, displayValue);
      labelValueTablePanel.add(label, (WebMarkupContainer) field);
    }
    return field;
  }

  @SuppressWarnings("serial")
  public IField addDateFieldPanel(final Object data, final String property, final String label, final LayoutLength labelLength,
      final DatePanel datePanel, final LayoutLength valueLength)
  {
    ensureGroupPanel();
    final IField field = new DateFieldLPanel(groupPanel.newChildId(), valueLength, datePanel);
    groupPanel.add(new LabelLPanel(groupPanel.newChildId(), labelLength, label, (AbstractLPanel) field, true));
    ((DateFieldLPanel) field).getDatePanel().setLabel(new Model<String>() {
      @Override
      public String getObject()
      {
        return label;
      }
    });
    groupPanel.add(field);
    return field;
  }

  @SuppressWarnings("serial")
  public IField addDateTimePanel(final Object data, final String property, final String label, final LayoutLength labelLength,
      final DateTimePanel dateTimePanel, final LayoutLength valueLength)
  {
    ensureGroupPanel();
    final IField field = new DateTimeFieldLPanel(groupPanel.newChildId(), valueLength, dateTimePanel);
    groupPanel.add(new LabelLPanel(groupPanel.newChildId(), labelLength, label, (AbstractLPanel) field, true));
    ((DateTimeFieldLPanel) field).getDatePanel().setLabel(new Model<String>() {
      @Override
      public String getObject()
      {
        return label;
      }
    });
    groupPanel.add(field);
    return field;
  }

  @SuppressWarnings("serial")
  public IField addSelectPanel(final String label, final LayoutLength labelLength, final AbstractSelectPanel< ? > selectPanel,
      final LayoutLength valueLength)
  {
    ensureGroupPanel();
    final SelectLPanel field = new SelectLPanel(groupPanel.newChildId(), valueLength, selectPanel);
    final LabelLPanel labelPanel = new LabelLPanel(groupPanel.newChildId(), labelLength, label, (AbstractLPanel) field, true);
    field.getSelectPanel().setLabel(new Model<String>() {
      @Override
      public String getObject()
      {
        return label;
      }
    });
    groupPanel.add(labelPanel);
    labelPanel.setLabelFor(field.getWrappedComponent());
    groupPanel.add(field);
    return field;
  }

  /**
   * @deprecated Use {@link #addContainer(WebMarkupContainer, PanelContext)}
   */
  public ContainerLPanel addContainer(final WebMarkupContainer container, final LayoutLength valueLength)
  {
    return addContainer(null, null, container, valueLength);
  }

  /**
   * @deprecated Use {@link #addTextField(PanelContext)}
   */
  public ContainerLPanel addContainer(final String label, final LayoutLength labelLength, final WebMarkupContainer container,
      final LayoutLength valueLength)
  {
    ensureGroupPanel();
    final ContainerLPanel containerPanel = new ContainerLPanel(groupPanel.newChildId(), valueLength, container);
    if (label != null) {
      final LabelLPanel labelPanel = new LabelLPanel(groupPanel.newChildId(), labelLength, label, container, true);
      groupPanel.add(labelPanel);
    }
    groupPanel.add(containerPanel);
    return containerPanel;
  }

  public ContainerLPanel addContainer(final WebMarkupContainer container, final PanelContext ctx)
  {
    ensureGroupPanel();
    final ContainerLPanel containerPanel = new ContainerLPanel(groupPanel.newChildId(), ctx.getValueLength(), container);
    ctx.internalSetValueField(containerPanel);
    if (ctx.getLabelLength() != null) {
      final LabelLPanel labelPanel = new LabelLPanel(groupPanel.newChildId(), ctx.getLabelLength(), ctx.getLabel(), container, true);
      groupPanel.add(labelPanel);
    }
    if (container instanceof FormComponent< ? > && ctx.getLabel() != null) {
      ((FormComponent< ? >) container).setLabel(new Model<String>(ctx.getLabel()));
    }
    groupPanel.add(containerPanel);
    return containerPanel;
  }

  @SuppressWarnings("serial")
  public IField addTextField(final String label, final LayoutLength labelLength, final TextField< ? > textField,
      final LayoutLength valueLength)
  {
    ensureGroupPanel();
    final IField field = new TextFieldLPanel(groupPanel.newChildId(), valueLength, textField);
    groupPanel.add(new LabelLPanel(groupPanel.newChildId(), labelLength, label, (AbstractLPanel) field, true));
    ((TextFieldLPanel) field).getTextField().setLabel(new Model<String>() {
      @Override
      public String getObject()
      {
        return label;
      }
    });
    groupPanel.add(field);
    return field;
  }

  /**
   * @param textField
   * @param valueLength
   * @return
   */
  public IField addTextField(final PanelContext ctx)
  {
    ensureGroupPanel();
    IField field;
    if (layoutContext.isMobileReadonly() == true) {
      field = addReadonlyTextField(ctx.getData(), ctx.getProperty(), ctx.getLabel(), ctx.getLabelLength(), ctx.getValueLength(), ctx
          .getFieldType(), ctx.isBreakBetweenLabelAndField());
    } else {
      field = groupPanel.addTextField(ctx);
    }
    return field;
  }

  /**
   * @param textField
   * @param valueLength
   * @return
   */
  public void addTextField(final TextField< ? > textField, final PanelContext ctx)
  {
    ensureGroupPanel();
    groupPanel.addTextField(textField, ctx);
  }

  /**
   * @param textField
   * @param valueLength
   * @return
   */
  public void addPasswordTextField(final PasswordTextField textField, final PanelContext ctx)
  {
    ensureGroupPanel();
    groupPanel.addPasswordTextField(textField, ctx);
  }

  /**
   * @param textField
   * @param valueLength
   * @return
   */
  public IField addTextField(final TextField< ? > textField, final LayoutLength valueLength)
  {
    ensureGroupPanel();
    final IField field = new TextFieldLPanel(groupPanel.newChildId(), valueLength, textField);
    groupPanel.add(field);
    return field;
  }

  /**
   * @param label Only used for setLabel (needed by validation messages).
   * @param textField
   * @param valueLength
   * @return
   */
  public IField addTextField(final String label, final TextField< ? > textField, final LayoutLength valueLength)
  {
    final IField field = addTextField(textField, valueLength);
    textField.setLabel(new Model<String>(label));
    return field;
  }

  public IField addJiraIssuesPanel(final LayoutLength length, final String text)
  {
    final IField field = new JiraIssuesLPanel(groupPanel.newChildId(), length, text);
    groupPanel.add(field);
    return field;
  }

  /**
   * property must be of type boolean.
   * @param data
   * @param property
   * @return
   */
  public IField addCheckBox(final Object data, final String property)
  {
    return addCheckBox(new PanelContext(data, property));
  }

  /**
   * property must be of type boolean.
   * @param data
   * @param property
   * @return
   */
  public IField addCheckBox(final PanelContext ctx)
  {
    ensureGroupPanel();
    IField field;
    if (layoutContext.isReadonly() == true) {
      final Object value = BeanHelper.getNestedProperty(ctx.getData(), ctx.getProperty());
      if (value != null && value instanceof Boolean && ((Boolean) value) == true) {
        field = new ImageLPanel(groupPanel.newChildId(), ImageDef.ACCEPT);
      } else {
        field = new ImageLPanel(groupPanel.newChildId(), ImageDef.DENY);
      }
    } else {
      field = new CheckBoxLPanel(groupPanel.newChildId(), ctx.getData(), ctx.getProperty());
    }
    if (ctx.getLabelLength() != null) {
      final LabelLPanel labelPanel = new LabelLPanel(groupPanel.newChildId(), ctx.getLabelLength(), ctx.getLabel(), (AbstractLPanel) field,
          ctx.isBreakBefore());
      ctx.internalSetLabelPanel(labelPanel);
      groupPanel.add(labelPanel);
      ((CheckBoxLPanel) field).getCheckBox().setLabel(new Model<String>(ctx.getLabel()));
      if (ctx.getTooltip() != null) {
        labelPanel.setTooltip(ctx.getTooltip());
      }
    }
    if (ctx.getTooltip() != null) {
      ((CheckBoxLPanel) field).setTooltip(ctx.getTooltip());
    }
    groupPanel.add(field);
    ctx.internalSetValueField(field);
    return field;
  }

  /**
   * property must be of type boolean.
   * @param data
   * @param property
   * @return
   */
  public IField addImage(final ImageDef imageDef)
  {
    final IField field = new ImageLPanel(groupPanel.newChildId(), imageDef);
    groupPanel.add(field);
    return field;
  }

  private boolean isBlank(final Object value)
  {
    if (value == null) {
      return true;
    } else if (value instanceof String == true) {
      return StringUtils.isBlank((String) value);
    }
    return false;
  }

  /**
   * @param container The parent container.
   * @param layoutContext The current used layout (ro/rw, mobile or desktop).
   */
  public DataObjectLPanel(final String id, final LayoutContext layoutContext)
  {
    super(id);
    this.layoutContext = layoutContext;
    this.fieldSetRepeater = new RepeatingView("fieldSetRepeater");
    add(fieldSetRepeater);
  }

  private void ensureFieldSetPanel()
  {
    if (fieldSetPanel == null) {
      newFieldSetPanel(null);
    }
  }

  private void ensureGroupPanel()
  {
    if (groupPanel == null) {
      newGroupPanel();
    }
  }

  private void ensureLabelValueTablePanel()
  {
    if (labelValueTablePanel == null) {
      newLabelValueTablePanel();
    }
  }
}
