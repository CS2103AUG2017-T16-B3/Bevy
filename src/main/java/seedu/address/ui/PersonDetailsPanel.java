package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.ReadOnlyPerson;


/**
 * An UI component that displays information of a {@code Person} upon selection.
 */
public class PersonDetailsPanel extends UiPart<Region> {
    private static final String FXML = "PersonDetailsPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label date;
    @FXML
    private Label address;
    @FXML
    private Label postalCode;
    @FXML
    private Label email;
    @FXML
    private Label favourite;

    public PersonDetailsPanel() {
        super(FXML);
        name.setText("");
        phone.setText("");
        date.setText("");
        address.setText("");
        postalCode.setText("");
        email.setText("");

        registerAsAnEventHandler(this);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Person} properties
     * so that they will be notified of any changes.
     */
    private void setLabel(ReadOnlyPerson person) {
        name.textProperty().bind(Bindings.convert(person.nameProperty()));
        phone.textProperty().bind(Bindings.convert(person.phoneProperty()));
        date.textProperty().bind(Bindings.convert(person.dateProperty()));
        address.textProperty().bind(Bindings.convert(person.addressProperty()));
        postalCode.textProperty().bind(Bindings.convert(person.postalCodeProperty()));
        email.textProperty().bind(Bindings.convert(person.emailProperty()));
        favourite.textProperty().bind(Bindings.convert(person.favProperty()));
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        setLabel(event.getNewSelection().person);
    }
}

