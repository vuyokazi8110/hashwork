/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashwork.client.content.system.address.views;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import hashwork.client.content.MainLayout;
import hashwork.client.content.system.address.AddressMenu;
import hashwork.client.content.system.demographics.forms.GenderListForm;
import hashwork.client.content.system.demographics.table.GenderListTable;
import hashwork.domain.ui.address.PostalCode;
import hashwork.services.ui.address.Impl.PostalCodeServiceImpl;
import hashwork.services.ui.address.PostalCodeService;

/**
 *
 * @author Garran
 */
public class PostalCodeTab extends VerticalLayout implements Button.ClickListener, Property.ValueChangeListener {
    private final PostalCodeService postalCodeService = new PostalCodeServiceImpl();
    private final MainLayout main;
    private final GenderListForm form;
    private final GenderListTable table;
    
    public PostalCodeTab(MainLayout app) {
        main = app;
        form = new GenderListForm();
        table = new GenderListTable(main);
        setSizeFull();
        addComponent(form);
        addComponent(table);
        addListeners();
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        final Button source = event.getButton();
        if (source == form.save) {
            saveForm(form.binder);
        } else if (source == form.edit) {
            setEditFormProperties();
        } else if (source == form.cancel) {
            getHome();
        } else if (source == form.update) {
            saveEditedForm(form.binder);
        } else if (source == form.delete) {
            deleteForm(form.binder);
        }
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        final Property property = event.getProperty();
        if (property == table) {
            final PostalCode locationType = postalCodeService.findById(table.getValue().toString());
            form.binder.setItemDataSource(new BeanItem<PostalCode>(locationType));
            setReadFormProperties();
        }
    }
    
    private void saveForm(FieldGroup binder) {
        try {
            binder.commit();
            postalCodeService.save(getEntity(binder));
            getHome();
            Notification.show("Record ADDED!", Notification.Type.TRAY_NOTIFICATION);
        } catch (FieldGroup.CommitException e) {
            Notification.show("Values MISSING!", Notification.Type.TRAY_NOTIFICATION);
            getHome();
        }
    }

    private void saveEditedForm(FieldGroup binder) {
        try {
            binder.commit();
            postalCodeService.update(getEntity(binder));
            getHome();
            Notification.show("Record UPDATED!", Notification.Type.TRAY_NOTIFICATION);
        } catch (FieldGroup.CommitException e) {
            Notification.show("Values MISSING!", Notification.Type.TRAY_NOTIFICATION);
            getHome();
        }
    }

    private void deleteForm(FieldGroup binder) {
        postalCodeService.delete(getEntity(binder));
        getHome();
    }

    private PostalCode getEntity(FieldGroup binder) {
        return ((BeanItem<PostalCode>) binder.getItemDataSource()).getBean();

    }

    private void getHome() {
        main.content.setSecondComponent(new AddressMenu(main, "7945"));
    }

    private void setEditFormProperties() {
        form.binder.setReadOnly(false);
        form.save.setVisible(false);
        form.edit.setVisible(false);
        form.cancel.setVisible(true);
        form.delete.setVisible(false);
        form.update.setVisible(true);
    }

    private void setReadFormProperties() {
        form.binder.setReadOnly(true);
        //Buttons Behaviou
        form.save.setVisible(false);
        form.edit.setVisible(true);
        form.cancel.setVisible(true);
        form.delete.setVisible(true);
        form.update.setVisible(false);
    }

    private void addListeners() {
        //Register Button Listeners
        form.save.addClickListener((Button.ClickListener) this);
        form.edit.addClickListener((Button.ClickListener) this);
        form.cancel.addClickListener((Button.ClickListener) this);
        form.update.addClickListener((Button.ClickListener) this);
        form.delete.addClickListener((Button.ClickListener) this);
        //Register Table Listerners
        table.addValueChangeListener((Property.ValueChangeListener) this);
    }
    
}
